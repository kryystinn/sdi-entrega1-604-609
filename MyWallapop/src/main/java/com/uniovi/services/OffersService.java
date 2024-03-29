package com.uniovi.services;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.repositories.OffersRepository;

@Service
public class OffersService {

	@Autowired
	private OffersRepository offersRepository;

	public void addOffer(Offer offer) {
		offersRepository.save(offer);
	}

	public Page<Offer> getUsersNotBoughtOffers(Pageable pageable, User user) {
		Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
		offers = offersRepository.findAllByUser(pageable, user);
		return offers;
	}

	public Page<Offer> searchOffersByTitle(Pageable pageable, String searchText, User user) {
		Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
		offers = offersRepository.searchByTitle(pageable, searchText, user);
		return offers;
	}

	public Page<Offer> getOffers(Pageable pageable) {
		Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
		offers = offersRepository.findAll(pageable);
		return offers;
	}
	
	public Page<Offer> getOffersToBuy(Pageable pageable, User user) {
		Page<Offer> offers = new PageImpl<Offer>(new LinkedList<Offer>());
		offers = offersRepository.findOffersToBuy(pageable, user);
		return offers;
	}

	public void deleteOffer(Long id) {
		offersRepository.deleteById(id);
	}
	
	
	public void updateOffer(Offer offer) {
		offersRepository.updateOffer(offer, offer.getId());
	}
	
	public Offer searchOfferById(Long id) {
		Offer offer = offersRepository.searchById(id);
		return offer;
	}
	
	public Page<Offer> getUsersPurchases(Pageable pageable, String email) {
		Page<Offer> purchases = new PageImpl<Offer>(new LinkedList<Offer>());
		purchases = offersRepository.findPurchasesByUsersEmail(pageable, email);
		return purchases;
	}
}
