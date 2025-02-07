package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Basic;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO: implement logic that based on mattribute fill the Basic annotation definition
public class BasicAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Basic,RdbmsMetaClassAttribute>{

	private BasicAnnotationDescriptionFactory() {
	}

	public BasicAnnotationDescriptionFactory on() {
		return new BasicAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.of(buildAnnotationDescription(Basic.class));
	}

}
