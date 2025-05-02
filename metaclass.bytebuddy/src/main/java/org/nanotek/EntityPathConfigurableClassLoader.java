package org.nanotek;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

public class EntityPathConfigurableClassLoader extends MetaClassVFSURLClassLoader {

	private String repositoryPath;
	
	private String entityPath;
	
	private String servicePath;
	
	public EntityPathConfigurableClassLoader(FileSystem fileSystem,
			 @Nullable String entityPath, @Nullable String repositoryPath, @Nullable String servicePath) {
		super(EntityPathConfigurableClassLoader.class.getClassLoader(), false, fileSystem);
		
		this.entityPath = Optional.ofNullable(entityPath).orElse(ENTITY_PATH);
		this.repositoryPath = Optional.ofNullable(repositoryPath).orElse(REPO_PATH);
		this.servicePath = Optional.ofNullable(servicePath).orElse(SERVICE_PATH);
		createEntityFileDirectory();
		
	}
	
	@Override
	public void postConstruct() {
	}
	 
	@Override 
	public  void createEntityFileDirectory() {
		try {
			Path dataPath = fileSystem.getPath(entityPath, new String[]{});
	        Files.createDirectories(dataPath);
	    	Path repoPath = fileSystem.getPath(repositoryPath,new String[]{});
	        Files.createDirectories(repoPath);
	        Path serPath = fileSystem.getPath(servicePath,new String[]{});
	        Files.createDirectories(serPath);
		}catch(IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
   }
	
	@Override
	public InputStream getResourceAsStream(String name) {
//		System.err.println("getResourceAsStream " + name);
		Path thePath = fileSystem.getPath(name, new String[0]);
    	boolean bol = Files.exists(thePath, new LinkOption[0]);
    	InputStream is = null;
    	try {
    		if(bol)
			 is = Files.newInputStream(thePath, StandardOpenOption.READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bol && is !=null? is : super.getResourceAsStream(name);
	}

	@Override
	public URL getResource(String name) {
//		System.err.println("getResource " + name);
		Path thePath = fileSystem.getPath(name, new String[0]);
    	boolean bol = Files.exists(thePath, new LinkOption[0]);
    	URL theURL = null;
    	try {
    		if(bol)	
    			theURL = thePath.toUri().toURL();
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
		return bol && theURL!=null? theURL : super.getResource(name);
	}

   @Override
	public Enumeration<URL> getResources(String name) throws IOException {
	   if (name.equals(repositoryPath))
	   {
		   List<URL> files =  new ArrayList<>();
		   System.err.println("getResoures Repositories " +  name);
		   Path theRepoPath = fileSystem.getPath(name, new String[0]);
		   if (Files.exists(theRepoPath, new LinkOption[0])) {
           	files.add(theRepoPath.toUri().toURL());	
           	return Collections.enumeration(files);
		   }
		   try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(theRepoPath)) {
	            for (Path file : directoryStream) {
	            	files.add(file.toUri().toURL());	
	            }
	            return Collections.enumeration(files);
	        }
	   }
	   
	   if (name.equals(entityPath))
	   {
		   List<URL> files =  new ArrayList<>();
		   System.err.println("getResoures Data " +  name);
		   Path thePath = fileSystem.getPath(name, new String[0]);
		   if (Files.exists(thePath, new LinkOption[0])) {
           	files.add(thePath.toUri().toURL());	
		   }
		   try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(thePath)) {
	            for (Path file : directoryStream) {
	            	files.add(file.toUri().toURL());	
	            }
	            return Collections.enumeration(files);
	        }
	   }
	  
	   return super.getResources(name);
   }

	public String getRepositoryPath() {
		return repositoryPath;
	}
	
	public String getEntityPath() {
		return entityPath;
	}
	
	public String getServicePath() {
		return servicePath;
	}
	
}
