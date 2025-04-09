package org.nanotek.metaclass.bytebuddy.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsIndex;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.metaclass.BuilderMetaClassRegistry;
import org.nanotek.metaclass.ProcessedForeignKeyRegistry;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.ColumnAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.IdAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.attributes.TemporalAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.EmailAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MaxAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MinAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotBlankAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotEmptyAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotNullAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.SizeAnnotationDescriptionFactory;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import net.bytebuddy.description.annotation.AnnotationDescription;

public interface AnnotationDescriptionFactory<T extends Annotation , K> {

	
	default AnnotationDescription buildAnnotationDescription(Class<T> annotationType) {
		return AnnotationDescription.Builder.ofType(annotationType).build();
	}
	
	default Optional<AnnotationDescription> buildAnnotationDescription(K ma) {
		return Optional.empty();
	}
	
	/**
	 * @deprecated
	 * @param ma
	 * @param metaClass
	 * @param builderMetaClassRegistry
	 * @return
	 */
	default Optional<AnnotationDescription> buildForeignAnnotationDescription
			(RdbmsMetaClassAttribute ma, RdbmsMetaClass metaClass,BuilderMetaClassRegistry builderMetaClassRegistry) {
		throw new RuntimeException("This method is deprecated since the processes fk registry was added");
	}
	
	default Optional<AnnotationDescription> buildForeignAnnotationDescription
			(RdbmsMetaClassAttribute ma, RdbmsMetaClass metaClass,BuilderMetaClassRegistry builderMetaClassRegistry,
			ProcessedForeignKeyRegistry processedForeignKeyRegistry) {	
			return Optional.empty();
	}
	
	public static class AttributeAnnotationDescriptionBuilder<K extends RdbmsMetaClassAttribute> {
		
		public static AttributeAnnotationDescriptionBuilder<RdbmsMetaClassAttribute> on () 
		{
			return new AttributeAnnotationDescriptionBuilder<RdbmsMetaClassAttribute>();
		}
		
		public AnnotationDescription[] build(K k) {
			var annotations = new ArrayList<AnnotationDescription>();
			
			EmailAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent (a -> annotations.add(a));
			MaxAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a->annotations.add(a));
			MinAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			SizeAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			NotBlankAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a ->annotations.add(a));
			NotNullAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			NotEmptyAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			ColumnAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			IdAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			TemporalAnnotationDescriptionFactory.on().buildAnnotationDescription(k)
			.ifPresent(a -> annotations.add(a));
			return annotations.toArray(new AnnotationDescription[annotations.size()]);
		}
	}
	
	public static class ForeignAttributeAnnotationDescriptionBuilder<K extends RdbmsMetaClassAttribute> {
		
		public static ForeignAttributeAnnotationDescriptionBuilder<RdbmsMetaClassAttribute> on () 
		{
			return new ForeignAttributeAnnotationDescriptionBuilder<RdbmsMetaClassAttribute>();
		}
		public AnnotationDescription[] build(K fk,
													RdbmsMetaClass fkRdbmsMetaClass,
													BuilderMetaClassRegistry builderMetaClassRegistr,
													ProcessedForeignKeyRegistry processedForeignKeyRegistry) {
			
			var annotations = new ArrayList<AnnotationDescription>();
			
			RdbmsMetaClassForeignKey foreignKey = findForeignKey(fk,fkRdbmsMetaClass);
			
			//TODO: will use the index to choose annotation type Many-One or One-One
			Optional<RdbmsIndex> index = findRdbmsIndex(fk, fkRdbmsMetaClass);
			
			//TODO: move this to the proper factory
			if(index.isPresent()) {
				AnnotationDescription adoo = AnnotationDescription.Builder.ofType(OneToOne.class).build();
				annotations.add(adoo);
			}else {
				AnnotationDescription admo = AnnotationDescription.Builder.ofType(ManyToOne.class).build();
				annotations.add(admo);
			}
			AnnotationDescription joinAnnotation = AnnotationDescription.Builder
														.ofType(JoinColumn.class)
														.define("name", foreignKey.getJoinColumnName())
														.define("referencedColumnName",foreignKey.getColumnName())
														.build();
														
			annotations.add(joinAnnotation);
			processedForeignKeyRegistry.registryForeignKeyMetaClass(foreignKey, fkRdbmsMetaClass);
			return annotations.toArray(new AnnotationDescription[annotations.size()]);
		}
		
		private Optional<RdbmsIndex> findRdbmsIndex(K fk, RdbmsMetaClass fkRdbmsMetaClass) {
			return fkRdbmsMetaClass
					.getRdbmsIndexes()
					.stream()
					.filter(index -> findColumnInIndex(index, fk))
					.filter(index -> index.getIsUnique())
					.filter(index -> index.getColumnNames().size()==1)
					.findFirst();
		}
		private Boolean findColumnInIndex(RdbmsIndex index, K fk) {
			return index.getColumnNames().stream().filter(c -> c.equalsIgnoreCase(fk.getColumnName())).count()>0;
		}
		private RdbmsMetaClassForeignKey findForeignKey(K k, RdbmsMetaClass fkRdbmsMetaClass) {
			return fkRdbmsMetaClass
					.getRdbmsForeignKeys()
					.stream()
					.filter(fk -> fk.getJoinColumnName().equals(k.getColumnName())).findFirst().get();
		}
	}
	
}
