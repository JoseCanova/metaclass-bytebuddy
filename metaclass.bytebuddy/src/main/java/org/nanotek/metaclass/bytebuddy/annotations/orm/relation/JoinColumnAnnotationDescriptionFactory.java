package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.JoinColumn;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO: implement the Annotation Description using as base the sample created in the data-service.
public class JoinColumnAnnotationDescriptionFactory<T extends RdbmsMetaClassForeignKey> 
implements AnnotationDescriptionFactory<JoinColumn,T>{


	private JoinColumnAnnotationDescriptionFactory() {
	}
 
	public static <T extends RdbmsMetaClassForeignKey>JoinColumnAnnotationDescriptionFactory<T> on() {
		return new JoinColumnAnnotationDescriptionFactory<>();
	}
	
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(T ma) {
		return Optional.of(AnnotationDescription.Builder
				.ofType(JoinColumn.class)
				.define("name", ma.getJoinColumnName())
				.define("referencedColumnName",ma.getColumnName())
				.build());
	}

}
