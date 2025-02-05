package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.lang.annotation.Annotation;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import net.bytebuddy.description.annotation.AnnotationDescription;

public class GenericAnnotationDescriptionFactory <T extends Annotation , K>
implements AnnotationDescriptionFactory<T, K> {

	public GenericAnnotationDescriptionFactory() {
	}

	
	
	@Override
	public AnnotationDescription buildAnnotationDescription(K ma) {
		throw new RuntimeException("not for use");
	}

}
