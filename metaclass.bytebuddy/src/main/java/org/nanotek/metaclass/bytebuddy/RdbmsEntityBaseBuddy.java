package org.nanotek.metaclass.bytebuddy;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.attributes.AttributeBaseBuilder;
import org.nanotek.metaclass.bytebuddy.attributes.PropertyTypeChanger;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.DynamicType.Unloaded;

public class RdbmsEntityBaseBuddy 
implements EntityBaseByteBuddy {

	private RdbmsMetaClass metaClass;
	private byte[] bytes;
	
	
	
	private RdbmsEntityBaseBuddy(RdbmsMetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	public static RdbmsEntityBaseBuddy instance(RdbmsMetaClass metaClass) {
		return new RdbmsEntityBaseBuddy(metaClass);
	}
	
	public Class<?> getLoadedClassInDefaultClassLoader(){
			return getLoadedClassInDefaultClassLoader(getClass().getClassLoader());
	}
	
	public Class<?> getLoadedClassInDefaultClassLoader(ClassLoader classLoader){
		ByteBuddy buddy = this.generateByteBuddy() ;
		
		Builder<?> bd = this.generateBuilderWithClassName
					(buddy, metaClass);
		Builder<?> builder =   AttributeBaseBuilder
				.on()
				.generateClassAttributes(metaClass , 
				bd);
		
			Unloaded<?> loaded = 
					Optional.of(builder)
			.map(b -> b.make()) .orElseThrow() ;
			
			this.bytes = loaded.getBytes();
			
			return loaded.load(classLoader)
					.getLoaded();
	}
	
	public RdbmsMetaClass getMetaClass() {
		return this.metaClass;
	}
	
	public <K extends RdbmsMetaClassAttribute> AnnotationDescription[] buildAnnotations(K att) {
		return new AnnotationDescriptionFactory.AttributeAnnotationDescriptionBuilder<K>().build(att);
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
