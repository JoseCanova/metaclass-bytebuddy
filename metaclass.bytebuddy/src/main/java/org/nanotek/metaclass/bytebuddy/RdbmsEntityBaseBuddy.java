package org.nanotek.metaclass.bytebuddy;

import java.util.List;
import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.attributes.AttributeBaseBuilder;
import org.nanotek.metaclass.bytebuddy.attributes.PropertyTypeChanger;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class RdbmsEntityBaseBuddy 
implements EntityBaseByteBuddy {

	private RdbmsMetaClass metaClass;

	private RdbmsEntityBaseBuddy(RdbmsMetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	public static RdbmsEntityBaseBuddy instance(RdbmsMetaClass metaClass) {
		return new RdbmsEntityBaseBuddy(metaClass);
	}
	//TODO:refactor all
	public Class<?> getLoadedClassInDefaultClassLoader(){
		ByteBuddy buddy = this.generateByteBuddy() ;
		
		Builder<?> bd = this.generateBuilderWithClassName
					(buddy, metaClass);
		Holder<Builder<?>> holder = Holder.of(bd);

		Builder<?> builder =   AttributeBaseBuilder
				.on()
				.generateClassAttributes(metaClass , 
				bd);
		
			Class<?> loaded = 
					Optional.of(builder)
			.map(b -> b.make().load(getClass().getClassLoader())
					.getLoaded()) .orElseThrow() ;
			
			return loaded;
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
}
