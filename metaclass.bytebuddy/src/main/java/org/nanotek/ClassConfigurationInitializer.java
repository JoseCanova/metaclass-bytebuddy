package org.nanotek;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.Enum;
import javax.annotation.Nullable;

import org.nanotek.meta.model.rdbms.RdbmsIndex;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.BuilderMetaClass;
import org.nanotek.metaclass.BuilderMetaClassRegistry;
import org.nanotek.metaclass.ProcessedForeignKeyRegistry;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;
import org.nanotek.metaclass.bytebuddy.attributes.AttributeBaseBuilder;

import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;


/**
 * The Initializer needs a "redesign" now that the ForeignKeys and Index were added 
 * to the MetaClass configuration.
 * The Class construction now need to be decomposed into basic 4 steps.
 * First Step the Preparation that will construct the "Class Skeleton" 
 * that is simply the class definition without any internal structure.
 * Second step is to define the identity attributes of the class (for now considering a model with a simple 
 * primary key).
 * Third step is the configuration of attributes that are internal attributes 
 * of the "rdbms table" which means they are not dependent of
 */
public interface ClassConfigurationInitializer {
	
	public static final BuilderMetaClassRegistry builderMetaClassRegistry 
							= new BuilderMetaClassRegistry();
	
	public static final ProcessedForeignKeyRegistry processedForeignKeyRegistry
						= new ProcessedForeignKeyRegistry();
	
	public  List<RdbmsMetaClass> getMetaClasses(@Nullable String uriEndpont) ;

	default List<Class<?>> configureMetaClasses (String uriEndpont
											,EntityPathConfigurableClassLoader byteArrayClassLoader,
											 MetaClassRegistry<?> metaClassRegistry ) throws Exception{
		
		List<RdbmsMetaClass> resultMetaClasses = getMetaClasses(uriEndpont); 
		List<RdbmsMetaClass> joinMetaClasses = new ArrayList<>();
		//TODO: need data structures for metaclasses and join tables (represented as metaclasses)
		List<RdbmsMetaClass> entityMetaClasses = 	resultMetaClasses
												.stream()
												.filter(mc -> {var isJoin = mc.isJoinMetaClass();
																if(isJoin)
																	joinMetaClasses.add(mc);
																return !isJoin;})
												.collect(Collectors.toList());

		entityMetaClasses.
		stream()
		.forEach(mc ->{
			mountRdbmsMetaClassConfiguration(mc,byteArrayClassLoader);
			prepareSimpleAttributes(mc);
			prepareForeignAttributes(mc);
		});
		
		processedForeignKeyRegistry
		.getProcessedForeignKeys()
		.forEach(fk ->{
			AttributeBaseBuilder
			.on()
			.generateParentRelationAttribute(fk,builderMetaClassRegistry);
		});
		
		//TODO: implement relation classification based on index defined for fk's
		//TODO: move the code to metaclass-bytebuddy.
		joinMetaClasses
		.forEach(joinMetaClass ->
								  processJoinTableRelations(joinMetaClass, builderMetaClassRegistry));
		
		ArrayList<Class<?>> theList = new ArrayList<>();
		entityMetaClasses.forEach(mc->{
			try {
					String key = mc.getTableName();
					BuilderMetaClass bmc= builderMetaClassRegistry.getBuilderMetaClass(key);
					Unloaded<?> unLoaded  = bmc.builder().make();
					String className = unLoaded.getTypeDescription().getActualName();
		    		Class<?> clazz = byteArrayClassLoader.defineClass(className, unLoaded.getBytes());
		    		theList.add(clazz);
			} catch (Exception e) {
				e.printStackTrace();
	    		throw new RuntimeException(e);
	    	}
		});
		
		theList.forEach(clazz ->{
    		metaClassRegistry.registryEntityClass(Class.class.<Class<Base<?>>>cast(clazz));});
		return theList;
		//		System.exit(0);
	}
	
	//TODO: implement oneone and onemany with join table indexes.
	default void processJoinTableRelations( RdbmsMetaClass joinMetaClass,
												  BuilderMetaClassRegistry buildermetaclassregistry2) {
		
		List<RdbmsIndex> indexes = joinMetaClass.getRdbmsIndexes();
		JoinTableRelationType relationType = classifyRelationType(indexes);
		switch(relationType) {
			case MANYMANY:
				processManyToManyRelations(joinMetaClass,buildermetaclassregistry2);
				break;
			default:
				throw new RuntimeException("other join table indexes not yet implemented");
		}
		
	}

