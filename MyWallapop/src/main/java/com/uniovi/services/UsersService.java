package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private OffersService offersService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		usersRepository.findAll().forEach(users::add);
		return users;
	}

	public User getUser(Long id) {
		return usersRepository.findById(id).get();
	}

	public void addUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
	}

	// Devuelve true si se puede comprar o false en caso contrario
	public boolean buyOffer(User buyer, Offer oToBuy) {
		if (buyer.getBalance() - oToBuy.getPrice() >= 0) {
			oToBuy.setBought(true); // la marcamos como comprada
			
			oToBuy.setEmailBuyer(buyer.getEmail()); // guardamos el email del comprador
			offersService.updateOffer(oToBuy); // la actualizamos en la base de datos
			
			buyer.setBalance(buyer.getBalance() - oToBuy.getPrice()); // reducimos el dinero del comprador
			oToBuy.getUser().setBalance(oToBuy.getUser().getBalance() + oToBuy.getPrice()); // aumentamos el dinero del vendedor
			
			usersRepository.updateBalance(buyer.getBalance(), buyer.getEmail()); // actualizamos el balance de ambos usuarios en la bdd
			usersRepository.updateBalance(oToBuy.getUser().getBalance(), oToBuy.getUser().getEmail()); 
			return true;
		}
		return false;
	}
	
	public User getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	public void deleteUser(Long id) {
		usersRepository.deleteById(id);
	}
	
}