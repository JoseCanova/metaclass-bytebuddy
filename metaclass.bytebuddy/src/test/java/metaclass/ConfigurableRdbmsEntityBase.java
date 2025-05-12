package metaclass;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.nanotek.BaseInstantiationException;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.EntityBaseByteBuddy;

/**
 * Simple delegate class that will be used to test the
 * class implementations of EntityBaseByteBuddy if necessary. 
 */
public class ConfigurableRdbmsEntityBase {

	private String strategy;
	
	
	public ConfigurableRdbmsEntityBase(@Nullable String arg) {
		super();
		strategy = Optional.ofNullable(strategy).orElse("default");
	}
	

	public <T extends EntityBaseByteBuddy> T getEntityBaseByteBuddy(Supplier<RdbmsMetaClass> metaClass) {
		
		Class<T> clazz = new EntityBaseBuddyRegistry<T>().getStrategy(strategy);
		
		Optional<?> opt = newInstance(clazz, new Object[] {metaClass.get()}, RdbmsMetaClass.class);
		return (T) opt.get();
			
	}
	
	private Optional<?> newInstance(Class<?> clazz , Object[] args , Class<?>... classArgs  ) throws BaseInstantiationException { 
		try {
			return Optional.of(clazz.getDeclaredConstructor(classArgs).newInstance(args));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new BaseInstantiationException(e);
		}
	}

}
