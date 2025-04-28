package org.nanotek;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.nanotek.Base;
//TODO: change the registry implementation to remove inherit coupling of registered classes from Base Interface.
public class MetaClassRegistry<T> {
	
	Map<UUID , Class<T>> classRegistry;
	
	Map<MultiKey<UUID> , Class<?>> repositoryRegistry;
	
	public MetaClassRegistry() {
		postConstruct();
	}
	
	private void postConstruct() {
		classRegistry = new DualHashBidiMap<UUID , Class<T>> ();
		repositoryRegistry = new DualHashBidiMap<MultiKey<UUID> , Class<?>> ();
	}

	public UUID registryEntityClass(Class<T> entityClass) {

		UUID uuid = Base.withUUID(entityClass);
		Optional.
		ofNullable(classRegistry.get(uuid))
		.orElse(classRegistry.put(uuid, entityClass));
		return uuid;
		
	}

	public Class<?> get(UUID uuid){
		throw new RuntimeException("Not yet implemented");
	}
	
	public Class<?> getEntityClass(UUID uuid){
		return Optional.ofNullable(classRegistry.get(uuid)).orElseThrow(EntityNotFoundException::new);
	}
	
	public Class<?> getRepositoryClass(UUID uuid) {
		return repositoryRegistry
					.keySet()
					.stream()
					.filter(mk -> mk.getKey(0).equals(uuid) || mk.getKey(1).equals(uuid))
					.limit(1)
					.map(mk -> repositoryRegistry.get(mk))
					.findFirst().orElseThrow(RepositoryNotFoundException::new);
	}
	
	public List<Class<?>> getEntityClasses() {
		return classRegistry
					.values()
					.stream()
					.collect(Collectors.toList());
	}
	
	public List<Class<?>> getRepositoryClasses(){
		return repositoryRegistry.values().stream().collect(Collectors.toList());
	}
	
	public MultiKey<UUID> registryRepositoryClass(Class<?> entityClass , Class<?> repositoryClass){
		UUID entityUUID = Base.withUUID(entityClass);
		return Optional
			.ofNullable(classRegistry.get(entityUUID))
			.map(clazz -> {
				var repositoryUUID = Base.withUUID(repositoryClass);
				var key = new MultiKey<UUID>(entityUUID , repositoryUUID);
				repositoryRegistry.put(key , repositoryClass);
				return key;
			})
			.orElseThrow(EntityNotFoundException::new);
	}
	
	public static class EntityNotFoundException extends RuntimeException{
		public EntityNotFoundException() {
			super ("Entity Class Not in Registry");
		}
	}

	public static class RepositoryNotFoundException extends RuntimeException{
		public RepositoryNotFoundException() {
			super ("Repository Class Not in Registry");
		}
	}



}
