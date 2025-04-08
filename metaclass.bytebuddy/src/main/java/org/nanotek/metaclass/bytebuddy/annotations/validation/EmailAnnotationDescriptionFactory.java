package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

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
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.ofNullable(ma).filter(att->att.getColumnName().toLowerCase().contains("email")).map(att ->buildAnnotationDescription(Email.class));
	}
	
}
