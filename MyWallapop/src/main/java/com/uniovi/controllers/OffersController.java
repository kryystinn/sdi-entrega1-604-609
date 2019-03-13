package com.uniovi.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.services.OffersService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AddOfferValidator;

@Controller
public class OffersController {

	@Autowired
	private OffersService offersService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private AddOfferValidator addOfferValidator;

	// Dar de alta una oferta

	@RequestMapping(value = "/offer/add")
	public String getOffer(Model model) {
		model.addAttribute("offer", new Offer());
		model.addAttribute("usersList", usersService.getUsers());
		return "offer/add";
	}

	@RequestMapping(value = "/offer/add", method = RequestMethod.POST)
	public String setOffer(Model model, @Validated Offer offer, BindingResult result) {
		addOfferValidator.validate(offer, result);

		if (result.hasErrors()) {
			model.addAttribute("usersList", usersService.getUsers());
			return "offer/add";
		}

		offersService.addOffer(offer);
		return "redirect:/offer/list";
	}

	// Mostrar lista de ofertas y actualizarla

	@RequestMapping("/offer/list")
	public String getList(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = usersService.getUserByEmail(email);
		model.addAttribute("offerList", offersService.getOffersForUser(user));
		return "offer/list";
	}

	@RequestMapping("/offer/list/update")
	public String updateList(Model model, Principal principal) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User user = usersService.getUserByEmail(email);
		model.addAttribute("offerList", offersService.getOffersForUser(user));
		return "offer/list :: tableOffers";
	}

	@RequestMapping("/home/update")
	public String updateListOfertas(Model model, Principal principal) {
		// Usuario autenticado
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);

		// Actualizar todas las ofertas menos las propias
		List<Offer> offers = offersService.getAllOffersExcept(user);
		model.addAttribute("offerList", offers);
		return "home :: tableOffers";
	}

	// Dar de baja una oferta

	@RequestMapping("/offer/delete/{id}")
	public String deleteOffer(@PathVariable Long id) {
		offersService.deleteOffer(id);
		return "redirect:/offer/list";
	}

}
