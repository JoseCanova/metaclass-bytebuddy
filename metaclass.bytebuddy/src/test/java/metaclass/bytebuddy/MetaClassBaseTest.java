package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Stream;

import org.instancio.Instancio;
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
	
	
	void testTestClassBuilder() throws ClassNotFoundException {
		testClassBuilder(); 
	}
	
	@Test
	void testLookupMethodHandle() throws Exception{
		Class<?> theClass = testClassBuilder();
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandles.Lookup plookup = MethodHandles.privateLookupIn(theClass,lookup);
		Stream
		.of(theClass.getDeclaredFields())
		.forEach(f -> {
			System.out.println(f.getName());
			try {
				Object instance = Instancio.create(theClass);
				MethodHandle mh = plookup.findGetter(theClass, f.getName() , f.getType());
				Object result = mh.invoke(instance);
				System.err.println(result);
				MethodHandle mhs = plookup.findSetter(theClass, f.getName() , f.getType());
				Object finstance = Instancio.create(f.getType());
				System.err.println("finstance" + finstance);
				mhs.invoke(instance,finstance);	
				Object fresult = mh.invoke(instance);
				System.err.println(fresult);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		});
		
	}
	
	Class<?> testClassBuilder() throws ClassNotFoundException {
	
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
		return loaded;
	}


	private Class<?> getJavaClass(String clazz) {
		try { 
			return Class.forName(clazz);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	
}
