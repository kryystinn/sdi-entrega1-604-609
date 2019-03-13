package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.repositories.OffersRepository;


@Service
public class OffersService {

	@Autowired
	private OffersRepository offersRepository;
	
	@Autowired
	private UsersService usersService;
	
	public void addOffer(Offer offer) {
		offersRepository.save(offer);
	}
	public List<Offer> getOffersForUser(User user) {
		List<Offer> offers = new ArrayList<Offer>();
		if ( user.getRole().equals("ROLE_USER")) {
			offers = offersRepository.findAllByUser(user);
		}
		return offers;
	}
	public List<Offer> getAllOffersExcept(User user) {
		List<Offer> offers = new ArrayList<Offer>();
		List<User> userOffers = usersService.getUsers();
		for (User u : userOffers)
			if (!u.equals(user))
				offers.addAll(getOffersForUser(u));
		
		return offers;
	}
	public List<Offer> getOffers() {
		List<Offer> offers = new ArrayList<Offer>();
		offersRepository.findAll().forEach(offers::add);
		return offers;
	}
	public void deleteOffer(Long id) {
		offersRepository.deleteById(id);
	}
}
