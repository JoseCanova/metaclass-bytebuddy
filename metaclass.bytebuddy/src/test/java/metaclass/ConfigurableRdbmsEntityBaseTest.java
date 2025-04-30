package metaclass;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.ConfigurableRdbmsEntityBase;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

public class ConfigurableRdbmsEntityBaseTest {

	public ConfigurableRdbmsEntityBaseTest() {
	}
	
	
	@Test 
	void test(){
		ConfigurableRdbmsEntityBase base  = new ConfigurableRdbmsEntityBase("default");
		var instance = base.getEntityBaseByteBuddy(RdbmsMetaClass::new);
		assertNotNull(instance);
		assertTrue(instance.getClass().equals(RdbmsEntityBaseBuddy.class));
	}

}
