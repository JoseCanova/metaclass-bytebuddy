package org.nanotek.metaclass.bytebuddy;

import java.io.Serializable;

import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;

/**
 * Define a type based upon in the @see MetaClass className.
 * @param <T>
 * @param <A>
 */
public interface BaseByteBuddy <T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> 
extends MetaByteBuddy<T,A>{

	
	@Override
	default  Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy, T metaclass) {
			return bytebuddy.subclass(Object.class)
					.implement(Serializable.class)
					.name(metaclass.getClassName());
		}
	
}
