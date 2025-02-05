package org.nanotek.metaclass.bytebuddy.annotations.orm;

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
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClass ma) {
		return buildAnnotationDescription(Table.class);
	}

}
