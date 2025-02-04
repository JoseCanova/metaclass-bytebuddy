package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType.Builder;


/**

Overview:
The MetaByteBuddy interface is designed to work with the Byte Buddy library for generating dynamic types. 
It provides default methods for creating a ByteBuddy instance, 
generating a builder for class construction, 
and adding additional properties to the generated class. 
The interface is generic, 
using type parameters to work with subclasses of MetaClass and MetaClassAttribute.

Key Abstractions:
Generics:

The interface uses two generic type parameters, T and A.

T extends MetaClass<T, A>, ensuring that T is a subclass of MetaClass.

A extends MetaClassAttribute<?>, representing the type of metadata attributes associated with the class.
 * @param <T>
 * @param <A>
 */

public interface MetaByteBuddy<T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> {
	
	default ByteBuddy generateByteBuddy() {
		return new ByteBuddy(ClassFileVersion.JAVA_V22);
	}
	
	default Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy, T metaclass){
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
