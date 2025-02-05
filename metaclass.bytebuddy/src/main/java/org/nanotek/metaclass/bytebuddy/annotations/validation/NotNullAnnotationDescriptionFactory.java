package org.nanotek.metaclass.bytebuddy.annotations.validation;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.NotNull;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class NotNullAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<NotNull,RdbmsMetaClassAttribute>{

	private NotNullAnnotationDescriptionFactory() {
	}

	public static NotNullAnnotationDescriptionFactory on() {
		return new NotNullAnnotationDescriptionFactory();
	}
	
	@Override
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return buildAnnotationDescription(NotNull.class);
	}

}
