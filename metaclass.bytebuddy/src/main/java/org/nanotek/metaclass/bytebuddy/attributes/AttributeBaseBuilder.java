package org.nanotek.metaclass.bytebuddy.attributes;


import java.util.List;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.metaclass.BuilderMetaClass;
import org.nanotek.metaclass.BuilderMetaClassRegistry;
import org.nanotek.metaclass.ProcessedForeignKeyRegistry;
import org.nanotek.metaclass.bytebuddy.Holder;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.ForeignKeyMetaClassRecord;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.JoinTableAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.ManyToManyAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.OneToManyAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.OneToOneAnnotationDescrptionFactory;

import jakarta.persistence.CascadeType;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;


//TODO: verify best is class name is the more appropriate name.
public interface AttributeBaseBuilder<T extends Builder<?> , M extends RdbmsMetaClass> {

	
	public static <T extends Builder<?> , M extends RdbmsMetaClass>  
		AttributeBaseBuilder<T,M> on(){
		return new AttributeBaseBuilder<T,M>(){};
	}
	
	
	//TODO: Fix type check 
	//TODO: verify method implementation for a functional method
	default T generateClassAttributes(M metaClass , T builder) {
		Holder<Builder<?>> holder = Holder.of(builder);
		List<RdbmsMetaClassAttribute> atts = metaClass.getMetaAttributes();
		atts
		.stream()
		.filter(att -> att.isPartOfForeignKey()==false)
		.forEach(att -> {
			AnnotationDescription[] descs = buildAnnotations(att);
							Class<?> theJavaClass = getJavaClass(att.getClazz());
							Builder<?> newBuilder = holder.get().orElseThrow()
									.defineProperty(att.getFieldName(), theJavaClass)
									.annotateField(descs);
							holder.put(newBuilder);
		});
		
		return (T)holder.get(). orElseThrow();
	}
	
	default T generateForeignKeyClassAttributes(M metaClass, T builder,
			BuilderMetaClassRegistry buildermetaclassregistry,
			ProcessedForeignKeyRegistry processedForeignKeyRegistry) {
		
		Holder<Builder<?>> holder = Holder.of(builder);

		List<RdbmsMetaClassAttribute> atts = metaClass.getMetaAttributes();
		atts
		.stream()
		.filter(att -> att.isPartOfForeignKey())
		.forEach(att -> {
			RdbmsMetaClassForeignKey fk = getForeignKey(att,metaClass.getRdbmsForeignKeys());
			BuilderMetaClass bmc = buildermetaclassregistry.getBuilderMetaClass(fk.getTableName());
			TypeDescription td = bmc.builder().toTypeDescription();
			
			RdbmsMetaClassAttribute parentAttribute = 
									findMetaClassAttributeByColumnName(bmc.metaClass(),fk.getColumnName());
			
			Builder<?> mutableBuilder = holder
				.get()
				.map(bd -> {
					AnnotationDescription[] descs =  buildRelationShipAnnotations(att,
							metaClass,
							buildermetaclassregistry,
							processedForeignKeyRegistry);
					return bd
							.defineProperty(parentAttribute.getFieldName(), td)
							.annotateField(descs);
				}).get();
			
			holder.put(mutableBuilder);
		});
		
		return (T)holder.get(). orElseThrow();
	};
	
	default void generateParentRelationAttribute(RdbmsMetaClassForeignKey fk,
			BuilderMetaClassRegistry buildermetaclassregistry) {
		
		String fkTableName = fk.getJoinTableName();
		RdbmsMetaClass fkMetaClass = buildermetaclassregistry.getBuilderMetaClass(fkTableName).metaClass();
		if(verifyUniqueIndex(fk,fkMetaClass)) {
			generateParentRelationClassAttribute(fk,buildermetaclassregistry);
		}else {
			generateParentCollectionsClassAttribute(fk,buildermetaclassregistry);
		}
	}
	
	default Boolean verifyUniqueIndex(RdbmsMetaClassForeignKey fk, RdbmsMetaClass fkMetaClass) {
		return fkMetaClass
		.getRdbmsIndexes()
		.stream()
		.filter(index -> index.getColumnNames().contains(fk.getJoinColumnName()))
		.filter(index -> index.getIsUnique())
		.count()>0L;
	}

