package org.nanotek.metaclass.bytebuddy.annotations.orm.relation;

import java.util.Optional;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassForeignKey;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.OneToOne;
import net.bytebuddy.description.annotation.AnnotationDescription;

public class OneToOneAnnotationDescrptionFactory
implements AnnotationDescriptionFactory<OneToOne,ForeignKeyMetaClassRecord>{

	private OneToOneAnnotationDescrptionFactory() {
	}
	
	public static OneToOneAnnotationDescrptionFactory on() {
		return new OneToOneAnnotationDescrptionFactory();
	}
	
 //TODO:define other annotation properties here.
	@Override
	public Optional<AnnotationDescription> buildAnnotationDescription(ForeignKeyMetaClassRecord theRecord) {
		AnnotationDescription ad= theRecord
		.foreignKey()
		.map(fk -> findIdAttribute(fk,theRecord.rdbmsMetaClass()))
		.map(att -> {
			return AnnotationDescription
					.Builder.ofType(OneToOne.class)
					.define("mappedBy", att.getFieldName()).build();
		}).orElse(AnnotationDescription
					.Builder.ofType(OneToOne.class).build());
		return Optional.of(ad);
	}
	
	public RdbmsMetaClassAttribute findIdAttribute(RdbmsMetaClassForeignKey fk , RdbmsMetaClass metaClass) {
		return metaClass
			.getMetaAttributes()
			.stream()
			.filter(att -> att.getColumnName().equals(fk.getColumnName())).findFirst().orElseThrow();
	}
}
