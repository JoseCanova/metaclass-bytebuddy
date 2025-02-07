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

	private static String TIMESTAMP = "timestamp";
	private static String DATE = "date";
	
	@Override
	public Optional< AnnotationDescription >buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional
		.of(ma)
		.filter(a -> isDateType(a))
		.map(a -> AnnotationDescription.Builder.ofType(Temporal.class).build());
	}

	private Boolean isDateType(RdbmsMetaClassAttribute a) {
		return a.getSqlType().toLowerCase().contains(DATE)||a.getSqlType().toLowerCase().contains(TIMESTAMP);
	}

}
