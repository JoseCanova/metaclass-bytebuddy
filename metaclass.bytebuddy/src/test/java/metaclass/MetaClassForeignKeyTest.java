package metaclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetaClassForeignKeyTest {

	
	List<RdbmsMetaClass> theClasses;
	
	
	@BeforeEach
	void testJsonReader() throws Exception{
		
        ObjectMapper objectMapper = new ObjectMapper();
        	List<?> list = objectMapper
        							.readValue (getClass().getResourceAsStream("/metaclass_list_foreign_indexes.json")
        										, List.class);
        	assertTrue (list.size()==2);
        	theClasses = list
				        	.stream()
				        	.map(theNode -> objectMapper.convertValue(theNode,RdbmsMetaClass.class))
				        	.collect(Collectors.toList());
        	
    }
	
	@Test
	void testForeignKeyClassResult() {
		
		theClasses
		.stream()
		.filter(theClass -> theClass.getRdbmsForeignKeys().size() == 1)
		.forEach(theClass -> {
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
		});
	}

}
