package org.nanotek.metaclass.bytebuddy.annotations.validation;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.NotBlank;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class NotBlankAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<NotBlank,RdbmsMetaClassAttribute> {

	private NotBlankAnnotationDescriptionFactory() {
	}

	public static NotBlankAnnotationDescriptionFactory on() {
		return new NotBlankAnnotationDescriptionFactory();
	}
	
	@Override
	public AnnotationDescription buildAnnotationDescription(
			RdbmsMetaClassAttribute ma) {
		return buildAnnotationDescription(NotBlank.class);
	}

}
