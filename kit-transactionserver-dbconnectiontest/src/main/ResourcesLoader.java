package main;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;

public class ResourcesLoader {

	public static Properties loadInternalPropertyFile(String resourceName) {

		try {
			InputStream in = ClassLoader.getSystemResourceAsStream(resourceName);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			return properties;
		} catch (FileNotFoundException e) {
			throw new ResourceException("Resource/Configuration file Not Found", resourceName, e);
		}
		catch (NullPointerException e) {
			throw new ResourceException("Unexpected error while loading resource", resourceName, e);
		}
		catch (IOException e) {
			throw new ResourceException("Error loading the resource file", resourceName, e);
		}//try-catch

	}

	public static Properties loadExternalPropertyFile(String fileName) {

		try {
			InputStream in = new FileInputStream(fileName);
			Properties properties = new Properties();
			properties.load(in);
			in.close();
			return properties;
		} catch (FileNotFoundException e) {
			throw new ResourceException("Resource/Configuration file Not Found", fileName, e);
		}
		catch (NullPointerException e) {
			throw new ResourceException("Unexpected error while loading resource", fileName, e);
		}
		catch (IOException e) {
			throw new ResourceException("Error loading the resource file", fileName, e);
		}//try-catch

	}

	public static ImageIcon loadInternalIcon(String iconFilename) {
		URL iconUrl = ClassLoader.getSystemResource(iconFilename);
		return new ImageIcon(iconUrl);
	}

}//class
