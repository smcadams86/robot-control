package edu.umn.robotcontrol.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesGenerator {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Properties props = new Properties();
		props.put("baseurl", "http://robotcontrolserver.elasticbeanstalk.com/control/control/hello");
		File f = new File("/Users/tyler/Desktop/robotcontrol.properties");
		FileOutputStream fos = new FileOutputStream(f);
		props.store(fos, "Robot Controller Test Properties");
		fos.close();
	}

}
