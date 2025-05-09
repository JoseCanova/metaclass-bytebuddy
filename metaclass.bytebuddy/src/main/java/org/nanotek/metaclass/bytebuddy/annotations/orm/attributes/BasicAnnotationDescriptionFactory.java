package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Basic;
import net.bytebuddy.description.annotation.AnnotationDescription;

/**
 * 
 * Produces jakarta.persistence.Basic type annotation.
 * 
 */
public class BasicAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Basic,RdbmsMetaClassAttribute>{

	private BasicAnnotationDescriptionFactory() {
	}

	public BasicAnnotationDescriptionFactory on() {
		return new BasicAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.empty();//Optional.of(buildAnnotationDescription(Basic.class));
	}

}
