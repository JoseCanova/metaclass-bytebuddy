package metaclass;

import org.junit.jupiter.api.Test;

import net.bytebuddy.ClassFileVersion;

public class ClassFileVersionResolverTest {

	@Test
	void testResolveClassVersion() {
		ClassFileVersion classFileVersion = ClassFileVersion.ofThisVm();
		System.out.println(classFileVersion.getJavaVersion());
	}
}
