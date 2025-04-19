package org.nanotek.metaclass.bytebuddy.attributes;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@FunctionalInterface
public interface PropertyTypeChanger {

	Class<?> getTypeForType(Class<?> clazz);
	
	public static BasePropertyTypeChanger asBase() {
		return new BasePropertyTypeChanger();
	}
	
	//TODO: provide a converter solution for timestamp - datetime (java.time) attributes.
	public static class BasePropertyTypeChanger implements PropertyTypeChanger{
		
		Map<String, Class<?>> classEntries = new TreeMap<>();
		
		public BasePropertyTypeChanger (){
			classEntries.put(java.sql.Date.class.getName(),java.util.Date.class);
			classEntries.put(java.sql.Timestamp.class.getName(),java.util.Date.class);
		}
		
		@Override
		public Class<?> getTypeForType(Class clazz) {
			return  Optional.ofNullable(classEntries.get(clazz.getName()))
					.orElse(clazz);
		}
		
	}
}
