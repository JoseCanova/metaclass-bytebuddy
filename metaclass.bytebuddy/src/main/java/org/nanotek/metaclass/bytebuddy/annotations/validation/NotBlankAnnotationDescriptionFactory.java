package org.nanotek.metaclass.bytebuddy.annotations.validation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class NotBlankAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<NotBlank,RdbmsMetaClassAttribute> {

	private NotBlankAnnotationDescriptionFactory() {
	}

	public static NotBlankAnnotationDescriptionFactory on() {
		return new NotBlankAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(
			RdbmsMetaClassAttribute ma) {
		return Optional
				.of(ma)
				.filter(a -> isJavaAllowedType(a))
				.filter(a -> isRequired(a))
				.map(a -> AnnotationDescription
						.Builder.ofType(NotBlank.class)
						.build());
			}

			private Boolean isRequired(RdbmsMetaClassAttribute a) {
				return a.isRequired();
			}

			private Boolean isJavaAllowedType(RdbmsMetaClassAttribute a) {
				return a.getClazz().toLowerCase().contains("string");
			}
}
