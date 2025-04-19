package org.nanotek.metaclass;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Provides a collection to hold byte-buddy Builders,
 * This is necessary with the advent of RelationShips 
 * on model configuration.
 * Builders (@see )hold of "Class Configuration" 
 * this allows deferring the class creation until all 
 * metamodel is properly configured.
 * Since the builder is "mutable class" 
 * once a BuilderMetaClass is recovered from the 
 * registry it is also must be removed  from it; if it`s being used(which means being mutated).
 * 
 */

public class BuilderMetaClassRegistry {

	Map<String,BuilderMetaClass> registry;
	
	public BuilderMetaClassRegistry() {
		postConstruct();
	}

	private void postConstruct() {
		registry = new TreeMap<String,BuilderMetaClass>();
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
