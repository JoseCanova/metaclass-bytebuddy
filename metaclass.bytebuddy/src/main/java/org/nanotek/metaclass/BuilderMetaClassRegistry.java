package org.nanotek.metaclass;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Provides a collection to hold byte-buddy Builders,
 * This is necessary with the advent of RelationShips 
 * on model configuration.
 * Builders (@see )hold of "Class Configuration" 
 * this allows deferring the class creation until all 
 * metamodel is properly configured.
 * Since the builder is "mutable class" 
 * once a BuilderMetaClass is recovered from the 
 * registry it is also removed from it.
 * 
 */

public class BuilderMetaClassRegistry {

	Map<String,BuilderMetaClass> registry;
	
	public BuilderMetaClassRegistry() {
		postConstruct();
	}

	private void postConstruct() {
		registry = new HashMap<>();
	}
	
	public BuilderMetaClass 
			registryBuilderMetaClass(String key , 
					BuilderMetaClass builderMetaClass) {
		return registry.put(key, builderMetaClass);
	}
	
	public BuilderMetaClass getBuilderMetaClass(String key) {
		return Optional.ofNullable(registry.get(key)).orElseThrow();	
	}

}
