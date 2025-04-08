package org.nanotek.metaclass;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import net.bytebuddy.dynamic.DynamicType.Builder;

public record BuilderMetaClass
	(Builder <?> builder,
			RdbmsMetaClass metaClass){
}
