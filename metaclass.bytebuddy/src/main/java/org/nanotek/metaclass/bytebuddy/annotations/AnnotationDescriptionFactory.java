package org.nanotek.metaclass.bytebuddy.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.ColumnAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.IdAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.TemporalAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.EmailAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MaxAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MinAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotBlankAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotEmptyAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotNullAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.SizeAnnotationDescriptionFactory;

import net.bytebuddy.description.annotation.AnnotationDescription;

public interface AnnotationDescriptionFactory<T extends Annotation , K> {

	
	default AnnotationDescription buildAnnotationDescription(Class<T> annotationType) {
		return AnnotationDescription.Builder.ofType(annotationType).build();
	}
	
	Optional<AnnotationDescription> buildAnnotationDescription(K  ma);
	
	public static class AttributeAnnotationDescriptionBuilder<K extends RdbmsMetaClassAttribute> {
		
		public AnnotationDescription[] build(K k) {
			var annotations = new ArrayList<AnnotationDescription>();
			
			EmailAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent (a -> annotations.add(a));
			MaxAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a->annotations.add(a));
			MinAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			SizeAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			NotBlankAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a ->annotations.add(a));
			NotNullAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			NotEmptyAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			ColumnAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			IdAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			TemporalAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			return annotations.toArray(new AnnotationDescription[annotations.size()]);
		}
		
	}
}
