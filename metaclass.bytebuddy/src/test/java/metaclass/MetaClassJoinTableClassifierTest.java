package metaclass;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.JoinTableClassifier;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MetaClassJoinTableClassifierTest {

	List<RdbmsMetaClass> theClasses;
	
	
	@BeforeEach
	void testJsonReader() throws Exception{
		
        ObjectMapper objectMapper = new ObjectMapper();
        	List<?> list = objectMapper
        							.readValue (getClass().getResourceAsStream("/metaclass_join_table.json")
        										, List.class);
        	theClasses = list
				        	.stream()
				        	.map(theNode -> objectMapper.convertValue(theNode,RdbmsMetaClass.class))
				        	.collect(Collectors.toList());
    }

	@Test
	void test() {
		assertTrue(theClasses.size()>0);
		JoinTableClassifier jtc = new JoinTableClassifier();
		var count = theClasses
			.stream()
			.filter(mc -> jtc.classify(mc).isPresent())
			.collect(Collectors.toList());
		assertTrue(count.size() ==  1);
	}
}
