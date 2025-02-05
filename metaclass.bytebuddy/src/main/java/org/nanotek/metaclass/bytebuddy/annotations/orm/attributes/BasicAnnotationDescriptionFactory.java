package org.nanotek.metaclass.bytebuddy.annotations.orm.attributes;

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
	public AnnotationDescription buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return buildAnnotationDescription(Basic.class);
	}

}
