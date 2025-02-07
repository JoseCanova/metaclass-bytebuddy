package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

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
	public Optional<AnnotationDescription> buildAnnotationDescription(
			RdbmsMetaClassAttribute ma) {
		return  Optional.of(buildAnnotationDescription(NotBlank.class));
	}

}