	enum JoinTableRelationType {
		ONEONE,
		ONEMANY,
		MANYMANY;
	}

	/**
	 * This method classify the relation type between to classes in a relation-table considering the happy path
	 * which means the indexes were properly defined by the relational model administrator.
	 * @param indexes
	 * @return
	 */
	default JoinTableRelationType classifyRelationType(List<RdbmsIndex> indexes) {
		long count = indexes.stream().filter(idx -> idx.getIsUnique()).count();
		return  count > 0 && count == indexes.size()?JoinTableRelationType.ONEONE:count>0?JoinTableRelationType.ONEMANY:JoinTableRelationType.MANYMANY;
	}

	default void processManyToManyRelations(RdbmsMetaClass joinMetaClass,
			BuilderMetaClassRegistry buildermetaclassregistry2) {
		AttributeBaseBuilder.on().processManyToManyRelations(joinMetaClass, buildermetaclassregistry2);
	}


	default void prepareSimpleAttributes(RdbmsMetaClass mc) {
	
				
				BuilderMetaClass bmc = builderMetaClassRegistry.getBuilderMetaClass(mc.getTableName());
				Builder <?> builder=bmc.builder();
				Builder <?> attributeBuilder = AttributeBaseBuilder
												.on().generateClassAttributes(mc , 
														builder);
				BuilderMetaClass abmc = new BuilderMetaClass(attributeBuilder,mc);
				builderMetaClassRegistry.registryBuilderMetaClass(mc.getTableName(), abmc);
	}

	default void prepareForeignAttributes(RdbmsMetaClass mc) {
		BuilderMetaClass bmc = builderMetaClassRegistry.getBuilderMetaClass(mc.getTableName());
		Builder <?> builder=bmc.builder();
		Builder <?> foreignAttributeBuilder = AttributeBaseBuilder
													.on()
													.generateForeignKeyClassAttributes(mc , 
															builder,
															builderMetaClassRegistry,
															processedForeignKeyRegistry);
				BuilderMetaClass fabmc = new BuilderMetaClass(foreignAttributeBuilder,mc);
				builderMetaClassRegistry.registryBuilderMetaClass(mc.getTableName(), fabmc);
	}

	default RdbmsEntityBaseBuddy mountRdbmsMetaClassConfiguration(RdbmsMetaClass mc, EntityPathConfigurableClassLoader byteArrayClassLoader) {
		var base = prepareEntityBaseBuddy(mc);
		var byteBuddy = base.generateByteBuddy();
		var baseBuilder = base.initializeInternalStatebuilder(byteBuddy, mc,byteArrayClassLoader);
		BuilderMetaClass bmc = new BuilderMetaClass(baseBuilder,mc);
		
		builderMetaClassRegistry
						.registryBuilderMetaClass(mc.getTableName(), bmc);
		return base;
	}

	default RdbmsEntityBaseBuddy prepareEntityBaseBuddy(RdbmsMetaClass mc) {
		return RdbmsEntityBaseBuddy.instance(mc);
	}

	/**
	 * This method is deprecated since the new 
	 * @deprecated 
	 * @param theClass
	 * @param classLoader
	 * @param metaClassRegistry
	 * @return
	 */
	default Class<?> metaClass(RdbmsMetaClass theClass , 
										MetaClassVFSURLClassLoader classLoader , 
										MetaClassRegistry<?> metaClassRegistry) {
	    	try {
	    		RdbmsEntityBaseBuddy eb = RdbmsEntityBaseBuddy.instance(theClass);
	    		Unloaded<?> loaded = eb.getBytesForClassLoader();
	    		String className = loaded.getTypeDescription().getActualName();
	    		Class<?> clazz = classLoader.defineClass(className, loaded.getBytes());
	    		metaClassRegistry.registryEntityClass(Class.class.<Class<Base<?>>>cast(clazz));
	    		return  clazz;	
	    	} catch (Exception e) {
	    		throw new RuntimeException(e);
	    	}
	    }
		
		
		 default Class<?> getIdClass(Class<?> y) {
				return Stream.of(y.getDeclaredFields())
				.filter( f -> hasIdAnnotation(f))
				.map(f -> f.getType())
				.findFirst().orElseThrow();
			}
		 
		 default Boolean hasIdAnnotation(Field f) {
				return Stream.of(f.getAnnotations()).filter(a ->a.annotationType().equals(jakarta.persistence.Id.class)).count()==1;
			}

		
}
