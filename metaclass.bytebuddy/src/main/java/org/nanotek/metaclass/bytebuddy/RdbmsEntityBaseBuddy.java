package org.nanotek.metaclass.bytebuddy;

import java.io.Serializable;
import java.util.Optional;

import org.nanotek.EntityPathConfigurableClassLoader;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.EntityAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.TableAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.attributes.AttributeBaseBuilder;
import org.nanotek.metaclass.bytebuddy.attributes.PropertyTypeChanger;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
/**
 * 
 * A container class for the bytebuddy builder  and the methods
 * to construct a concrete implementation of the class.
 * 
 */
public class RdbmsEntityBaseBuddy 
implements EntityBaseByteBuddy {

	protected RdbmsMetaClass metaClass;
	protected byte[] bytes;
	
	protected Builder<?> internalStatebuilder;
	
	
	public RdbmsEntityBaseBuddy(RdbmsMetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	public static RdbmsEntityBaseBuddy instance(RdbmsMetaClass metaClass) {
		return new RdbmsEntityBaseBuddy(metaClass);
	}
	
	/**
	 * Utility method used to create a Class instance using the default classloader.
	 */
	public Class<?> getLoadedClassInDefaultClassLoader(){
			return getLoadedClass(getClass().getClassLoader());
	}
	
	/**
	 * @param bytebuddy ByteBuddy instance that is the base class for the creation of DynamicType.Builder.
	 * @param metaclass RdbmsMetaClass instance that holds the Parameters for the creation of the TypeDescription.
	 * @param classLoader an EntityPathConfigurableClassLoader instance that is used internally to construct the concrete class.
	 * @return Builder<?> of the TypeDescription instance created by the metaclass.
	 */
	public Builder<?> initializeInternalStatebuilder  (ByteBuddy bytebuddy,
														RdbmsMetaClass metaclass,
														EntityPathConfigurableClassLoader classLoader){
		
		TypeDescription baseTd =   TypeDescription.ForLoadedType.of(Serializable.class);
		String thePackage = classLoader.getEntityPath().replaceAll("[/]", ".").concat(".");
		internalStatebuilder = bytebuddy
								.subclass(baseTd)
								.name(thePackage.concat(metaclass.getClassName()))
								.annotateType(EntityAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass).get())
								.annotateType(TableAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass).get());
	
		return internalStatebuilder;
	}


	//TODO: review methods for removal on future.
	public Class<?> getLoadedClass(ClassLoader classLoader){
		
		ByteBuddy buddy = this.generateByteBuddy() ;
		Builder<?> bd = this.generateBuilderWithClassName
										(buddy, metaClass);
		Builder<?> builder =   AttributeBaseBuilder
									.on()
									.generateClassAttributes(metaClass , 
									bd,false);
		
			Unloaded<?> loaded = 
					Optional.of(builder)
			.map(b -> b.make()) .orElseThrow() ;
			
			this.bytes = loaded.getBytes();
			return loaded.load(classLoader,
								ClassLoadingStrategy.Default.INJECTION)
								.getLoaded();
	}
	
	//TODO: review methods for removal on future.
	public Unloaded<?> getBytesForClassLoader(){
		ByteBuddy buddy = this.generateByteBuddy() ;
		
		Builder<?> bd = this.generateBuilderWithClassName (buddy, metaClass);
		
		Builder<?> builder =   AttributeBaseBuilder
				.on()
				.generateClassAttributes(metaClass , 
				bd,false);
		
			Unloaded<?> unloaded = 
					Optional.of(builder)
			.map(b -> b.make()) .orElseThrow() ;
			
			this.bytes = unloaded.getBytes();
			return unloaded;
	}
	
	public RdbmsMetaClass getMetaClass() {
		return this.metaClass;
	}
	
	//TODO: review methods for removal on future.
	public <K extends RdbmsMetaClassAttribute> AnnotationDescription[] buildAnnotations(K att) {
		return new AnnotationDescriptionFactory.AttributeAnnotationDescriptionBuilder<K>().build(att,false);
	}
	
	public Class<?> getJavaClass(String clazz) {
		try { 
			return  PropertyTypeChanger
						.asBase()
						.getTypeForType(Class.forName(clazz));
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public byte[] getBytes() {
		return bytes;
	}
}
