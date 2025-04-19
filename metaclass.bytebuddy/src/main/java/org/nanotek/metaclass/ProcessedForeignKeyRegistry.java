package org.nanotek.metaclass;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;

public class ProcessedForeignKeyRegistry {

	Map<RdbmsMetaClassForeignKey,RdbmsMetaClass> registry;
	
	public ProcessedForeignKeyRegistry() {
		postConstruct();
	}

	private void postConstruct(){
		registry = new HashedMap<>();
	}
	
	public RdbmsMetaClassForeignKey registryForeignKeyMetaClass
	  (RdbmsMetaClassForeignKey fk , RdbmsMetaClass mc ) {
		registry.put(fk, mc);
		return fk;
	}
	
	public Set<RdbmsMetaClassForeignKey> getProcessedForeignKeys() {
		return registry.keySet();
	}
	
	//TODO: 
 	public RdbmsMetaClass getMetaClassForeignKey(RdbmsMetaClassForeignKey key) {
		return Optional
				.ofNullable(registry.get(key))
				.orElseThrow();
	}
}
