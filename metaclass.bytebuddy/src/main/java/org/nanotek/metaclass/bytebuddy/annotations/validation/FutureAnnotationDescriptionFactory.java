package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Future;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO:implement a check constraint to verify if the annotation description is 
//valid against the MetaAttribute
public class FutureAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<Future, RdbmsMetaClassAttribute> {

	public FutureAnnotationDescriptionFactory() {
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return  Optional.empty();// Optional.of(buildAnnotationDescription(Future.class));
	}

}
