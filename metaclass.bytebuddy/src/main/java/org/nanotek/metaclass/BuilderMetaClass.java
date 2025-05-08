package org.nanotek.metaclass;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import net.bytebuddy.dynamic.DynamicType.Builder;

/**
 * Record that holds a ByteBuddy instance and the metaclass representation
 * of a RdbmsTable, used during the process of class definition.
 */
public record BuilderMetaClass
	(Builder <?> builder,
			RdbmsMetaClass metaClass){
}
