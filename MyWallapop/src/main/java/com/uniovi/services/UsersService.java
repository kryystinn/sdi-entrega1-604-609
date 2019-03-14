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
	public boolean buyOffer(User u, Offer oToBuy) {
		if (u.getBalance() - oToBuy.getPrice() >= 0) {
			offersService.setOfferBought(true, oToBuy.getId());
			u.setBalance(u.getBalance() - oToBuy.getPrice());
			usersRepository.updateBalance(u.getBalance(), u.getEmail());
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