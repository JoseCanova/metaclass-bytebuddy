package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.ManyToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class ManyToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<ManyToMany, ForeignKeyMetaClassRecord> {

	private ManyToManyAnnotationDescriptionFactory() {
	}
	
	public static ManyToManyAnnotationDescriptionFactory on() {
		return new ManyToManyAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord ma) {
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(ManyToMany.class).build();
		return Optional.of(ad);
	}

	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord ma,String mappedBy) {
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(ManyToMany.class).define("mappedBy", mappedBy).build();
		return Optional.of(ad);
	}

	
}
