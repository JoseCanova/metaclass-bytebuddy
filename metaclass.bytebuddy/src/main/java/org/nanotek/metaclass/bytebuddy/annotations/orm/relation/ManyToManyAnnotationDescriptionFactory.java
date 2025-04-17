package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.ManyToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class ManyToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<ManyToMany, ForeignKeyMetaClassRecord> {

	public ManyToManyAnnotationDescriptionFactory() {
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord ma) {
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(ManyToMany.class).build();
		return Optional.of(ad);
	}

}
