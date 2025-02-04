package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType.Builder;

public interface MetaByteBuddy<T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> {
	
	default ByteBuddy generateByteBuddy() {
		return new ByteBuddy(ClassFileVersion.JAVA_V22);
	}
	
	default Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy, MetaClass<T,A> metaclass){
		String packageName = "org.nanotek.";
		metaclass.getClassName();
		return bytebuddy
				.subclass(Object.class)
				.name(metaclass.getClassName());
	}
	
	default ByteBuddy generateClassAdditionalProperties(Builder<Object> builder , MetaClass<T,A> metaClass) {
		return null;
	}
}
