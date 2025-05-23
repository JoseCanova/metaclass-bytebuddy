package org.nanotek.metaclass.bytebuddy;

import org.nanotek.Base;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.orm.EntityAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.TableAnnotationDescriptionFactory;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

/**
 * Generates the ByteBuddy builder based upon in a
 * @see RdbmsMetaClass definition and a default package hierarchy.
 * 
 */
public interface EntityBaseByteBuddy 
extends MetaByteBuddy<RdbmsMetaClass, RdbmsMetaClassAttribute> {

	public static String basePackage = "org.nanotek.entity";
	
	@Override
	default Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy,
													RdbmsMetaClass metaclass) {
		
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  , TypeDescription.Latent.class).build();
		return bytebuddy
				.subclass(td)
				.name(basePackage.concat(".").concat(metaclass.getClassName()))
				.annotateType(EntityAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass).get())
				.annotateType(TableAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass).get())
				.withHashCodeEquals()
				.withToString();
	}
}
