package org.nanotek.metaclass.bytebuddy;

import java.lang.annotation.Annotation;

import org.nanotek.Base;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.orm.EntityAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.TableAnnotationDescriptionFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;

public interface EntityBaseByteBuddy extends BaseByteBuddy<RdbmsMetaClass, RdbmsMetaClassAttribute> {

	//TODO:verify how to fit indexes structure for the TableAnnotation.
	@Override
	default Builder<?> generateBuilderWithClassName(ByteBuddy bytebuddy,
			RdbmsMetaClass metaclass) {
		
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  ,Base.class).build();
		return bytebuddy
				.subclass(td)
				.name(metaclass.getClassName())
				.annotateType(EntityAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass))
				.annotateType(TableAnnotationDescriptionFactory.on().buildAnnotationDescription(metaclass))
				.withHashCodeEquals()
				.withToString();
	}
}
