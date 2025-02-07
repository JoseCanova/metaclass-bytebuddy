package org.nanotek.metaclass.bytebuddy.annotations;

import java.lang.annotation.Annotation;
import java.util.Optional;

import net.bytebuddy.description.annotation.AnnotationDescription;

public interface AnnotationDescriptionFactory<T extends Annotation , K> {

	
	default AnnotationDescription buildAnnotationDescription(Class<T> annotationType) {
		return AnnotationDescription.Builder.ofType(annotationType).build();
	}
	
	Optional<AnnotationDescription> buildAnnotationDescription(K  ma);
}
