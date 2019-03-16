package com.uniovi.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class MyWallapopTests {
	
	
	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "C:\\Users\\crist\\Documents\\geckodriver024win64.exe";
	
	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8090";
	
	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
	System.setProperty("webdriver.firefox.bin", PathFirefox);
	System.setProperty("webdriver.gecko.driver", Geckdriver);
	WebDriver driver = new FirefoxDriver();
	return driver;
	}
	
	
	
	//Antes de cada prueba se navega a la URL /home de la aplicación.
	@Before
	public void setUp(){
		driver.navigate().to(URL);
	}
	
	//Después de cada prueba se borran las cookies del navegador.
	@After
	public void tearDown(){
		driver.manage().deleteAllCookies();
	}
	
	//Antes de la primera prueba:
	@BeforeClass
	static public void begin() {
	}
	
	//Al finalizar la última prueba:
	@AfterClass
	static public void end() {
		driver.quit();
	}


}
