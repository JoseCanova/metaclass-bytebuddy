package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClassAttribute;
import org.nanotek.metaclass.bytebuddy.EntityBaseByteBuddy;
import org.nanotek.metaclass.bytebuddy.Holder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bytebuddy.dynamic.DynamicType.Builder;

public class MetaClassBaseTest {
    
	
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
	void testClassBuilder() throws ClassNotFoundException {
	
		EntityBaseByteBuddy eb = new EntityBaseByteBuddy() {};
		Builder<?> builder =  eb.generateBuilderWithClassName
								(eb.generateByteBuddy() , theClass);
		
		Holder<Builder<?>> holder = Holder.of(builder);
		
		List<RdbmsMetaClassAttribute> atts = theClass.getMetaAttributes();
		atts
			.stream()
			.forEach(att -> {
				Class<?> theJavaClass = getJavaClass(att.getClazz());
				Builder<?> theBuilder = holder.get().orElseThrow();
				holder.put(theBuilder.defineProperty(att.getFieldName(), theJavaClass));
			});
		Class<?> loaded = 
		holder.get()
		.map(b -> b.make().load(getClass().getClassLoader()).getLoaded()) .orElseThrow() ;
		
		System.out.println(loaded.getName());
		Stream
		.of(loaded.getDeclaredFields())
		.forEach(f -> System.out.println(f.getName()));
	}


	private Class<?> getJavaClass(String clazz) {
		try { 
			return Class.forName(clazz);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	
}
