package metaclass.bytebuddy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

public class SimplePojoMethodHandleTest {

	public SimplePojoMethodHandleTest() {
	}
	
	@Test
	void test() throws Exception {
		Class<?> theClass = SimplePojo.class;
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

}
