package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Id;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO:mount junit test
public class IdAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Id, RdbmsMetaClassAttribute> {

	private IdAnnotationDescriptionFactory() {
	}
	
	public static IdAnnotationDescriptionFactory on() {
		return new IdAnnotationDescriptionFactory();
	}

	@Override
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return buildAnnotationDescription(Id.class);
	}

}
