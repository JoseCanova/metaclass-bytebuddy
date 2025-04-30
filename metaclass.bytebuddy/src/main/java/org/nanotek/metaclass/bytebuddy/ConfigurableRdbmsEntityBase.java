package org.nanotek.metaclass.bytebuddy;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.nanotek.BaseInstantiationException;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

public class ConfigurableRdbmsEntityBase {

	private String strategy;
	
	
	public ConfigurableRdbmsEntityBase(@Nullable String arg) {
		super();
		strategy = Optional.ofNullable(strategy).orElse("default");
	}
	

	public EntityBaseByteBuddy getEntityBaseByteBuddy(Supplier<RdbmsMetaClass> metaClass) {
		
		Class<?> clazz = EntityBaseBuddyRegistry.getStrategy(strategy);
		
		Optional<?> opt = newInstance(clazz, new Object[] {metaClass.get()}, RdbmsMetaClass.class);
		return opt.map(o -> EntityBaseByteBuddy.class.cast(o)).orElseThrow();
			
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
