package metaclass.bytebuddy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.nanotek.meta.model.MetaClass;
import org.nanotek.meta.model.rdbms.RdbmsMetaClass;
import org.nanotek.metaclass.bytebuddy.BaseByteBuddy;
import org.nanotek.metaclass.bytebuddy.MetaByteBuddy;

import jakarta.persistence.Column;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy;
import net.bytebuddy.jar.asm.Opcodes;

public class BaseByteBuddyInitializationTest {

	public BaseByteBuddyInitializationTest() {
	}

	
//	@Test
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
		Builder<?> abuilder  = builder.defineField("myString", java.lang.String.class, Opcodes.ACC_PRIVATE);
		TypeDescription tb = abuilder.toTypeDescription();
		
		AnnotationDescription ad = AnnotationDescription.Builder.ofType(Column.class)
				.define("name", "aString").define("nullable", true).build();
		
		Builder<?> bbuilder  = abuilder.defineProperty("aString", java.lang.String.class).annotateField(new AnnotationDescription[] {ad});
		TypeDescription atb = bbuilder.toTypeDescription();
		List<FieldDescription.InDefinedShape> theList = atb.getDeclaredFields();
		theList.stream().forEach(is -> System.err.println(is.getDeclaredAnnotations() + is.toString()));
		List<MethodDescription.InDefinedShape> theMethods = atb.getDeclaredMethods();
		theMethods.stream().forEach(mi -> {
			System.err.println(mi.toString());
		});
		
		Class<?> clazz = bbuilder.make()
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
