package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Column;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO: Implement method with column annotation properties and junit support
public class ColumnAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<Column, RdbmsMetaClassAttribute> {

	private ColumnAnnotationDescriptionFactory() {
	}

	public static ColumnAnnotationDescriptionFactory on() {
		return new ColumnAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional< AnnotationDescription > buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.of(ma)
		.filter(a -> !ma.isPartOfId())
		.map(a -> 
				AnnotationDescription.Builder.ofType(Column.class)
				.define("name", a.getColumnName()).build());
	}

}
