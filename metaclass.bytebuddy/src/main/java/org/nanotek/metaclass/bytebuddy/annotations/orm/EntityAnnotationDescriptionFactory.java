package org.nanotek.metaclass.bytebuddy.annotations.orm;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Entity;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class EntityAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Entity, RdbmsMetaClass> {

	private EntityAnnotationDescriptionFactory() {
	}

	public static EntityAnnotationDescriptionFactory on() {
		return new EntityAnnotationDescriptionFactory();
	}

	@Override
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClass ma) {
		return buildAnnotationDescription(Entity.class);
	}
	
	
}
