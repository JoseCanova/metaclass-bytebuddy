package org.nanotek.metaclass.bytebuddy.annotations.orm;

import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import jakarta.persistence.Entity;

public class EmbeddedAnnotationDescriptionFactory
implements AnnotationDescriptionFactory<Entity, RdbmsMetaClass> {

}
