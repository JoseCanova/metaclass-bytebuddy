package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonReader {
    
	
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
        	assertTrue(theClass.getTableName().equals("simple_table"));
    }
	
	
	//TODO: implement the class builder with attributes and validation.
	@Test
	void testClassBuilder() {
		
	}
	
	
	
}
