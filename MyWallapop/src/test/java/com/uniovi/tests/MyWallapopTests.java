package com.uniovi.tests;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;

//acceder a nuestras clases para probarlas
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.services.RolesService;
import com.uniovi.services.UsersService;
import com.uniovi.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;

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
				add(new Offer("Cuadro", "Autorretrato de van Gogh tamaño real", "2019-01-21", 500.0, user2));
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

	// PR01. Acceder a la página principal /
	@Test
	public void PR01() {
		PO_HomeView.checkWelcome(driver, PO_Properties.getSPANISH());
	}

	// PR02. Opción de navegación. Pinchar en el enlace Registro en la página
	// principal.
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
	}

	// PR03. Opción de navegación. Pinchar en el enlace Iniciar sesión en la página
	// principal (que debería de ser el mismo).
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
	}

	// PR04. Opción de navegación. Cambio de idioma de Español a Ingles y vuelta a
	// Español
	@Test
	public void PR04() {
		PO_HomeView.checkChangeIdiom(driver, "btnSpanish", "btnEnglish", PO_Properties.getSPANISH(),
				PO_Properties.getENGLISH());
		// SeleniumUtils.esperarSegundos(driver, 2);
	}

	// PR05. Prueba del formulario de registro. Registro con datos correctos.
	@Test
	public void PR05() {
		// Vamos al formulario de registro
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		// Rellenamos el formulario.
		PO_RegisterView.fillForm(driver, "prueba@gmail.com", "Soy una", "Prueba", "123456", "123456");
		// Comprobamos que entramos en home
		PO_View.checkElement(driver, "text", "Ofertas");
	}

}
