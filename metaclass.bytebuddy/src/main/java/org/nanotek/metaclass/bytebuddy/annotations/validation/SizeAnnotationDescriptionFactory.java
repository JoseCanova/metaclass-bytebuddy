package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Size;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class SizeAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<Size,RdbmsMetaClassAttribute>{

	private SizeAnnotationDescriptionFactory() {
	}

	public static SizeAnnotationDescriptionFactory on() {
		return new SizeAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional< AnnotationDescription> buildAnnotationDescription
		(RdbmsMetaClassAttribute  ma) {
		String lenStr = ma.getLength();
		Integer length = Integer.valueOf(lenStr);
		return 
				Optional.of(
					AnnotationDescription.Builder.ofType(Size.class).define("max", length) .build());
	}

}
