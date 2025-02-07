package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Temporal;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class TemporalAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<Temporal, RdbmsMetaClassAttribute> {

	private TemporalAnnotationDescriptionFactory() {
	}
	
	public static TemporalAnnotationDescriptionFactory on() {
		return new TemporalAnnotationDescriptionFactory();
	}

	@Override
	public Optional< AnnotationDescription >buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.of(buildAnnotationDescription(Temporal.class));
	}

}
