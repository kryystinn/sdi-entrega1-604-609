package com.uniovi.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.repositories.OffersRepository;


@Service
public class OffersService {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private OffersRepository offersRepository;
	
	// Dar de alta nueva oferta
	
	public void addOffer(Offer offer) {
		offersRepository.save(offer);
	}

	public List<Offer> getOffersForUser (User user){
		List<Offer> offers = new ArrayList<Offer>();
		if ( user.getRole().equals("ROLE_USER")) {
			offers = offersRepository.findAllByUser(user);
		}
		return offers;
	}
	
	public List<Offer> getOffers(){
		List<Offer> offers = new ArrayList<Offer>();
		offersRepository.findAll().forEach(offers::add);
		return offers;
		}



}
