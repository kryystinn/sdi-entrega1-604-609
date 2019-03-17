package com.uniovi.tests;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
//acceder a nuestras clases para probarlas
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;
import com.uniovi.services.RolesService;
import com.uniovi.services.UsersService;
import com.uniovi.tests.pageobjects.PO_AddOfferView;
import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_NavView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyWallapopTests {

	@Autowired
	private UsersService usersService;
	@Autowired
	private RolesService rolesService;
	@Autowired
	private UsersRepository usersRepository;

	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "C:\\Users\\Iván\\Desktop\\SDI\\materialPruebas\\geckodriver024win64.exe";
	//static String Geckdriver024 = "C:\\Users\\crist\\Documents\\geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8090";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega a la URL /home de la aplicación y se cargan los datos
	@Before
	public void setUp() {
		initdb();
		driver.navigate().to(URL);
	}
	
	//auxiliar de initdb
	private void buyOffer(User buyer, Offer oToBuy) {
		if (buyer.getBalance() - oToBuy.getPrice() >= 0) {
			oToBuy.setBought(true); // la marcamos como comprada
			oToBuy.setEmailBuyer(buyer.getEmail()); // guardamos el email del comprador
			buyer.setBalance(buyer.getBalance() - oToBuy.getPrice()); // reducimos el dinero del comprador
			oToBuy.getUser().setBalance(oToBuy.getUser().getBalance() + oToBuy.getPrice()); // aumentamos el dinero del vendedor
		}
	}
	
	// Ejecutar antes de cada prueba para que este en un estado determinista
	public void initdb() {
		
		//Borramos todas las entidades.
		usersRepository.deleteAll();
		
		//Ahora las volvemos a crear
		User user1 = new User("pedro@gmail.com", "Pedro", "Díaz");
		user1.setPassword("123456");
		user1.setRole(rolesService.getRoles()[0]);
		User user2 = new User("lucas@gmail.com", "Lucas", "Núñez");
		user2.setPassword("123456");
		user2.setRole(rolesService.getRoles()[0]);
		User user3 = new User("maria@gmail.com", "María", "Rodríguez");
		user3.setPassword("123456");
		user3.setRole(rolesService.getRoles()[0]);
		User user4 = new User("marta@gmail.com", "Marta", "Almonte");
		user4.setPassword("123456");
		user4.setRole(rolesService.getRoles()[0]);
		User user5 = new User("pelayo@gmail.com", "Pelayo", "Valdes");
		user5.setPassword("123456");
		user5.setRole(rolesService.getRoles()[0]);
		User user6 = new User("admin@email.com", "admin", "admin");
		user6.setPassword("admin");
		user6.setRole(rolesService.getRoles()[2]);
		
		Set<Offer> user1Offers = new HashSet<Offer>() {
			private static final long serialVersionUID = 1L;

			{
				Offer offer1 = new Offer("Camiseta", "Negra", "2019-03-01", 12.0, user1);
				buyOffer(user2, offer1);
				Offer offer2 = new Offer("Silla", "Sin una pata", "2019-02-21", 4.0, user1);
				buyOffer(user2, offer2);
				Offer offer3 = new Offer("Teclado", "Gaming", "2019-02-15", 20.0, user1);
				buyOffer(user5, offer3);
				add(offer1);
				add(offer2);
				add(offer3);
				add(new Offer("Teclado básico", "No-Gaming", "2019-03-13", 20.0, user1));
				add(new Offer("Funda móvil", "Sin abrir, nueva", "2019-05-30", 20.0, user1));
			}
		};
		user1.setOffers(user1Offers);

		Set<Offer> user2Offers = new HashSet<Offer>() {
			private static final long serialVersionUID = 1L;

			{
				Offer offer1 = new Offer("Jarra", "De cristal", "2019-03-12", 10.0, user2);
				buyOffer(user1, offer1);
				Offer offer2 = new Offer("Funda móvil", "Sin abrir, nueva", "2019-05-30", 20.0, user2);
				buyOffer(user1, offer2);
				Offer offer3 = new Offer("Riñonera", "Nike, completamente nueva", "2019-03-03", 24.50, user2);
				buyOffer(user5, offer3);
				add(offer1);
				add(offer2);
				add(offer3);
				add(new Offer("Cuadro", "Autorretrato de van Gogh tamaño real, pero es el original...", "2019-01-21", 83.0, user2));
				add(new Offer("Teclado con luces", "Gaming", "2019-04-20", 20.0, user2));
			}
		};
		user2.setOffers(user2Offers);
		
		Set<Offer> user3Offers = new HashSet<Offer>() {
			private static final long serialVersionUID = 1L;

			{
				Offer offer1 = new Offer("Perfume", "Black Opium de YVS, 90ml, prescintado de tienda.", "2019-03-13", 50.0, user3);
				buyOffer(user4, offer1);
				Offer offer2 = new Offer("Chaqueta vaquera", "La vendo porque no la uso. Usada dos veces.", "2018-12-14", 18.0, user3);
				buyOffer(user4, offer2);
				add(offer1);
				add(offer2);
				add(new Offer("Coche Opel Corsa", "ITV hasta el verano pasado, 150.000 km", "2019-01-12", 1500.0, user3));
			}
		};
		user3.setOffers(user3Offers);
		
		
		Set<Offer> user4Offers = new HashSet<Offer>() {
			private static final long serialVersionUID = 1L;

			{
				Offer offer1 = new Offer("Set de platos", "De cerámica, en buen estado", "2019-02-12", 65.0, user4);
				buyOffer(user3, offer1);
				Offer offer2 = new Offer("Set de maquillaje", "Fue un regalo de Reyes que no me ha gustado.", "2019-01-20", 20.0, user4);
				buyOffer(user3, offer2);
				add(offer1);
				add(offer2);
				add(new Offer("Set de toallas", "De IKEA.", "2019-01-20", 15.0, user4));
			}
		};
		user4.setOffers(user4Offers);
		
		Set<Offer> user5Offers = new HashSet<Offer>() {
			private static final long serialVersionUID = 1L;

			{
				add(new Offer("Cascos con micro", "De conector USB, funciona 10/10", "2019-03-08", 25.50, user5));
				add(new Offer("Libro", "Luna de Plutón by Dross", "2019-01-10", 14.0, user5));
				add(new Offer("Teclado", "Mecánico, le falta la ñ pero está chulo.", "2019-02-15", 10.0, user5));
			}
		};
		user5.setOffers(user5Offers);
		
		usersService.addUser(user1);
		usersService.addUser(user2);
		usersService.addUser(user3);
		usersService.addUser(user4);
		usersService.addUser(user5);
		usersService.addUser(user6);
	}

	// Después de cada prueba se borran las cookies del navegador.
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba:
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba:
	@AfterClass
	static public void end() {
		driver.quit();
	}

	// PR01. Registro de Usuario con datos válidos.
	@Test
	public void PR01() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba@gmail.com", "Soy una", "Prueba", "123456", "123456");
		// Comprobamos que entramos en home (un elemento especifico de home para Users como Ofertas)
		PO_View.checkElement(driver, "text", "Ofertas");
	}
	
	// PR02. Registro de usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
	@Test
	public void PR02() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		
		// Rellenamos el formulario
		PO_RegisterView.fillForm(driver, " ", "Jose", "Perez Vazquez", "123456", "123456");
		// Comprobamos el error de email vacío
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
		
		// Rellenamos el formulario de nuevo
		PO_RegisterView.fillForm(driver, "jose@email.com", " ", "Perez Vazquez", "123456", "123456");
		// Comprobamos el error de nombre vacío
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
		
		// Rellenamos el formulario de nuevo
		PO_RegisterView.fillForm(driver, "jose@email.com", "Jose", " ", "123456", "123456");
		// Comprobamos el error de apellidos vacíos
		PO_RegisterView.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
		
	}



	// PR03. Registro de usuario con datos inválidos (repetición de contraseña inválida).
	@Test
	public void PR03() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_RegisterView.fillForm(driver, "pedro@gmail.com", "Pedro", "Diaz Atapuerca", "123456", "1234567");
		PO_View.getP();
		// Comprobamos el error de no coincidencia de contraseñas
		PO_RegisterView.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH());
	}

	// PR04. Registro de usuario con datos inválidos (email existente).
	@Test
	public void PR04() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_RegisterView.fillForm(driver, "pedro@gmail.com", "Pedro", "Diaz Atapuerca", "123456", "123456");
		// Comprobamos el error de email repetido
		PO_RegisterView.checkKey(driver, "Error.signup.email.duplicate", PO_Properties.getSPANISH());
	}

	// PR05. Inicio de sesión con datos válidos (administrador).
	@Test
	public void PR05() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta administrador
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que tenemos la funcion de gestionar usuarios, exclusiva del admin
		PO_View.checkElement(driver, "text", "Gestionar Usuarios");
	}
	
	// PR06. Inicio de sesión con datos válidos (usuario estándar).
	@Test
	public void PR06() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "lucas@gmail.com", "123456");
		// Comprobamos que tenemos la opcion de ver las compras realizadas por el usuario
		PO_View.checkElement(driver, "text", "Mis compras");
	}
	
	// PR07. Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos).
	@Test
	public void PR07() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "a", "123456");
		// Comprobamos que lanza un error al estar vacio el email
		PO_RegisterView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());
		// Rellenamos el formulario de nuevo
		PO_LoginView.fillForm(driver, "lucas@gmail.com", "a");
		// Comprobamos que lanza un error al estar vacia la contraseña
		PO_LoginView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());
	}
	
	// PR08. Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña incorrecta).
	@Test
	public void PR08() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "lucas@gmail.com", "123");
		// Comprobamos que lanza un error al estar vacio el email
		PO_LoginView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());
	}
	
	// PR09. Inicio de sesión con datos inválidos (usuario estándar, email no existente en la aplicación).
	@Test
	public void PR09() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, "inventao@gmail.com", "123456");
		// Comprobamos que lanza un error al estar vacio el email
		PO_LoginView.checkKey(driver, "Error.login", PO_Properties.getSPANISH());
	}
	
	// PR10. Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio 
	// de sesión (Login).
	@Test
	public void PR10() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "lucas@gmail.com", "123456");
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
		// Comprobamos que nos dirige a /login
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// PR11. Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
	@Test
	public void PR11() {
		// Comprobamos que no hay ningún elemento cuyo href sea /logout
		Boolean resultado = (new WebDriverWait(driver, PO_NavView.getTimeout()).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(@href,'" + "/logout" + "')]"))));
		assertTrue(resultado);
	}
	
	// PR12. Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
	// sistema.
	@Test
	public void PR12() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta administrador
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que entramos en la pagina privada del admin
		PO_View.checkElement(driver, "text", "Gestionar Usuarios");
		// Hacemos click en la opción Ver Usuarios
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Contamos el número de filas de la tabla de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR13. Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
	// y dicho usuario desaparece.
	@Test
	public void PR13() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta administrador:
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que entramos en la pagina privada del admin:
		PO_View.checkElement(driver, "text", "Gestionar Usuarios");
		// Hacemos click en la opción Ver Usuarios
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Contamos el número de filas de la tabla de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		// Comprobamos que al principio el email del primer usuario de la lista se encuentra presente:
		SeleniumUtils.textoPresentePagina(driver, "pedro@gmail.com");
		
		// Marcamos el checkbox del primer usuario de la lista, que se corresponde con Pedro:
		driver.findElement(By.id("cbpedro@gmail.com")).click();
		// Eliminamos este usuario marcado:
		driver.findElement(By.id("deleteButton")).click();
		
		//Comprobamos que hay una fila menos de la tabla de usuarios:
		List<WebElement> elementosUpdated = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosUpdated.size() == 4);
		
		//Comprobamos que Pedro no se encuentra entre la lista de usuarios:
		SeleniumUtils.textoNoPresentePagina(driver, "pedro@gmail.com");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR14. Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
	// y dicho usuario desaparece.
	@Test
	public void PR14() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta administrador:
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que entramos en la pagina privada del admin:
		PO_View.checkElement(driver, "text", "Gestionar Usuarios");
		// Hacemos click en la opción Ver Usuarios
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Contamos el número de filas de la tabla de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		//Comprobamos que al principio el email del último usuario de la lista está presente:
		SeleniumUtils.textoPresentePagina(driver, "pelayo@gmail.com");
		
		// Marcamos el checkbox del último usuario de la lista, que se corresponde con Pelayo:
		driver.findElement(By.id("cbpelayo@gmail.com")).click();
		
		// Eliminamos este usuario marcado:
		driver.findElement(By.id("deleteButton")).click();
		
		//Comprobamos que hay una fila menos de la tabla de usuarios:
		List<WebElement> elementosUpdated = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosUpdated.size() == 4);
		
		//Comprobamos que Pelayo no se encuentra entre la lista de usuarios:
		SeleniumUtils.textoNoPresentePagina(driver, "pelayo@gmail.com");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR15. Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
	// usuarios desaparecen.
	@Test
	public void PR15() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta administrador:
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que entramos en la pagina privada del admin:
		PO_View.checkElement(driver, "text", "Gestionar Usuarios");
		// Hacemos click en la opción Ver Usuarios
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Contamos el número de filas de la tabla de usuarios
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		//Comprobamos que al principio el email de los 3 usuarios están presentes:
		SeleniumUtils.textoPresentePagina(driver, "lucas@gmail.com");
		SeleniumUtils.textoPresentePagina(driver, "maria@gmail.com");
		SeleniumUtils.textoPresentePagina(driver, "marta@gmail.com");
		
		
		// Marcamos el checkbox de los usuarios, que se corresponden con Lucas, María y Marta:
		driver.findElement(By.id("cblucas@gmail.com")).click();
		driver.findElement(By.id("cbmaria@gmail.com")).click();
		driver.findElement(By.id("cbmarta@gmail.com")).click();
		
		// Eliminamos los usuarios marcados:
		driver.findElement(By.id("deleteButton")).click();
		
		//Comprobamos que hay 3 filas menos de la tabla de usuarios:
		List<WebElement> elementosUpdated = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementosUpdated.size() == 2);
		
		//Comprobamos que Lucas, María y Marta no se encuentran entre la lista de usuarios:
		SeleniumUtils.textoNoPresentePagina(driver, "lucas@gmail.com");
		SeleniumUtils.textoNoPresentePagina(driver, "maria@gmail.com");
		SeleniumUtils.textoNoPresentePagina(driver, "marta@gmail.com");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR16. Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el botón Submit.
	// Comprobar que la oferta sale en el listado de ofertas de dicho usuario.
	@Test
	public void PR16() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta estándar:
		PO_LoginView.fillForm(driver, "marta@gmail.com", "123456");
		// Comprobamos que entramos en la página principal del user (home):
		PO_View.checkElement(driver, "text", "Ofertas");
		// Hacemos click en la opción Añadir oferta:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "addOffer");
		// Comprobamos que entramos en la página de añadir una oferta:
		PO_View.checkElement(driver, "text", "Añadir oferta");
		
		// Rellenamos el formulario con datos válidos:
		PO_AddOfferView.fillForm(driver, "Chancla de goma", "Perdí la otra, por eso la vendo", "2019-04-22", "5.0");

		// Comprobamos que al pulsar el botón nos redirige a Tus ofertas:
		PO_View.checkElement(driver, "text", "Tus ofertas");
		
		// Comprobamos que la oferta que acabamos de crear se encuentra entre las ofertas de Marta:
		// Inicialmente tenía una sola oferta a la venta, ahora debería de tener 2:
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertas.size() == 2);
		// Comprobamos que la chancla se halla entre ellas:
		SeleniumUtils.textoPresentePagina(driver, "Chancla de goma");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR17. Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo título vacío) y pulsar
	// el botón Submit. Comprobar que se muestra el mensaje de campo obligatorio
	@Test
	public void PR17() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta estándar:
		PO_LoginView.fillForm(driver, "marta@gmail.com", "123456");
		// Comprobamos que entramos en la página principal del user (home):
		PO_View.checkElement(driver, "text", "Ofertas");
		// Hacemos click en la opción Añadir oferta:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "addOffer");
		// Comprobamos que entramos en la página de añadir una oferta:
		PO_View.checkElement(driver, "text", "Añadir oferta");
		
		// Rellenamos el formulario con datos no válidos:
		PO_AddOfferView.fillForm(driver, " ", "Vendo batidora", "2019-04-22", "25.0");
		PO_View.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
		
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR18. Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas los que
	// existen para este usuario. 
	@Test
	public void PR18() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta estándar:
		PO_LoginView.fillForm(driver, "pedro@gmail.com", "123456");
		// Comprobamos que entramos en la página principal del user (home):
		PO_View.checkElement(driver, "text", "Ofertas");
		// Hacemos click en la opción Tus Ofertas:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "offersList");
		// Comprobamos que entramos en la página de Tus Ofertas:
		PO_View.checkElement(driver, "text", "Tus ofertas");
		// Inicialmente Pedro tenía dos ofertas a la venta, la funda móvil y el teclado básico, por lo que:
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertas.size() == 2);
		
		// Comprobamos las ofertas son las mencionadas:
		SeleniumUtils.textoPresentePagina(driver, "Funda móvil");
		SeleniumUtils.textoPresentePagina(driver, "Teclado básico");
		
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	
	// PR19. Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar que la lista se actualiza y
	// que la oferta desaparece.
	@Test
	public void PR19() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta estándar:
		PO_LoginView.fillForm(driver, "pelayo@gmail.com", "123456");
		// Comprobamos que entramos en la página principal del user (home):
		PO_View.checkElement(driver, "text", "Ofertas");
		// Hacemos click en la opción Tus Ofertas:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "offersList");
		// Comprobamos que entramos en la página de Tus Ofertas:
		PO_View.checkElement(driver, "text", "Tus ofertas");
		// Inicialmente Pelayo tenía 3 ofertas:
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertas.size() == 3);
		
		// Eliminamos la primera oferta:
		// Que se corresponde con unos cascos:
		SeleniumUtils.textoPresentePagina(driver, "Cascos con micro");
		driver.findElement(By.id("delCascos con micro")).click();
		
		// Comprobamos que disminuye una oferta en la lista:
		List<WebElement> ofertasUpdate = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertasUpdate.size() == 2);
		// Comprobamos que era el elemento que eliminamos:
		SeleniumUtils.textoNoPresentePagina(driver, "Cascos con micro");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}
	
	// PR20. Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar que la lista se actualiza y
	// que la oferta desaparece.
	@Test
	public void PR20() {
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta estándar:
		PO_LoginView.fillForm(driver, "pelayo@gmail.com", "123456");
		// Comprobamos que entramos en la página principal del user (home):
		PO_View.checkElement(driver, "text", "Ofertas");
		// Hacemos click en la opción Tus Ofertas:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "offersList");
		// Comprobamos que entramos en la página de Tus Ofertas:
		PO_View.checkElement(driver, "text", "Tus ofertas");
		// Inicialmente Pelayo tenía 3 ofertas:
		List<WebElement> ofertas = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertas.size() == 3);
		
		// Eliminamos la última oferta:
		// Que se corresponde con un teclado:
		SeleniumUtils.textoPresentePagina(driver, "Libro");
		driver.findElement(By.id("delTeclado")).click();
		
		// Comprobamos que disminuye una oferta en la lista:
		List<WebElement> ofertasUpdate = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(ofertasUpdate.size() == 2);
		// Comprobamos que era el elemento que eliminamos:
		SeleniumUtils.textoNoPresentePagina(driver, "Teclado");
		
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
	}

	
	// PR21. Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
	// corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR21() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "maria@gmail.com", "123456");
		// Hacemos click en el boton search
		By boton = By.className("btn");
		driver.findElement(boton).click();
		
		/**
		 * Comprobamos que están todas las ofertas del sistema (son 19 menos las 3 de María que no se muestran 16)
		 */
		int numberOfOffers = 0;
		// Primera página
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		numberOfOffers += elementos.size();
		// Recorremos las página 2, 3 y 4 de la lista
		for (int i = 2; i<5; i++) {
			// Esperamos a que se muestren los enlaces de paginación
			elementos = PO_View.checkElement(driver, "free", "//a[contains(@class, 'page-link')]");
			elementos.get(i).click();
			elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
					PO_View.getTimeout());
			numberOfOffers += elementos.size();
		}
		assertTrue(numberOfOffers == 16);
	}
	
	// PR22. Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
	// muestra la página que corresponde, con la lista de ofertas vacía.
	@Test
	public void PR22() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "maria@gmail.com", "123456");
		// Introducimos una cadena sin coincidencias en el campo de busqueda
		WebElement searchInput = driver.findElement(By.name("searchText"));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys("cadena imposible de hacer coincidir");
		// Hacemos click en el boton search
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobamos que la tabla está vacía
		assertTrue(driver.findElements(By.xpath("//table[@id='tableOffers']/tbody/tr")).size() == 0);
	}
	
	// PR23. Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que deja
	// un saldo positivo en el contador del comprador. Y comprobar que el contador se actualiza
	// correctamente en la vista del comprador.
	@Test
	public void PR23() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "maria@gmail.com", "123456");
		
		// Introducimos una cadena en el campo de busqueda. Nos deberían de salir 2 ofertas, una vendida y otra no.
		WebElement searchInput = driver.findElement(By.name("searchText"));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys("si");
		// Hacemos click en el boton search
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobamos que la tabla tiene efectivamente 2 ofertas
		assertTrue(driver.findElements(By.xpath("//table[@id='tableOffers']/tbody/tr")).size() == 2);
		// Compramos la segunda oferta 'Teclado básico' (la no vendida)
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Comprar");
		elementos.get(0).click();
		
		// Hacemos click en la opción del principal.username
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "btnGroup", PO_NavView.getTimeout());
		elementos.get(0).click();
		// Esperamos a que aparezca el menú de opciones
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "usersdropdownMenuButton", PO_NavView.getTimeout());
		// Comprobamos que el contador se actualiza correctamente (83€-20€=63€)
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "balance", PO_NavView.getTimeout());
		assertTrue(elementos.get(0).getText().equals("63.0€"));
		
	}
	
	// PR24. Sobre una búsqueda determinada (a elección de desarrollador), comprar una oferta que deja
	// un saldo 0 en el contador del comprobador. Y comprobar que el contador se actualiza correctamente en
	// la vista del comprador.
	@Test
	public void PR24() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "maria@gmail.com", "123456");
		
		// Introducimos una cadena en el campo de busqueda, en este caso queremos un producto especifico
		WebElement searchInput = driver.findElement(By.name("searchText"));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys("Cuadro");
		// Hacemos click en el boton search
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobamos que solo sale 1 oferta
		assertTrue(driver.findElements(By.xpath("//table[@id='tableOffers']/tbody/tr")).size() == 1);
		// La compramos
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Comprar");
		elementos.get(0).click();
		
		// Hacemos click en la opción del principal.username
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "btnGroup", PO_NavView.getTimeout());
		elementos.get(0).click();
		// Esperamos a que aparezca el menú de opciones
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "usersdropdownMenuButton", PO_NavView.getTimeout());
		// Comprobamos que el contador a 0
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "id", "balance", PO_NavView.getTimeout());
		assertTrue(elementos.get(0).getText().equals("0.0€"));
	}
	
	// PR25. Sobre una búsqueda determinada (a elección de desarrollador), intentar comprar una oferta
	// que esté por encima de saldo disponible del comprador. Y comprobar que se muestra el mensaje de
	// saldo no suficiente.
	@Test
	public void PR25() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "pedro@gmail.com", "123456");
		
		// Introducimos una cadena en el campo de busqueda, en este caso queremos un producto especifico
		WebElement searchInput = driver.findElement(By.name("searchText"));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys("Coche");
		// Hacemos click en el boton search
		By boton = By.className("btn");
		driver.findElement(boton).click();
		// Comprobamos que solo sale 1 oferta
		assertTrue(driver.findElements(By.xpath("//table[@id='tableOffers']/tbody/tr")).size() == 1);
		// La compramos
		List<WebElement> elementos = PO_View.checkElement(driver, "text", "Comprar");
		elementos.get(0).click();
		// Nos sale un error
		PO_LoginView.checkKey(driver, "Error.buy.money", PO_Properties.getSPANISH());
	}
	
	// PR26. Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar que aparecen
	// las ofertas que deben aparecer.	
	@Test
	public void PR26() {
		/**
		 * Parte 1 - Comprobar q aparecen en mis compras los objetos comprados (2 por defecto en maria)
		 */
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "maria@gmail.com", "123456");
		// Hacemos click en Mis Ofertas
		PO_HomeView.clickOption(driver, "offer/purchases", "id", "purchases");
		// Contamos el número de filas de la lista de compras
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 2);
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
		
		/**
		 * Parte 2 - Comprobar que ya no aparecen en Mis Ofertas los objetos comprados (en este caso marta)
		 */
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "marta@gmail.com", "123456");
		// Hacemos click en Tus Ofertas
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "offersList");
		// Comprobamos que ya no las puede ver (marta al inicializar la bbdd, tiene 3 ofertas, 2 compradas y 1 en venta)
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}
	
	// PR27. Visualizar al menos cuatro páginas en Español/Inglés/Español (comprobando que algunas
	// de las etiquetas cambian al idioma correspondiente). Página principal/Opciones Principales de
	// Usuario/Listado de Usuarios de Admin/Vista de alta de Oferta.
	@Test
	public void PR27() {
		/*
		 * Parte 1: Página principal: home con usuario estándar
		 */
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "pedro@gmail.com", "123456");
		// Comprobamos que estamos en la página principal:
		SeleniumUtils.textoPresentePagina(driver, "Ofertas");
		// Cambiamos el idioma a inglés
		PO_NavView.changeIdiom(driver, "btnEnglish");
		// Comprobamos que es inglés:
		SeleniumUtils.textoPresentePagina(driver, "Offers");
		// Volvemos a ponerlo en español y comprobamos:
		PO_NavView.changeIdiom(driver, "btnSpanish");
		// Comprobamos que esté en español
		SeleniumUtils.textoPresentePagina(driver, "Ofertas");
		
		/*
		 * Parte 2: Opciones principales del usuario: vista de Mis Compras
		 */
		// Vamos a la ventana de mis compras:
		driver.findElement(By.id("purchases")).click();
		// Comprobamos que está en español:
		SeleniumUtils.textoPresentePagina(driver, "Mis compras");
		// Cambiamos el idioma a inglés
		PO_NavView.changeIdiom(driver, "btnEnglish");
		// Comprobamos que es inglés:
		SeleniumUtils.textoPresentePagina(driver, "My purchases");
		// Volvemos a ponerlo en español y comprobamos:
		PO_NavView.changeIdiom(driver, "btnSpanish");
		// Comprobamos que esté en español
		SeleniumUtils.textoPresentePagina(driver, "Mis compras");
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
		
		/*
		 * Parte 3: Lista de Usuarios como admin
		 */
		// Vamos al formulario de login:
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con la cuenta de admin:
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		// Comprobamos que estamos en la página principal:
		SeleniumUtils.textoPresentePagina(driver, "Bienvenido a myWallapop");
		// Vamos a la página del listado de usuarios:
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Comprobamos que es español:
		SeleniumUtils.textoPresentePagina(driver, "Usuarios registrados del sistema");
		// Vamos a home y cambiamos el idioma a inglés
		driver.findElement(By.id("home")).click();
		PO_NavView.changeIdiom(driver, "btnEnglish");
		// Vamos al listado de nuevo y comprobamos que está en inglés:
		PO_NavView.clickDropdownMenuOption(driver, "btnUserManagement", "admin-user-menu", "seeUsers");
		// Comprobamos que es español:
		SeleniumUtils.textoPresentePagina(driver, "Registered system users");
		// Nos desconectamos
		PO_NavView.clickDropdownMenuOption(driver, "btnGroup", "usersdropdownMenuButton", "btnLogout");
		
		/*
		 * Parte 3: Vista de alta de una oferta
		 */
		PO_NavView.changeIdiom(driver, "btnSpanish");
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "pedro@gmail.com", "123456");
		// Comprobamos que estamos en la página principal:
		SeleniumUtils.textoPresentePagina(driver, "Ofertas");
		// Vamos a la pantalla de añadir una oferta:
		PO_NavView.clickDropdownMenuOption(driver, "btnOffersManagement", "offersDropdownMenu", "addOffer");
		// Comprobamos que está en español:
		SeleniumUtils.textoPresentePagina(driver, "Añadir oferta");
		// Cambiamos el idioma a inglés
		PO_NavView.changeIdiom(driver, "btnEnglish");
		// Comprobamos que está en inglés:
		SeleniumUtils.textoPresentePagina(driver, "Add offer");
		// Volvemos a español y comprobamos:
		PO_NavView.changeIdiom(driver, "btnSpanish");
		// Comprobamos que está en inglés:
		SeleniumUtils.textoPresentePagina(driver, "Precio");
	}
	
	
	// PR28. Intentar acceder sin estar autenticado a la opción de listado de usuarios del administrador. Se
	// deberá volver al formulario de login.
	@Test
	public void PR28() {
		// Intentamos acceder sin estar logeados:
		driver.get(URL + "/user/list");
		// Comprobamos que nos devuelve a login:
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// PR29. Intentar acceder sin estar autenticado a la opción de listado de ofertas propias de un usuario
	// estándar. Se deberá volver al formulario de login.
	@Test
	public void PR29() {
		// Intentamos acceder sin estar logeados:
		driver.get(URL + "/offer/list");
		// Comprobamos que nos devuelve a login:
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// PR30. Estando autenticado como usuario estándar intentar acceder a la opción de listado de
	// usuarios del administrador. Se deberá indicar un mensaje de acción prohibida.
	@Test
	public void PR30() {
		// Vamos al formulario de login.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario con una cuenta de usuario
		PO_LoginView.fillForm(driver, "pedro@gmail.com", "123456");
		// Comprobamos que estamos en la página principal:
		SeleniumUtils.textoPresentePagina(driver, "Ofertas");
		// Intentamos acceder sin tener permisos:
		driver.get(URL + "/user/list");
		// Comprobamos que nos da un error:
		SeleniumUtils.textoPresentePagina(driver, "HTTP Status 403 – Forbidden");
	}
	
	// PR39. Registro de usuario con datos inválidos (longitud de email, nombre y apellidos incorrecta).
	@Test
	public void PR39() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_RegisterView.fillForm(driver, "ola", "Jose", "Perez Vazquez", "123456", "123456");
		// Comprobamos el error de email corto
		PO_RegisterView.checkKey(driver, "Error.signup.email.length", PO_Properties.getSPANISH());
		
		// Rellenamos el formulario de nuevo
		PO_RegisterView.fillForm(driver, "jose@email.com", "c", "Perez Vazquez", "123456", "123456");
		// Comprobamos el error de nombre corto
		PO_RegisterView.checkKey(driver, "Error.signup.name.length", PO_Properties.getSPANISH());
		
		// Rellenamos el formulario de nuevo
		PO_RegisterView.fillForm(driver, "jose@email.com", "Jose", "P", "123456", "123456");
		// Comprobamos el error de apellidos cortos
		PO_RegisterView.checkKey(driver, "Error.signup.lastName.length", PO_Properties.getSPANISH());
	}
}
