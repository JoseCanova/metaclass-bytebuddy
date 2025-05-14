package org.nanotek.metaclass.bytebuddy.annotations.orm;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Entity;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class EmbeddedAnnotationDescriptionFactory
implements AnnotationDescriptionFactory<Entity, RdbmsMetaClass> {

	
	private EmbeddedAnnotationDescriptionFactory() {
	}
	
	public static EmbeddedAnnotationDescriptionFactory on() {
		return new EmbeddedAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription() {
		return  Optional.of(AnnotationDescription.Builder.ofType(Entity.class).build());
	}
	
}
