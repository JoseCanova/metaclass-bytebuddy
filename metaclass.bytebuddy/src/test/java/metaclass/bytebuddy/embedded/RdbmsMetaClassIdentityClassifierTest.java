package metaclass.bytebuddy.embedded;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nanotek.ClassConfigurationInitializer;
import org.nanotek.RdbmsMetaClassIdentityClassifier;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RdbmsMetaClassIdentityClassifierTest 
implements ClassConfigurationInitializer,RdbmsMetaClassIdentityClassifier{

	
	private List<RdbmsMetaClass> metaClasses;
	
	@BeforeEach
	void prepareDataForTest() {
		try {
			metaClasses = getMetaClasses("metaclass_multikey.json");
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	@Test
	void testCompositeKeyClassifier() {
		assertNotNull(metaClasses);
		assertTrue(metaClasses.size() == 1);
		Optional<MetaClassIdentityClassification> optClassification = classifyIdentity(metaClasses.get(0));
		optClassification.ifPresentOrElse(classifier ->
			assertTrue(classifier.classification().equals(KeyClassification.COMPOSITE))
		,() -> {throw new RuntimeException("");});
	}
	
	
	@Override
	public List<RdbmsMetaClass> getMetaClasses(String uriEndpont) {
		try (			InputStream is = RdbmsMetaClassIdentityClassifierTest.class
				.getClassLoader()
				.getResourceAsStream("metaclass_multikey.json");
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
