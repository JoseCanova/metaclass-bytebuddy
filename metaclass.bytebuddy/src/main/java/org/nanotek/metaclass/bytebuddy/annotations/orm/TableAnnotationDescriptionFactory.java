package org.nanotek.metaclass.bytebuddy.annotations.orm;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Table;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class TableAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Table,RdbmsMetaClass>{

	private TableAnnotationDescriptionFactory() {
	}

	public static TableAnnotationDescriptionFactory on() {
		return new TableAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional< AnnotationDescription> buildAnnotationDescription(RdbmsMetaClass ma) {
		return Optional.of(ma)
		.map(c -> AnnotationDescription.Builder.ofType(Table.class).define("name", c.getTableName()).build());
	}

}
