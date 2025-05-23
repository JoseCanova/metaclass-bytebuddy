package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.NotEmpty;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class NotEmptyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<NotEmpty,RdbmsMetaClassAttribute> {

	private NotEmptyAnnotationDescriptionFactory() {
	}

	public static NotEmptyAnnotationDescriptionFactory on() {
		return new NotEmptyAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional< AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.empty();// Optional.of(buildAnnotationDescription(NotEmpty.class));
	}

}
