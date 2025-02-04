package org.nanotek.metaclass.bytebuddy;

import java.lang.annotation.Annotation;

import org.nanotek.Base;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;

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
		String className = metaclass.getClassName();
		String tableName = metaclass.getTableName();
		
		TypeDefinition td = TypeDescription.Generic.Builder.parameterizedType(Base.class  ,Base.class).build();
		return bytebuddy
				.subclass(td)
				.name(metaclass.getClassName())
				.annotateType(new EntityImpl(className))
				.annotateType(new TableImpl(tableName))
				.withHashCodeEquals()
				.withToString();
	}
	
	
	class TableImpl implements Table{

		private String name;
		
		public TableImpl(String name2) {
			this.name = name2;
		}
		
		@Override
		public Class<? extends Annotation> annotationType() {
			return Table.class;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public String catalog() {
			return "";
		}

		@Override
		public String schema() {
			return "";
		}

		@Override
		public UniqueConstraint[] uniqueConstraints() {
			return new UniqueConstraint[0];
		}

		@Override
		public Index[] indexes() {
			return new Index[0];
		}
		
	}
	
	class EntityImpl implements Entity{

		private String name;
		
		public EntityImpl(String name2) {
			this.name = name2;
		}
		
		@Override
		public Class<? extends Annotation> annotationType() {
			// TODO Auto-generated method stub
			return Entity.class;
		}

		@Override
		public String name() {
			return name;
		}
		
	}
}
