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
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.ForeignKeyMetaClassRecord;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.JoinColumnAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.orm.relation.OneToOneAnnotationDescrptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.EmailAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MaxAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.MinAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotBlankAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotEmptyAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.NotNullAnnotationDescriptionFactory;
import org.nanotek.metaclass.bytebuddy.annotations.validation.SizeAnnotationDescriptionFactory;

import jakarta.persistence.ManyToOne;
import net.bytebuddy.description.annotation.AnnotationDescription;

public interface AnnotationDescriptionFactory<T extends Annotation , K> {

	default Optional<AnnotationDescription>  buildAnnotationDescription(){
		return Optional.empty();
	}
	
	default AnnotationDescription buildAnnotationDescription(Class<T> annotationType) {
		return AnnotationDescription.Builder.ofType(annotationType).build();
	}
	
	default Optional<AnnotationDescription> buildAnnotationDescription(K ma) {
		return Optional.empty();
	}
	
	default RelationType classifyRelationType(RdbmsMetaClassForeignKey foreignKey, RdbmsMetaClass rdbmsMetaClass) {
		return rdbmsMetaClass
		.getRdbmsForeignKeys()
		.stream()
		.filter(fk -> fk.equals(foreignKey))
		.findFirst()
		.map(fk -> RelationType.CHILD)
		.orElse(RelationType.PARENT);
	}
	
	default  RdbmsMetaClassAttribute findIdAttribute(RdbmsMetaClassForeignKey fk , RdbmsMetaClass metaClass) {
		return metaClass
				.getMetaAttributes()
				.stream()
				.filter(att -> att.getColumnName().equals(fk.getColumnName())).findFirst().orElseThrow();
		}

	//TODO| Fix parent annotation for the correct type
	default AnnotationDescription mountParentAnnotation(ForeignKeyMetaClassRecord theRecord,Class<? extends Annotation> annotationType) {
	    return Optional.of(theRecord
		.foreignKey())
		.map(fk -> findIdAttribute(fk,theRecord.rdbmsMetaClass()))
		.map(att -> {
			return AnnotationDescription
					.Builder.ofType(annotationType)
					.define("mappedBy", att.getFieldName()).build();
		}).orElseThrow();
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
			Optional<RdbmsIndex> uniqueIndex = findUniqueRdbmsIndex(fk, fkRdbmsMetaClass);
			
			uniqueIndex.ifPresentOrElse(index ->{
				ForeignKeyMetaClassRecord fkbmc =new ForeignKeyMetaClassRecord(foreignKey,fkRdbmsMetaClass,builderMetaClassRegistr);
				OneToOneAnnotationDescrptionFactory.on().buildAnnotationDescription(fkbmc)
				.ifPresentOrElse(adoo -> annotations.add(adoo) , ()-> new RuntimeException("no annotation present"));
			},()->{
				AnnotationDescription admo = AnnotationDescription.Builder.ofType(ManyToOne.class).build();
				annotations.add(admo);
			});
			
			JoinColumnAnnotationDescriptionFactory
							.on()
							.buildAnnotationDescription(foreignKey)
							.ifPresent(ant -> annotations.add(ant));
			
			processedForeignKeyRegistry.registryForeignKeyMetaClass(foreignKey, fkRdbmsMetaClass);
			return annotations.toArray(new AnnotationDescription[annotations.size()]);
		}
		
		private Optional<RdbmsIndex> findUniqueRdbmsIndex(K fk, RdbmsMetaClass fkRdbmsMetaClass) {
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
