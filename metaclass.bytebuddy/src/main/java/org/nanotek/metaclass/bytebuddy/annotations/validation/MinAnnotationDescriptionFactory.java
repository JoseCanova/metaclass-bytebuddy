package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Min;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class MinAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Min, RdbmsMetaClassAttribute> {

	private MinAnnotationDescriptionFactory() {
	}

	public static MinAnnotationDescriptionFactory on() {
		return new MinAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional< AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.empty();
	}

}
