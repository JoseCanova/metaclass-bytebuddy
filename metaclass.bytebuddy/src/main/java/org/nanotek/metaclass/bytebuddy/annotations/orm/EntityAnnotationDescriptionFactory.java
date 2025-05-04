package org.nanotek.metaclass.bytebuddy.annotations.orm;

import java.util.Optional;

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
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClass ma) {
		
		return Optional.of(ma)
		.map(c -> AnnotationDescription.
						Builder.ofType(Entity.class)
						.define("name", c.getClassName()).build());
	}
	
	
}
