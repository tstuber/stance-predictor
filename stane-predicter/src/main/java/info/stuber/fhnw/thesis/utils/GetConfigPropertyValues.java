package info.stuber.fhnw.thesis.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class GetConfigPropertyValues {

	static InputStream inputStream;
	static Properties prop = null;
	static String propFileName = "config.properties";
	
	public static String getProperty(String property) {
		if(prop == null)
			try {
				readPropValues();
			} catch (IOException e) {
				return(e.toString());
			}
		
		return prop.getProperty(property);
	}
 
	private static void readPropValues() throws IOException {
 
		try {
			prop = new Properties();
			inputStream = GetConfigPropertyValues.class.getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
	}
}
