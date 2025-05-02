package metaclass.bytebuddy;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;

public interface ClassFileSerializer {

public static void saveEntityFile(Class<?> c, ClassLoader bytearrayclassloader2) {
		
		String directoryString = System.getProperty("java.io.tmpdir");
		
		String fileName =  c.getName().replaceAll("[.]","/").concat(".class");
		
		InputStream is = bytearrayclassloader2.getResourceAsStream(fileName);

		try 
		{

			byte[] classBytes = is.readAllBytes();
			var simpleName = c.getSimpleName();
			Path dirPath = Paths.get(directoryString, new String[] {});
			Files.createDirectories(dirPath);
			var classLocation  = directoryString.concat("/").concat(simpleName).concat(".class");
			Path classPath = Paths.get(classLocation, new String[] {});
			if(!Files.exists(classPath, LinkOption.NOFOLLOW_LINKS))
    			Files.createFile(classPath, new FileAttribute[0]);
			Files.write(classPath, classBytes, StandardOpenOption.WRITE);

			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

 default void saveEntityFile(File fileLocation , Class<?> c, ClassLoader bytearrayclassloader2) {
	
	String directoryString = fileLocation.getAbsolutePath() ;
	
	String fileName =  c.getName().replaceAll("[.]","/").concat(".class");
	
	InputStream is = bytearrayclassloader2.getResourceAsStream(fileName);

	try 
	{

		byte[] classBytes = is.readAllBytes();
		var simpleName = c.getSimpleName();
		Path dirPath = Paths.get(directoryString, new String[] {});
		Files.createDirectories(dirPath);
		var classLocation  = directoryString.concat("/").concat(simpleName).concat(".class");
		Path classPath = Paths.get(classLocation, new String[] {});
		if(!Files.exists(classPath, LinkOption.NOFOLLOW_LINKS))
			Files.createFile(classPath, new FileAttribute[0]);
		Files.write(classPath, classBytes, StandardOpenOption.WRITE);

		
	}catch(Exception ex) {
		ex.printStackTrace();
	}
	
}
}
