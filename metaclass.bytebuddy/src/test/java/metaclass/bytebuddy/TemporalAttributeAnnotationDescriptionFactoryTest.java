package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.annotations.AnnotationDescriptionFactory;

import net.bytebuddy.description.annotation.AnnotationDescription;

public class TemporalAttributeAnnotationDescriptionFactoryTest {

	public TemporalAttributeAnnotationDescriptionFactoryTest() {
	}

	@Test
	void testFactory() {
		 RdbmsMetaClassAttribute attribute = new RdbmsMetaClassAttribute();
	        attribute.setRequired(true);
	        attribute.setSqlType("TIMESTAMP");
	        attribute.setLength("255");
	        attribute.setColumnName("date");
	        attribute.setPartOfId(false);
	        attribute.setClazz("java.util.Date");
	        
	        AnnotationDescription[] as = AnnotationDescriptionFactory
	        .AttributeAnnotationDescriptionBuilder
	        .on()
	        .build(attribute,false);
	        
	        assertTrue(as.length>0);
	        
	        List<AnnotationDescription> annotations = Arrays.asList(as);
	        
	        annotations.forEach(System.out::println);


	}
	
}
