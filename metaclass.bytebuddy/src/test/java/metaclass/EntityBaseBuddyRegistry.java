package metaclass;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.nanotek.metaclass.bytebuddy.EntityBaseByteBuddy;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

public class EntityBaseBuddyRegistry<T extends EntityBaseByteBuddy> {
	
	private final Map<String, Class<?>> strategies = new HashedMap<>();


	public EntityBaseBuddyRegistry() {
		strategies.put("default", RdbmsEntityBaseBuddy.class);
	}
	
	public Class<T> getStrategy(String name) {
        return (Class<T>)strategies.getOrDefault(name, RdbmsEntityBaseBuddy.class);
    }

}
