package org.nanotek.metaclass.bytebuddy.annotations.orm;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.MappedSuperclass;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class MappedSuperClassAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<MappedSuperclass, RdbmsMetaClass> {

	private MappedSuperClassAnnotationDescriptionFactory() {
	}
	
	public static MappedSuperClassAnnotationDescriptionFactory on() {
		return new MappedSuperClassAnnotationDescriptionFactory();
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClass ma) {
		return Optional.of(buildAnnotationDescription(MappedSuperclass.class));
	}

}
