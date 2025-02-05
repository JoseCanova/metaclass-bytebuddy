package org.nanotek.metaclass.bytebuddy.annotations.orm;

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
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClass ma) {
		return buildAnnotationDescription(MappedSuperclass.class);
	}

}
