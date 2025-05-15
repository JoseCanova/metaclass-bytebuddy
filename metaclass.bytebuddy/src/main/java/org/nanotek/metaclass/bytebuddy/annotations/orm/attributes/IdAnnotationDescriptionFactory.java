package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Id;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * Produces jakarta.persistence.Id type annotation.
 */
public class IdAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Id, RdbmsMetaClassAttribute> {

	private IdAnnotationDescriptionFactory() {
	}
	
	public static IdAnnotationDescriptionFactory on() {
		return new IdAnnotationDescriptionFactory();
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.of(ma)
				.filter(a -> a.isPartOfId())
				.map(a -> AnnotationDescription.Builder.ofType(Id.class).build());
	}

}
