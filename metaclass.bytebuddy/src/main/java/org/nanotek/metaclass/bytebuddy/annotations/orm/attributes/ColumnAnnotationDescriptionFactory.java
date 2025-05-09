package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;
import java.util.stream.Stream;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Column;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * Produces jakarta.persistence.Column type annotation.
 */
public class ColumnAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<Column, RdbmsMetaClassAttribute> {

	private ColumnAnnotationDescriptionFactory() {
	}

	public static ColumnAnnotationDescriptionFactory on() {
		return new ColumnAnnotationDescriptionFactory();
	}
	@Override
	public Optional< AnnotationDescription > buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		
		return 
		 Optional.of(ma)
		.map(a -> {
			AnnotationDescription.Builder adb= AnnotationDescription.Builder.ofType(Column.class)
				.define("name", a.getColumnName());
			adb = adb.define("nullable", ma.isRequired()?false:true);
			return adb;
		})
		.map(adb -> {
			return isStringType(ma)?adb.define("length",Long.valueOf(ma.getLength())):adb;
		})
		.map(adb -> {
			StringNumericPair[] 
					precisionScaleParameters = mountPrecisionScaleBigDecimalParameter(ma);
			return Stream
			.of(precisionScaleParameters)
			.map(pair -> adb.define(pair.name, pair.value))
			.reduce((a,b) -> b)
			.orElse(adb).build();
			});
	}
	
	private boolean isStringType(RdbmsMetaClassAttribute ma) {
		return ma.getClazz().equals("java.land.String");
	}

	private StringNumericPair[] mountPrecisionScaleBigDecimalParameter(RdbmsMetaClassAttribute ma) {
		StringNumericPair[] thePair =  !ma.getClazz().equals("java.math.BigDecimal") || "0".equals(ma.getLength())? 
				new StringNumericPair[0]: 
			new StringNumericPair[] {new StringNumericPair ("precision", Integer.valueOf(ma.getLength())),
					new StringNumericPair ("scale", ma.getScale())};
		return thePair;
	}

	public static record StringNumericPair(String name , Integer value) {
		public StringNumericPair(String name , Integer value) {
			this.name = name;
			this.value = value;
		}
	}
}
