package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.metaclass.bytebuddy.MetaByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;

public class BaseByteBuddyInitializationTest {

	public BaseByteBuddyInitializationTest() {
	}

	
	@Test
	void testBaseByteBuddyInitialization() {
		MetaClass<?,?> mc = new MetaClass<>();
		mc.setClassName("ClassName");
		MetaByteBuddy mb = new MetaByteBuddy() {};
		ByteBuddy buddy = mb.generateByteBuddy();
		Builder<?> builder = mb.generateBuilderWithClassName(buddy, mc);
		Class<?> clazz = builder.make().load(getClass().getClassLoader()).getLoaded();
		assertNotNull(clazz);
		assertTrue(clazz.getName().equals("ClassName"));
	}
}
