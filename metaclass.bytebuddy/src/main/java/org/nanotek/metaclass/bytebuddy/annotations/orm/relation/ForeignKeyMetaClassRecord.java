package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.metaclass.BuilderMetaClassRegistry;

public record ForeignKeyMetaClassRecord
(RdbmsMetaClassForeignKey foreignKey,
		RdbmsMetaClass rdbmsMetaClass, 
		BuilderMetaClassRegistry builderMetaClassRegistry)  {
}
