package com.cms.utility;

import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;

public class ReadConfig {
	
	public Properties pro ;
	
	public ReadConfig() 
	{
	try {
		File src = new File("C:\\Users\\ATLAS-ADMIN\\eclipse-workspace\\automation-cc\\src\\test\\resources\\Config File\\config.properties");
		FileInputStream fis = new FileInputStream(src);
		pro = new Properties ();
		pro.load(fis);
	}
		catch(Exception e)
		{
			System.out.println("Exception is "+e.getMessage());
		}
		
	}
//	public  String getFile(String filename) throws IOException
//	{
//		Properties prop = new Properties();
//		String p1 = "C:\\Users\\ATLAS-ADMIN\\eclipse-workspace\\AEIS-DMS1\\config.properties" ;
//		FileInputStream fis = new FileInputStream(p1);
//		prop.load(fis);
//		String data = prop.getProperty(filename);
//		return data;
//	}
	 public String getApplicationURL()
	 {
		 String url = pro.getProperty("baseurl");
		 return url;
	 }
      
	 public String getLoginUsername()
	 {
		 String username = pro.getProperty("username");
		 return username;
	 }
	 public String getLoginPassword()
	 {
		 String password = pro.getProperty("password");
		 return password;
	 }
	 
	 
	 
}
