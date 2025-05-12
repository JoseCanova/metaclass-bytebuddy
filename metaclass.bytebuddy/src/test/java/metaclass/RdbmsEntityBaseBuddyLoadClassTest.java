package metaclass;

import java.io.InputStream;
import java.nio.file.FileSystem;

import org.junit.jupiter.api.Test;
import org.nanotek.EntityPathConfigurableClassLoader;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.RdbmsEntityBaseBuddy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class RdbmsEntityBaseBuddyLoadClassTest {

	public static final FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

	@Test
	void testLoadClassInputStream() throws Exception{
		EntityPathConfigurableClassLoader classLoader = new EntityPathConfigurableClassLoader(fileSystem,null,null,null);
		InputStream is = getClass().getClassLoader().getResourceAsStream("object_metaclass.json");
		ObjectMapper mapper = new ObjectMapper();
		RdbmsMetaClass metaClass = mapper.readValue(is, RdbmsMetaClass.class);
		RdbmsEntityBaseBuddy byteBuddy = new RdbmsEntityBaseBuddy(metaClass);
		Class<?> clazz = byteBuddy.getLoadedClass(classLoader);
	}
}
