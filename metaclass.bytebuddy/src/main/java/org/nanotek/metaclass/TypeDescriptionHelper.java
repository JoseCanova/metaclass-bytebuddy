package org.nanotek.metaclass;

import jakarta.persistence.Id;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.field.FieldList;
import net.bytebuddy.description.type.TypeDescription;

public class TypeDescriptionHelper {

	public TypeDescriptionHelper() {
	}

	void findIdAttribute(TypeDescription typeDescription) {
		FieldList<FieldDescription.InDefinedShape> theFieldList= typeDescription.getDeclaredFields();
		theFieldList.forEach(fd ->{
			isIdField(fd.getDeclaredAnnotations());
		});
	}

	private Boolean isIdField(AnnotationList declaredAnnotations) {
		return declaredAnnotations
							.stream()
							.filter(an -> an.getClass()
							.equals(Id.class))
							.count() > 0;
	}
	
}
