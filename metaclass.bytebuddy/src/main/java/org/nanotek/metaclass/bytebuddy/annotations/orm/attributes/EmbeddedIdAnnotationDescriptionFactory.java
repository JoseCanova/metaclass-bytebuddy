package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.IdBase;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.EmbeddedId;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * Produces jakarta.persistence.EmbeddedId using  single attribute type annotation.
 */
public class EmbeddedIdAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<EmbeddedId, IdBase<?,?>> {

	private EmbeddedIdAnnotationDescriptionFactory() {
	}
	
	public static EmbeddedIdAnnotationDescriptionFactory on() {
		return new EmbeddedIdAnnotationDescriptionFactory();
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(IdBase<?,?> ma) {
		
		return Optional.of(ma)
//				.filter(a -> a.isPartOfId())
				.map(a -> AnnotationDescription.Builder
						.ofType(EmbeddedId.class)
						.build());
		
	}
}
