package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;

public record ForeignKeyMetaClassRecord
(Optional<RdbmsMetaClassForeignKey>foreignKey,RdbmsMetaClass rdbmsMetaClass) {
}
