package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Max;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class MaxAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Max, RdbmsMetaClassAttribute> {

	private MaxAnnotationDescriptionFactory() {
	}

	public static MaxAnnotationDescriptionFactory on() {
		return new MaxAnnotationDescriptionFactory();
	}
	
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.empty();
	}

}
