package com.uniovi.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.services.OffersService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AddOfferValidator;

@Controller
public class OffersController {

//	@Autowired
//	private HttpSession httpSession;

	@Autowired
	private OffersService offersService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private AddOfferValidator addOfferValidator;


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
//		String email = principal.getName();
//		User user = usersService.getUserByEmail(email);
		model.addAttribute("offerList", offersService.getOffers());
		return "offer/list :: tableOffers";
	}

}