package org.nanotek.metaclass.bytebuddy.attributes;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface PropertyTypeChanger {

	Class<?> getTypeForType(Class<?> clazz);
	
	public static BasePropertyTypeChanger asBase() {
		return new BasePropertyTypeChanger();
	}
	
	public static class BasePropertyTypeChanger implements PropertyTypeChanger{
		
		Map<Class<?> , Class<?>> classEntries = new HashMap<>();
		
		@SuppressWarnings("rawtypes")
		@Override
		public Class<?> getTypeForType(Class clazz) {
			return  Optional.ofNullable(classEntries.get(clazz))
					.orElse(clazz);
		}
		
	}
}
