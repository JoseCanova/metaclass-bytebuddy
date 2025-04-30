package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public class ConfigurableRdbmsEntityBase {

	private String strategy;
	
	
	public ConfigurableRdbmsEntityBase(String arg) {
		super();
		strategy = arg;
	}

	public EntityBaseByteBuddy getEntityBaseByteBuddy(RdbmsMetaClass metaClass) {
		switch (strategy) {
			default:
				return new RdbmsEntityBaseBuddy(metaClass);
		}
			
	}

}