	//TODO: simplify the implementation of both methods.
	default void generateParentRelationClassAttribute(RdbmsMetaClassForeignKey fk,
			BuilderMetaClassRegistry buildermetaclassregistry) {
		BuilderMetaClass oneBuilderMetaClass =  buildermetaclassregistry.getBuilderMetaClass(fk.getTableName());
		RdbmsMetaClass oneMetaClass = oneBuilderMetaClass.metaClass();
		Builder<?> oneBuilder = oneBuilderMetaClass.builder();
		//default one attribute for primary key using primary key field name for Many Side
		RdbmsMetaClassAttribute mappedByAttribute = findIdAttribute(oneMetaClass);
		BuilderMetaClass childBuilderMetaClass =  buildermetaclassregistry.getBuilderMetaClass(fk.getJoinTableName());
		RdbmsMetaClass childMetaClass = childBuilderMetaClass.metaClass();
		Builder<?> childBuilder = childBuilderMetaClass.builder();
		
										//TODO: move this to the proper annotation factory.
										 TypeDescription cascadeTypeTd = new TypeDescription.ForLoadedType(CascadeType.class);
									     EnumerationDescription cascadeTypeEd = new EnumerationDescription.ForLoadedEnumeration(CascadeType.ALL);
									     var av = AnnotationValue.ForDescriptionArray.of(cascadeTypeTd, new EnumerationDescription[]{cascadeTypeEd});
										
									     ForeignKeyMetaClassRecord theRecord = new  ForeignKeyMetaClassRecord(fk, oneMetaClass,buildermetaclassregistry);
									     AnnotationDescription oneToOneAnnotationDescription = OneToOneAnnotationDescrptionFactory
																					    		 .on()
																					    		 .buildAnnotationDescription(theRecord).orElseThrow();

										
										//TypeDescription setTypeDescription = new TypeDescription.ForLoadedType(java.util.Set.class );
										//Here the type definition is relative to the foreign key class (the holder of the fk attribute)
										TypeDefinition tdClazz = childBuilder.toTypeDescription();
										Builder<?> oneResultantBuilder = oneBuilder
										.defineProperty( childMetaClass.getClassName().toLowerCase() , tdClazz)
										.annotateField(new AnnotationDescription[] {oneToOneAnnotationDescription});
										
										BuilderMetaClass bmc = new BuilderMetaClass(oneResultantBuilder,oneMetaClass);
										buildermetaclassregistry.registryBuilderMetaClass(oneMetaClass.getTableName(), bmc);
	}

	default void generateParentCollectionsClassAttribute(RdbmsMetaClassForeignKey fk,
													BuilderMetaClassRegistry buildermetaclassregistry) {
		//TODO: refactor this method generating the correct parameters for onetoman
		//need to first locate if the "target class" which will be added the collection is valid 
		
		BuilderMetaClass oneBuilderMetaClass =  buildermetaclassregistry.getBuilderMetaClass(fk.getTableName());
		RdbmsMetaClass oneMetaClass = oneBuilderMetaClass.metaClass();
		Builder<?> oneBuilder = oneBuilderMetaClass.builder();
		//default one attribute for primary key using primary key field name for Many Side
		RdbmsMetaClassAttribute mappedByAttribute = findIdAttribute(oneMetaClass);
		BuilderMetaClass manyBuilderMetaClass =  buildermetaclassregistry.getBuilderMetaClass(fk.getJoinTableName());
		RdbmsMetaClass manyMetaClass = manyBuilderMetaClass.metaClass();
		Builder<?> manyBuilder = manyBuilderMetaClass.builder();
		
										
										TypeDescription cascadeTypeTd = new TypeDescription.ForLoadedType(CascadeType.class);
									    EnumerationDescription cascadeTypeEd = new EnumerationDescription.ForLoadedEnumeration(CascadeType.ALL);
									    var av = AnnotationValue.ForDescriptionArray.of(cascadeTypeTd, new EnumerationDescription[]{cascadeTypeEd});
									    
									    ForeignKeyMetaClassRecord theRecord = new  ForeignKeyMetaClassRecord(fk, oneMetaClass,buildermetaclassregistry);
									    
										AnnotationDescription oneToManyAnnotationDescription = OneToManyAnnotationDescriptionFactory
																								.on()
																								.buildAnnotationDescription(theRecord)
																								.orElseThrow();
										
										TypeDescription setTypeDescription = new TypeDescription.ForLoadedType(java.util.Set.class );
										//Here the type definition is relative to the foreign key class (the holder of the fk attribute)
										TypeDefinition tdClazz = manyBuilder.toTypeDescription();
										TypeDescription.Generic genericTypeDef = TypeDescription.Generic
																			.Builder
																			.parameterizedType(setTypeDescription  ,tdClazz).build();
										Builder<?> oneResultantBuilder = oneBuilder
										.defineProperty( manyMetaClass.getClassName().toLowerCase() , genericTypeDef)
										.annotateField(new AnnotationDescription[] {oneToManyAnnotationDescription});
										
										BuilderMetaClass bmc = new BuilderMetaClass(oneResultantBuilder,oneMetaClass);
										buildermetaclassregistry.registryBuilderMetaClass(oneMetaClass.getTableName(), bmc);
		
	}
	
