package com.uniovi.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	private static final Logger logger = LoggerFactory.getLogger(OffersController.class);
	
	@Autowired
	private OffersService offersService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private AddOfferValidator addOfferValidator;

	@RequestMapping("/offer/add")
	public String getOffer(Model model) {
		model.addAttribute("offer", new Offer());
		Date myDate = new Date();
		model.addAttribute("date", new SimpleDateFormat("yyyy-MM-dd").format(myDate));
		return "offer/add";
	}

	@RequestMapping(value = "/offer/add", method = RequestMethod.POST)
	public String setOffer(Model model, Principal principal, @Validated Offer offer, BindingResult result) {
		addOfferValidator.validate(offer, result);
		User user = usersService.getUserByEmail(principal.getName());
		
		if (result.hasErrors()) {
			logger.warn("El usuario "+user.getEmail()+" ha cometido un error al intentar añadir una nueva oferta al sistema.");
			return "offer/add";
		}

		// El usuario que ha creado la oferta es el mismo que está en sesion en el momento de responder la petición de inserción
		offer.setUser(user);
		offersService.addOffer(offer);
		logger.info("El usuario "+user.getEmail()+" ha añadido una nueva oferta al sistema.");
		return "redirect:/offer/list";
	}

	@RequestMapping("/offer/list")
	public String getList(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> offerList = offersService.getUsersNotBoughtOffers(pageable, user);
		model.addAttribute("offerList", offerList.getContent());
		model.addAttribute("page", offerList);
		return "offer/list";
	}

	@RequestMapping("/offer/list/update")
	public String updateList(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> offerList = offersService.getUsersNotBoughtOffers(pageable, user);
		model.addAttribute("offerList", offerList.getContent());
		model.addAttribute("page", offerList);
		return "offer/list :: tableOffers";
	}

	@RequestMapping("/offer/delete/{id}")
	public String deleteOffer(Principal principal, @PathVariable Long id) {
		offersService.deleteOffer(id);
		logger.info("El usuario "+principal.getName()+" ha borrado una de sus ofertas del sistema.");
		return "redirect:/offer/list";
	}
	
	@RequestMapping("/offer/purchases")
	public String getPurchases(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> purchasesList = offersService.getUsersPurchases(pageable, user.getEmail());
		model.addAttribute("purchasesList", purchasesList.getContent());
		model.addAttribute("page", purchasesList);
		return "offer/purchases";
	}
	
	@RequestMapping(value="/offer/{id}/buy", method=RequestMethod.GET)
	public String setBoughtTrue(HttpSession session, Model model, Principal principal, @PathVariable Long id){
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Offer offerToBuy = offersService.searchOfferById(id);
		
		if (usersService.buyOffer(user, offerToBuy))
			session.setAttribute("balance", user.getMoney());	
		else {
			session.setAttribute("error", "Error.buy.money");
			logger.warn("El usuario "+user.getEmail()+" no ha podido comprar la oferta " + offerToBuy.getTitle() +".");
			return "home :: tableOffers";
		}
		logger.info("El usuario "+user.getEmail()+" ha comprado la oferta " + offerToBuy.getTitle() + " correctamente.");
		return "redirect:/home";
	}
}
