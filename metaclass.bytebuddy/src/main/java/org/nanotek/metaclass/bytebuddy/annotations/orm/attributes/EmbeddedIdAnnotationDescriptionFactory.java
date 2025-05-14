package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * Produces jakarta.persistence.EmbeddedId using  single attribute type annotation.
 */
public class EmbeddedIdAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<EmbeddedId, RdbmsMetaClassAttribute> {

	private EmbeddedIdAnnotationDescriptionFactory() {
	}
	
	public static EmbeddedIdAnnotationDescriptionFactory on() {
		return new EmbeddedIdAnnotationDescriptionFactory();
	}

	@Override
	//TODO: Change here, embeddedid can be a group of attributes.
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		
		return Optional.of(ma)
				.filter(a -> a.isPartOfId())
				.map(a -> AnnotationDescription.Builder
						.ofType(EmbeddedId.class)
						.build());
		
	}
}
