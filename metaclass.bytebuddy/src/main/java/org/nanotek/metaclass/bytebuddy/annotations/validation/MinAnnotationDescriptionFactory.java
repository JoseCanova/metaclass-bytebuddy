package org.nanotek.metaclass.bytebuddy.annotations.validation;

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
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return null;
	}

}
