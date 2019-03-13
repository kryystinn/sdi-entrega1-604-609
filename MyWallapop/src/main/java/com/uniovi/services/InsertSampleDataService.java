package com.uniovi.services;


import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;
	@Autowired
	private RolesService rolesService;

	@PostConstruct
	public void init() {
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
			add(new Offer("Camiseta", "Negra", "2019/03/01", 12.0, user1));
			add(new Offer("Silla", "Sin una pata", "2019/02/21", 4.0, user1));
			add(new Offer("Teclado", "Gaming", "2019/02/15", 20.0, user1));
			}
			};
			user1.setOffers(user1Offers);

			Set<Offer> user2Offers = new HashSet<Offer>() {
				private static final long serialVersionUID = 1L;

				{
					add(new Offer("Jarra", "De cristal", "2019/03/12", 10.0, user2));
					add(new Offer("Cuadro", "Autorretrato de van Gogh tamaño real", "2019/01/21", 500.0, user2));
					add(new Offer("Riñonera", "Nike, completamente nueva", "2019/03/03", 24.50, user2));
				}
			};
			user2.setOffers(user2Offers);

		
		usersService.addUser(user1);
		usersService.addUser(user2);
		usersService.addUser(user3);
		usersService.addUser(user4);
		usersService.addUser(user5);
		usersService.addUser(user6);
	}
}
