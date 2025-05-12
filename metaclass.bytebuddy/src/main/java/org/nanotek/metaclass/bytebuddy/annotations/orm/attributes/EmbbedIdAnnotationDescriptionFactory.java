package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * Produces jakarta.persistence.EmbeddedId type annotation.
 */
public class EmbbedIdAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<EmbeddedId, RdbmsMetaClassAttribute> {

	private EmbbedIdAnnotationDescriptionFactory() {
	}
	
	public static EmbbedIdAnnotationDescriptionFactory on() {
		return new EmbbedIdAnnotationDescriptionFactory();
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		
		return Optional.of(ma)
				.filter(a -> a.isPartOfId())
				.map(a -> AnnotationDescription.Builder
						.ofType(EmbeddedId.class)
						.build());
		
	}

}
