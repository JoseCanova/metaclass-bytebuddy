package org.nanotek.metaclass.bytebuddy;

import java.util.List;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

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

	public Class<?> getLoadedClassInDeaultClassLoader(){
		Builder<?> builder =  this.generateBuilderWithClassName
				(this.generateByteBuddy() , metaClass);

			Holder<Builder<?>> holder = Holder.of(builder);
			List<RdbmsMetaClassAttribute> atts = metaClass.getMetaAttributes();
			atts
			.stream()
			.forEach(att -> {
				AnnotationDescription[] descs = buildAnnotations(att);
								Class<?> theJavaClass = getJavaClass(att.getClazz());
								Builder<?> theBuilder = holder.get().orElseThrow();
								holder.put(theBuilder
										.defineProperty(att.getFieldName(), theJavaClass)
										.annotateType(descs));
			});
			Class<?> loaded = 
			holder.get()
			.map(b -> b.make().load(getClass().getClassLoader())
					.getLoaded()) .orElseThrow() ;
			
			return loaded;
	}
	
	@SuppressWarnings("rawtypes")
	AnnotationDescription[] buildAnnotations(RdbmsMetaClassAttribute att) {
		return new AnnotationDescriptionFactory.AttributeAnnotationDescriptionBuilder().build(att);
	}
	
	private Class<?> getJavaClass(String clazz) {
		try { 
			return Class.forName(clazz);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public RdbmsMetaClass getMetaClass() {
		return this.metaClass;
	}
	
}