	default void processManyToManyRelations(RdbmsMetaClass joinMetaClass,
			BuilderMetaClassRegistry buildermetaclassregistry2) {
		RdbmsMetaClassForeignKey rmc = joinMetaClass.getRdbmsForeignKeys().get(0);
		RdbmsMetaClassForeignKey lmc = joinMetaClass.getRdbmsForeignKeys().get(1);
		
		BuilderMetaClass rightMc = buildermetaclassregistry2.getBuilderMetaClass(rmc.getTableName());
		BuilderMetaClass leftMc = buildermetaclassregistry2.getBuilderMetaClass(lmc.getTableName());
		ForeignKeyMetaClassRecord rmcr = new ForeignKeyMetaClassRecord(rmc, rightMc.metaClass(),buildermetaclassregistry2);
		ForeignKeyMetaClassRecord lrmc = new ForeignKeyMetaClassRecord(rmc, leftMc.metaClass(),buildermetaclassregistry2);
		String mappedby =  rightMc.metaClass().getClassName().toLowerCase();
		AnnotationDescription rad = ManyToManyAnnotationDescriptionFactory.on().buildAnnotationDescription(rmcr,mappedby).get();
		AnnotationDescription lad = ManyToManyAnnotationDescriptionFactory.on().buildAnnotationDescription(lrmc,CascadeType.ALL).get();
		AnnotationDescription jcad = JoinTableAnnotationDescriptionFactory.on().buildAnnotationDescription(joinMetaClass).get();
		
		
		TypeDescription setTypeDescription = new TypeDescription.ForLoadedType(java.util.Set.class );
		//Here the type definition is relative to the foreign key class (the holder of the fk attribute)
		TypeDescription.Generic rgenericTypeDef = TypeDescription.Generic
											.Builder
											.parameterizedType(setTypeDescription  ,leftMc.builder().toTypeDescription()).build();
		TypeDescription.Generic lgenericTypeDef = TypeDescription.Generic
				.Builder
				.parameterizedType(setTypeDescription  ,rightMc.builder().toTypeDescription()).build();
		
		Builder<?> rb = rightMc.builder()
							.defineProperty(leftMc.metaClass().getClassName().toLowerCase(), rgenericTypeDef)
							.annotateField(new AnnotationDescription[] {rad});
		
		
		Builder<?> lb = leftMc.builder()
				.defineProperty(rightMc.metaClass().getClassName().toLowerCase(), lgenericTypeDef)
				.annotateField(new AnnotationDescription[] {lad,jcad});

		
		BuilderMetaClass rn = new BuilderMetaClass(rb,rightMc.metaClass());
		BuilderMetaClass ln = new BuilderMetaClass(lb,leftMc.metaClass());
		buildermetaclassregistry2.registryBuilderMetaClass(rightMc.metaClass().getTableName(), rn);
		buildermetaclassregistry2.registryBuilderMetaClass(leftMc.metaClass().getTableName(), ln);		
	}

	
	default RdbmsMetaClassAttribute findIdAttribute(RdbmsMetaClass metaClass) {
		return metaClass
			.getMetaAttributes()
			.stream()
			.filter(att -> att.isPartOfId()).findFirst().orElseThrow();
	}


	default RdbmsMetaClassAttribute findMetaClassAttributeByColumnName(RdbmsMetaClass metaClass, String columnName) {
	return 	metaClass
		.getMetaAttributes()
		.stream()
		.filter(att -> att.getColumnName().equals(columnName))
		.findFirst().orElseThrow();
	}


	default RdbmsMetaClassForeignKey getForeignKey(RdbmsMetaClassAttribute att,
			List<RdbmsMetaClassForeignKey> rdbmsForeignKeys) {
		
		return rdbmsForeignKeys
		.stream()
		.filter(fk -> att.getColumnName().equals(fk.getJoinColumnName()))
		.findFirst().get();
	}


	default <K extends RdbmsMetaClassAttribute> AnnotationDescription[] buildAnnotations(K att) {
		return new AnnotationDescriptionFactory.AttributeAnnotationDescriptionBuilder<K>().build(att);
	}
	
	default <K extends RdbmsMetaClassAttribute> AnnotationDescription[] 
			buildRelationShipAnnotations(K att,
					RdbmsMetaClass metaClass,
					BuilderMetaClassRegistry buildermetaclassregistry,
					ProcessedForeignKeyRegistry processedForeignKeyRegistry
					) {
		return new AnnotationDescriptionFactory.ForeignAttributeAnnotationDescriptionBuilder<K>().build(att,metaClass,buildermetaclassregistry,processedForeignKeyRegistry);
	}
	
	
	default Class<?> getJavaClass(String clazz) {
		try { 
			return  PropertyTypeChanger
						.asBase()
						.getTypeForType(Class.forName(clazz));
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}



}
