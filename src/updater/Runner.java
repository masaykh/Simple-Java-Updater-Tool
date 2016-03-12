package updater;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Runner {

	private File externalJar;
	private String mainClass;
	public Runner(File externalJar,String mainClass ){
	this.externalJar = externalJar;
	this.mainClass = mainClass;
	exec();
	}
	
	public void exec() {
		
	
	JarFile jarFile;
	try {
		jarFile = new JarFile(externalJar);

	try {
	    Manifest manifest = jarFile.getManifest();
	    mainClass = manifest.getMainAttributes().getValue("Main-Class");
	} finally {
	    jarFile.close();
	}
	URLClassLoader child = new URLClassLoader(new URL[]{externalJar.toURI().toURL()}, this.getClass().getClassLoader());
	Class<?> classToLoad = Class.forName(mainClass, true, child);
	Method method = classToLoad.getDeclaredMethod("main", String[].class);
	Object[] arguments = {new String[0]};
	method.invoke(null, arguments);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
	

