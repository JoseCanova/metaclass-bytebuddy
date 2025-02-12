package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
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
        	assertTrue(theClass.getTableName().equals("simple_table"));
    }
	
	@Test
	void testClassResult() {
		var baseByteBuddy = RdbmsEntityBaseBuddy
			.instance(theClass);
		var loaded = baseByteBuddy
			.getLoadedClassInDefaultClassLoader();
		
		System.out.println(loaded.getName());
		Stream
		.of(loaded.getDeclaredFields())
		.forEach(f -> 
				System.out.println(f.getName()));
		}

}
