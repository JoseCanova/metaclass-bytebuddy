package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.classification.RelationType;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.classification.RelationTypeClassifier;

import jakarta.persistence.OneToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;

//TODO: Provide a unit testing for parent and default over classification test
public class OneToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<OneToMany,ForeignKeyMetaClassRecord>,
RelationTypeClassifier{

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
