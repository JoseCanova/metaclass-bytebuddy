package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.type.TypeDescription;

public class ManyToManyAnnotationDescriptionFactory 
implements AnnotationDescriptionFactory<ManyToMany, ForeignKeyMetaClassRecord> {

	private ManyToManyAnnotationDescriptionFactory() {
	}
	
	public static ManyToManyAnnotationDescriptionFactory on() {
		return new ManyToManyAnnotationDescriptionFactory();
	}
	
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord ma) {
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(ManyToMany.class).build();
		return Optional.of(ad);
	}

	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord ma,String mappedBy) {
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(ManyToMany.class).define("mappedBy", mappedBy).build();
		return Optional.of(ad);
	}

	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord lrmc, CascadeType cascadeType) {
		 TypeDescription cascadeTypeTd = new TypeDescription.ForLoadedType(CascadeType.class);
	     EnumerationDescription cascadeTypeEd = new EnumerationDescription.ForLoadedEnumeration(cascadeType);
	     var av = AnnotationValue.ForDescriptionArray.of(cascadeTypeTd, new EnumerationDescription[]{cascadeTypeEd});
	     AnnotationDescription ad = AnnotationDescription.Builder
	    		 .ofType(ManyToMany.class)
	    		 .define("cascade",av)
	    		 .build();
	     return Optional.of(ad);
	}

	
}
