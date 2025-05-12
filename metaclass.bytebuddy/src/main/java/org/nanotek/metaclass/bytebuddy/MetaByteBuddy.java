package org.nanotek.metaclass.bytebuddy;

import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.MetaClassAttribute;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType.Builder;


/**
*
*Overview:
*The MetaByteBuddy interface is designed to work with the Byte Buddy library for generating dynamic types. 
*It provides default methods for creating a ByteBuddy instance, 
*generating a builder for class construction, 
*and adding additional properties to the generated class. 
 * @param <T>  T is a subclass of MetaClass.
 * @param <A> representing the type of metadata attributes associated with the class.
 */

public interface MetaByteBuddy
<T extends MetaClass<T,A>,A extends MetaClassAttribute<?>> {
	
	default ByteBuddy generateByteBuddy() {
		return new ByteBuddy(ClassFileVersion.JAVA_V22);
	}
	
	default Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy, T metaclass){
		metaclass.getClassName();
		return bytebuddy
				.subclass(Object.class)
				.name(metaclass.getClassName());
	}
}
