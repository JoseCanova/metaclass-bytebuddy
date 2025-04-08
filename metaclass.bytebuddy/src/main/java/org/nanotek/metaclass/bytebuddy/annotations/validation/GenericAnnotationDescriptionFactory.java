package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.lang.annotation.Annotation;
import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import net.bytebuddy.description.annotation.AnnotationDescription;

public class GenericAnnotationDescriptionFactory <T extends Annotation , K>
implements AnnotationDescriptionFactory<T, K> {

	public GenericAnnotationDescriptionFactory() {
	}

	
	
	public Optional<AnnotationDescription> buildAnnotationDescription(K ma) {
		throw new RuntimeException("not for use");
	}

}
