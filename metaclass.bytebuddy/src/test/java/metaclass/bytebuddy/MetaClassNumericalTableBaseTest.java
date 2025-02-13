package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;

import jakarta.persistence.Column;

public class MetaClassNumericalTableBaseTest {
    
	
	RdbmsMetaClass theClass ;
	
	
	@BeforeEach
	void testJsonReader() throws Exception{
		
        ObjectMapper objectMapper = new ObjectMapper();
        	List<JsonNode> list = objectMapper.readValue
            			(getClass().getResourceAsStream("/metaclass_numeric.json")
            					, List.class);
        	assertTrue (list.size()==1);
        	Object theNode = list.get(0);
        	theClass = objectMapper.convertValue(theNode,RdbmsMetaClass.class);
        	assertTrue(theClass.getTableName().equals("simple_numeric_table"));
    }
	
	
	//TODO: implement the class builder with attributes and validation.
	@Test
	void testClassBuilder() {
	
			var baseByteBuddy = RdbmsEntityBaseBuddy
				.instance(theClass);
			var loaded = baseByteBuddy
				.getLoadedClassInDefaultClassLoader();
			
			List<RdbmsMetaClassAttribute> atts = theClass.getMetaAttributes();
			assertTrue(loaded.getDeclaredFields().length == atts.size());
			List<FieldAttributePair> theList = dumpDeclaredFields(loaded , theClass);
			assertTrue(loaded.getDeclaredFields().length == theList.size());
			verifyNumericFields(theList);
			verifyAttributeColumnAnnotation(theList);
	}


	private void verifyAttributeColumnAnnotation(List<FieldAttributePair> theList) {
		theList
		.stream()
		.filter(pair -> !pair.attribute().isPartOfId())
		.map(pair -> pair.field())
		.map(field -> field.getAnnotations())
		.forEach(anns -> assertTrue( containsColumnAnnotation(anns)));
	}


	private Boolean containsColumnAnnotation(Annotation[] anns) {
		long theCount =  Stream
		.of(anns)
		.filter(ann -> ann.annotationType().equals(Column.class))
		.count() ;
		return theCount > 0;
	}


	private void verifyNumericFields(List<FieldAttributePair> theList) {
		theList
		.stream()
		.forEach(pair -> {
			assertTrue(pair.attribute().getClazz().equals(pair.field().getType().getName()));
		});
	}


	private List<FieldAttributePair> dumpDeclaredFields(Class<?> loaded, RdbmsMetaClass theClass2) {
		return Stream
			.of(loaded.getDeclaredFields())
			.map(f -> createFieldAttributePair(f,theClass.getMetaAttributes()))
			.collect(Collectors.toList());
	}

	
	
	private FieldAttributePair createFieldAttributePair(Field f, List<RdbmsMetaClassAttribute> metaAttributes) {
		return	metaAttributes.stream()
		.filter( att -> att.getFieldName().equals(f.getName()))
		.map(att -> new FieldAttributePair(f, att))
		.findFirst().get();
	}


	
	public static record FieldAttributePair(Field field, RdbmsMetaClassAttribute attribute){
		public FieldAttributePair(Field field, RdbmsMetaClassAttribute attribute) {
			this.field=field;
			this.attribute=attribute;
		}
	}
	
	
}
