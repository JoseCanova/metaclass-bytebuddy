package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.BaseByteBuddy;
import org.nanotek.metaclass.bytebuddy.MetaByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;

public class BaseByteBuddyInitializationTest {

	public BaseByteBuddyInitializationTest() {
	}

	
	@Test
	void testBaseByteBuddyInitialization() {
		MetaClass<?,?> mc = new MetaClass<>();
		mc.setClassName("org.nanotek.ClassName");
		MetaByteBuddy mb = new BaseByteBuddy() {};
		ByteBuddy buddy = mb.generateByteBuddy();
		Builder<?> builder = mb.generateBuilderWithClassName(buddy, mc);
		Class<?> clazz = builder.make()
				.load(getClass().getClassLoader(),
						ClassLoadingStrategy.Default.INJECTION.with(PackageDefinitionStrategy.Trivial.INSTANCE))
				.getLoaded();
		assertNotNull(clazz);
		assertTrue(clazz.getSimpleName().equals("ClassName"));
		System.out.println("package name " + clazz.getPackage().getName());
		System.out.println("clazz name " + clazz.getSimpleName());
	}
	
	@Test
	void testRdbmsBaseByteBuddyInitialization() {
		RdbmsMetaClass  mc = new RdbmsMetaClass();
		mc.setClassName("org.nanotek.ClassName");
		MetaByteBuddy mb = new BaseByteBuddy() {};
		ByteBuddy buddy = mb.generateByteBuddy();
		Builder<?> builder = mb.generateBuilderWithClassName(buddy, mc);
		Class<?> clazz = builder.make()
				.load(getClass().getClassLoader(),
						ClassLoadingStrategy.Default.INJECTION.with(PackageDefinitionStrategy.Trivial.INSTANCE))
				.getLoaded();
		assertNotNull(clazz);
		assertTrue(clazz.getSimpleName().equals("ClassName"));
		System.out.println("package name " + clazz.getPackage().getName());
		System.out.println("clazz name " + clazz.getSimpleName());
		System.out.println("class supertype " + clazz.getSuperclass().getName());
	}
}
