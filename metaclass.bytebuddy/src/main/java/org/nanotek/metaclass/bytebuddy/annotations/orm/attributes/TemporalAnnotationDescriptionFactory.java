package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationDescription.Builder;

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
		.map(a -> AnnotationDescription.Builder.ofType(Temporal.class))
		.map(a -> defineType(a , ma).build());
	}

	private Builder defineType(Builder a, RdbmsMetaClassAttribute ma) {
		var type = getType(ma);
		return a.define("value", type) ;
	}

	private TemporalType getType(RdbmsMetaClassAttribute ma) {
		return ma.getSqlType().toLowerCase().contains(DATE)?TemporalType.DATE:TemporalType.TIMESTAMP;
	}

	private Boolean isDateType(RdbmsMetaClassAttribute a) {
		return a.getSqlType().toLowerCase().contains(DATE)||a.getSqlType().toLowerCase().contains(TIMESTAMP);
	}

}
