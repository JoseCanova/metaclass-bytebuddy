package org.nanotek.metaclass.bytebuddy.annotations.orm.relation.classification;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;

public interface RelationTypeClassifier {

	
	default RelationType classifyRelationType(RdbmsMetaClassForeignKey foreignKey, RdbmsMetaClass rdbmsMetaClass) {
		return rdbmsMetaClass
		.getRdbmsForeignKeys()
		.stream()
		.filter(fk -> fk.equals(foreignKey))
		.findFirst()
		.map(fk -> RelationType.CHILD)
		.orElse(RelationType.PARENT);
	}
	
}
