package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.BuilderMetaClass;
import org.nanotek.metaclass.BuilderMetaClassRegistry;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.classification.RelationType;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.classification.RelationTypeClassifier;

import jakarta.persistence.OneToOne;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class OneToOneAnnotationDescrptionFactory
implements AnnotationDescriptionFactory<OneToOne,ForeignKeyMetaClassRecord>,
RelationTypeClassifier{

	private OneToOneAnnotationDescrptionFactory() {
	}
	
	public static OneToOneAnnotationDescrptionFactory on() {
		return new OneToOneAnnotationDescrptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord theRecord) {
		
		AnnotationDescription ad = null;
		
		RelationType relationType = classifyRelationType(theRecord.foreignKey(),theRecord.rdbmsMetaClass());
		
		switch (relationType) {
			case PARENT:
				ad = mountParentAnnotation(theRecord,OneToOne.class);
				break;
			case CHILD:
				ad = mountOneToOneChildAnnotation();
				break;
			default:
				throw new RuntimeException("not a valid foreign key on metamodel");
		}
		
		return Optional.of(ad);
	}
	
	private AnnotationDescription mountOneToOneChildAnnotation() {
		return AnnotationDescription.Builder.ofType(OneToOne.class).build();
		
	}

	//NOT USED BY NOW
	private BuilderMetaClass findChildMetaClass(ForeignKeyMetaClassRecord theRecord) {
		return findMetaClass(theRecord.foreignKey().getJoinTableName(), theRecord.builderMetaClassRegistry());
	}
	
	//NOT USED BY NOW
	private BuilderMetaClass findMetaClass(String tableName, BuilderMetaClassRegistry builderMetaClassRegistry) {
		return builderMetaClassRegistry.getBuilderMetaClass(tableName);
	}

}
