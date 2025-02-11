package org.nanotek.metaclass.bytebuddy;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.attributes.AttributeBaseBuilder;

import net.bytebuddy.dynamic.DynamicType.Builder;

public class RdbmsEntityBaseBuddy 
implements EntityBaseByteBuddy {

	private RdbmsMetaClass metaClass;

	private RdbmsEntityBaseBuddy(RdbmsMetaClass metaClass) {
		this.metaClass = metaClass;
	}
	
	public static RdbmsEntityBaseBuddy instance(RdbmsMetaClass metaClass) {
		return new RdbmsEntityBaseBuddy(metaClass);
	}

	public Class<?> getLoadedClassInDeaultClassLoader(){
		
		Builder<?> builder =   AttributeBaseBuilder
				.on()
				.generateClassAttributes(metaClass , 
				this.generateBuilderWithClassName
				(this.generateByteBuddy() , metaClass));
			Class<?> loaded = 
			Optional.of(builder)
			.map(b -> b.make().load(getClass().getClassLoader())
					.getLoaded()) .orElseThrow() ;
			
			return loaded;
	}
	
	public RdbmsMetaClass getMetaClass() {
		return this.metaClass;
	}
	
}
