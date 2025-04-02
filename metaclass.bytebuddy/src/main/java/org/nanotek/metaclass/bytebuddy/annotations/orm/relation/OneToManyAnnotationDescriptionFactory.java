package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.OneToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;


//TODO: implement the Annotation Description using as base the sample created in the data-service.
public class OneToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<OneToMany,RdbmsMetaClassAttribute>{

	public OneToManyAnnotationDescriptionFactory() {
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClassAttribute ma) {
		return Optional.empty();
	}

}
