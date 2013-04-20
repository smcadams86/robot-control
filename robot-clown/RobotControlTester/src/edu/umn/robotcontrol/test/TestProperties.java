package edu.umn.robotcontrol.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class TestProperties {

	static String baseurl = "http://localhost/RobotControlServer";
	static{
	    File propsFile;
		try {
			URL propsURL = ClassLoader.getSystemResource("robotcontrol.properties");
			propsFile = new File(propsURL.toURI());
			FileInputStream fis = new FileInputStream(propsFile);
			Properties props = new Properties();
			props.load(fis);
			fis.close();
			baseurl =  props.getProperty("baseurl");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
