package org.nanotek.metaclass.bytebuddy.annotations.validation;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Email;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class EmailAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Email, RdbmsMetaClassAttribute> {

	public EmailAnnotationDescriptionFactory() {
	}

	public static EmailAnnotationDescriptionFactory on() {
		return new EmailAnnotationDescriptionFactory();
	}

	@Override
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return buildAnnotationDescription(Email.class);
	}
	
}
