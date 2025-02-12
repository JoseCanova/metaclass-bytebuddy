package org.nanotek.metaclass.bytebuddy.attributes;


import java.util.List;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.Holder;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;


//TODO: verify best is class name is the more appropriate name.
public interface AttributeBaseBuilder<T extends Builder<?> , M extends RdbmsMetaClass> {

	
	public static <T extends Builder<?> , M extends RdbmsMetaClass>  
		AttributeBaseBuilder<T,M> on(){
		return new AttributeBaseBuilder<T,M>(){};
	}
	
	
	//TODO: Fix type check 
	//TODO: verify method implementation for a functional method
	@SuppressWarnings("unchecked")
	default T generateClassAttributes(M metaClass , T builder) {
		Holder<Builder<?>> holder = Holder.of(builder);
		List<RdbmsMetaClassAttribute> atts = metaClass.getMetaAttributes();
		atts
		.stream()
		.forEach(att -> {
			System.err.println(att.getColumnName());
			AnnotationDescription[] descs = buildAnnotations(att);
							Class<?> theJavaClass = getJavaClass(att.getClazz());
							Builder<?> newBuilder = holder.get().orElseThrow()
									.defineProperty(att.getFieldName(), theJavaClass)
									.annotateField(descs);
							holder.put(newBuilder);
		});
		
		return (T)holder.get(). orElseThrow();
	}
	
	default <K extends RdbmsMetaClassAttribute> AnnotationDescription[] buildAnnotations(K att) {
		return new AnnotationDescriptionFactory.AttributeAnnotationDescriptionBuilder<K>().build(att);
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
