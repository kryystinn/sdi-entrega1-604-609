package com.uniovi.repositories;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.uniovi.entities.Offer;
import com.uniovi.entities.User;


public interface OffersRepository extends CrudRepository<Offer, Long>{

	@Query("SELECT o FROM Offer o WHERE o.user = ?1 AND o.bought = false ORDER BY o.title ASC")
	Page<Offer> findAllByUser(Pageable pageable, User user);
	
	@Query("SELECT o FROM Offer o WHERE o.user <> ?1 ORDER BY o.id ASC ")
	Page<Offer> findOffersToBuy(Pageable pageable, User user);
	
	@Query("SELECT o FROM Offer o WHERE LOWER(o.title) LIKE LOWER('%'+?1+'%') AND o.user <> ?2 ORDER BY o.id ASC") 
	Page<Offer> searchByTitle(Pageable pageable, String seachtext, User user);
	
	@Query("SELECT o FROM Offer o WHERE o.id LIKE ?1") 
	Offer searchById(Long id);
	
	Page<Offer> findAll(Pageable pageable);
	
	@Query("SELECT o FROM Offer o WHERE o.bought = true AND o.emailBuyer = ?1 ORDER BY o.id ASC") 
	Page<Offer> findPurchasesByUsersEmail(Pageable pageable, String email);
	
	@Modifying
	@Transactional
	@Query("UPDATE Offer o SET o = ?1 WHERE id = ?2")
	void updateOffer(Offer offer, Long id);
}
