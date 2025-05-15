package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetaClassAttributeTest {

	
	RdbmsMetaClass theClass ;
	
	
	@BeforeEach
	void testJsonReader() throws Exception{
		
        ObjectMapper objectMapper = new ObjectMapper();
        	List<JsonNode> list = objectMapper.readValue
            			(getClass().getResourceAsStream("/metaclass.json")
            					, List.class);
        	assertTrue (list.size()==1);
        	Object theNode = list.get(0);
        	theClass = objectMapper.convertValue(theNode,RdbmsMetaClass.class);
        	assertTrue(theClass.getTableName().equals("simple_table_identity"));
    }
	
	@Test
	void testClassResult() {
		var baseByteBuddy = RdbmsEntityBaseBuddy
			.instance(theClass);
		var loaded = baseByteBuddy
			.getLoadedClassInDefaultClassLoader();
		
		List<RdbmsMetaClassAttribute> atts = theClass.getMetaAttributes();
	/*
	 * Swimming through the void, we hear the word
		We lose ourselves, but we find it all
	 */
		assertTrue(loaded.getDeclaredFields().length == atts.size());
		}

}
