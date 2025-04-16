package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.RelationType;

import jakarta.persistence.OneToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;


//TODO: implement the Annotation Description using as base the sample created in the data-service.
public class OneToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<OneToMany,ForeignKeyMetaClassRecord>{

	private OneToManyAnnotationDescriptionFactory() {
	}
	
	public static OneToManyAnnotationDescriptionFactory on() {
		return new OneToManyAnnotationDescriptionFactory();
	}

	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord theRecord) {
		
		AnnotationDescription ad = null;
		
		RelationType relationType = classifyRelationType(theRecord.foreignKey(),theRecord.rdbmsMetaClass());
		
		switch (relationType) {
			case PARENT:
				ad = mountParentAnnotation(theRecord,OneToMany.class);
				break;
			default:
				throw new RuntimeException("not a valid one to many foreign key on metamodel");
		}
		
		return Optional.of(ad);
	}



}
