package org.nanotek.metaclass.bytebuddy;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class EntityBaseBuddyRegistry {
	
	private static final Map<String, Class<?>> strategies = new HashedMap<>();

    static {
        strategies.put("default", RdbmsEntityBaseBuddy.class);
//        strategies.put("custom", new CustomEntityBaseStrategy());
    }

	public EntityBaseBuddyRegistry() {
	}
	
	public static Class<?> getStrategy(String name) {
        return strategies.getOrDefault(name, RdbmsEntityBaseBuddy.class);
    }

}
