package org.nanotek.metaclass.bytebuddy;

import org.nanotek.Base;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

/**
 * Moving the TypeDefinition away from Base utility class.
 * 
 * @param <T>
 * @param <A>
 */
public interface BaseByteBuddy <T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> 
extends MetaByteBuddy<T,A>{

	
	@Override
	default  Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy, T metaclass) {
			TypeDefinition td = TypeDescription
									.Generic
									.Builder
									.parameterizedType(Base.class  , TypeDescription.Latent.class)
									.build();
			return bytebuddy.subclass(td).name(metaclass.getClassName());
		}
	
}
