package metaclass.bytebuddy.embedded;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.ClassConfigurationInitializer;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import com.fasterxml.jackson.databind.ObjectMapper;
//TODO:Move the identity classifier to the support project.
public class RdbmsMetaClassIdentityClassifierTest 
implements ClassConfigurationInitializer{

	private List<RdbmsMetaClass> metaClasses;
	private List<RdbmsMetaClass> metaClassesSimple;
	@BeforeEach
	void prepareDataForTest() {
		try {
			metaClasses = getMetaClasses("metaclass_multikey.json");
			metaClassesSimple = getMetaClasses("metaclass_simple_identity.json");
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Test
	void testCompositeKeyClassifier() {
		assertNotNull(metaClasses);
		assertTrue(metaClasses.size() == 1);
		RdbmsMetaClass metaClass = metaClasses.get(0);
		assertTrue(metaClass.getIdentityClassification().equals("COMPOSITE"));
		
		assertNotNull(metaClassesSimple);
		assertTrue(metaClassesSimple.size() == 1);
		RdbmsMetaClass metaClassSimple = metaClassesSimple.get(0);
		assertTrue(metaClassSimple.getIdentityClassification().equals("SIMPLE"));
		
	}
	
	@Override
	public List<RdbmsMetaClass> getMetaClasses(String uriEndpont) {
		try (			InputStream is = RdbmsMetaClassIdentityClassifierTest.class
				.getClassLoader()
				.getResourceAsStream(uriEndpont);
					){
			List<RdbmsMetaClass> al = new ArrayList<>();
			var objectMapper = new ObjectMapper ();
			objectMapper.readValue
	    			(is
	    					, List.class)
	    					.stream()
	    					.forEach(mc -> al.add(objectMapper.convertValue(mc,RdbmsMetaClass.class)));
			return al;
		}catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Bad Metaclass File");
		}
	}
}
