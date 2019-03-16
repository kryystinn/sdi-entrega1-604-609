package com.uniovi.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;

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
	
	
	
	
	//PR02. Acceder a la página principal /
	@Test
	public void PR01() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
	}
	
	//PR02. Opción de navegación. Pinchar en el enlace Registro en la página principal.
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
	}
	
	//PR03. Opción de navegación. Pinchar en el enlace Iniciar sesión en la página principal (que debería de ser el mismo).
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
	}
	
	//PR04. Opción de navegación. Cambio de idioma de Español a Ingles y vuelta a Español
	@Test
	public void PR04() {
		PO_HomeView.checkChangeIdiom(driver, "btnSpanish", "btnEnglish",
				PO_Properties.getSPANISH(), PO_Properties.getENGLISH());
		//SeleniumUtils.esperarSegundos(driver, 2);
	}
	
	//PR05. Prueba del formulario de registro. Registro con datos correctos.
	@Test
	public void PR05() {
		//Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		//Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba@gmail.com", "Soy una", "Prueba", "123456",
				"123456");
		//Comprobamos que entramos en home
		PO_View.checkElement(driver, "text", "Ofertas");
	}


}
