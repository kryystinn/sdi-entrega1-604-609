package com.uniovi.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.uniovi.entities.User;

public interface UsersRepository extends CrudRepository<User, Long> {
	User findByEmail(String dni);
	
	@Modifying
	@Transactional
	@Query("UPDATE User SET balance = ?1 WHERE email = ?2")
	void updateBalance(double balance, String email);
}