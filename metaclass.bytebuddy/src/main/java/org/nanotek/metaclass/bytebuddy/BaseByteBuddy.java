package org.nanotek.metaclass.bytebuddy;

import org.nanotek.Base;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;

public interface BaseByteBuddy <T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> 
extends MetaByteBuddy<T,A>{

	
	@SuppressWarnings("rawtypes")
	@Override
	default Builder<Base> generateBuilderWithClassName(ByteBuddy bytebuddy, MetaClass<T, A> metaclass) {
			String packageName = "org.nanotek.";
			metaclass.getClassName();
			return bytebuddy.subclass(Base.class).name(packageName.concat(metaclass.getClassName()));
		}
	
}
