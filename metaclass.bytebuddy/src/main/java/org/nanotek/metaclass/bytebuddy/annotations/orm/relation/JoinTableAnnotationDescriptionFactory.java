package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.type.TypeDescription;

public class JoinTableAnnotationDescriptionFactory
		implements AnnotationDescriptionFactory<JoinTable, RdbmsMetaClass>{

	private JoinTableAnnotationDescriptionFactory() {}
	
	public JoinTableAnnotationDescriptionFactory on() {
		return new JoinTableAnnotationDescriptionFactory();
	}
	 @Override
	public Optional<AnnotationDescription> buildAnnotationDescription(RdbmsMetaClass ma) {
		 
		 AnnotationDescription ad =  Optional
		 .of(ma)
		 .filter(m -> m.isJoinMetaClass())
		 .map(m->buildJoinTableAnnotationDescription(m))
		 .orElseThrow();
		 
		 return Optional.of(ad);
	}

	private AnnotationDescription buildJoinTableAnnotationDescription(RdbmsMetaClass m) {
		
		AnnotationDescription parentJoinColumn = JoinColumnAnnotationDescriptionFactory
								.on()
								.buildAnnotationDescription(m.getRdbmsForeignKeys().get(0)).get();
		
		AnnotationDescription childJoinColumn = JoinColumnAnnotationDescriptionFactory
								.on()
								.buildAnnotationDescription(m.getRdbmsForeignKeys().get(1)).get();
		
		TypeDescription joinColumnTypeDescription = new TypeDescription.ForLoadedType(JoinColumn.class);
	    var avParent = AnnotationValue.ForDescriptionArray.of(joinColumnTypeDescription, new AnnotationDescription[]{parentJoinColumn});
	    var avChild =  AnnotationValue.ForDescriptionArray.of(joinColumnTypeDescription, new AnnotationDescription[]{childJoinColumn});
		
	    return AnnotationDescription
			.Builder
			.ofType(JoinTable.class)
			.define("name", m.getTableName())
			.define("joinColumns",avParent)
			.define("inverseJoinColumns",avChild)
			.build();
	}
	
}
