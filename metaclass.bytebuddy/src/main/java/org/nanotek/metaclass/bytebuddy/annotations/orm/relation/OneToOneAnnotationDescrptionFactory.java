package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.metaclass.BuilderMetaClass;
import org.nanotek.metaclass.BuilderMetaClassRegistry;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.OneToOne;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class OneToOneAnnotationDescrptionFactory
implements AnnotationDescriptionFactory<OneToOne,ForeignKeyMetaClassRecord>{

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
				ad = mountOneToOneParentAnnotation(theRecord);
				break;
			case CHILD:
				ad = mountOneToOneChildAnnotation();
				break;
			default:
				throw new RuntimeException("not a valid foreign key on metamodel");
		}
		
		return Optional.of(ad);
	}
	
	private AnnotationDescription mountOneToOneParentAnnotation(ForeignKeyMetaClassRecord theRecord) {
	    return Optional.of(theRecord
		.foreignKey())
		.map(fk -> findIdAttribute(fk,theRecord.rdbmsMetaClass()))
		.map(att -> {
			return AnnotationDescription
					.Builder.ofType(OneToOne.class)
					.define("mappedBy", att.getFieldName()).build();
		}).orElseThrow();
	}

	private AnnotationDescription mountOneToOneChildAnnotation() {
		return AnnotationDescription.Builder.ofType(OneToOne.class).build();
		
	}

	//NOT USED BY NOW
	private BuilderMetaClass findChildMetaClass(ForeignKeyMetaClassRecord theRecord) {
		return findMetaClass(theRecord.foreignKey().getJoinTableName(), theRecord.builderMetaClassRegistry());
	}
	

	private BuilderMetaClass findMetaClass(String tableName, BuilderMetaClassRegistry builderMetaClassRegistry) {
		return builderMetaClassRegistry.getBuilderMetaClass(tableName);
	}

	private RelationType classifyRelationType(RdbmsMetaClassForeignKey foreignKey, RdbmsMetaClass rdbmsMetaClass) {
		return rdbmsMetaClass
		.getRdbmsForeignKeys()
		.stream()
		.filter(fk -> fk.equals(foreignKey))
		.findFirst()
		.map(fk -> RelationType.CHILD)
		.orElse(RelationType.PARENT);
	}

	public RdbmsMetaClassAttribute findIdAttribute(RdbmsMetaClassForeignKey fk , RdbmsMetaClass metaClass) {
		return metaClass
			.getMetaAttributes()
			.stream()
			.filter(att -> att.getColumnName().equals(fk.getColumnName())).findFirst().orElseThrow();
	}

	enum RelationType {
		PARENT,
		CHILD;
	}

}
