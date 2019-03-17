package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.Offer;
import com.uniovi.entities.User;
import com.uniovi.services.OffersService;
import com.uniovi.services.RolesService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UsersController {
	
	private static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private OffersService offersService;
	
	@Autowired
	private SignUpFormValidator signUpFormValidator;
	

	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public String getListado(Model model) {
		model.addAttribute("usersList", usersService.getUsersExceptAdmin());
		return "user/list";
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value="checkboxes", required = false) List<String> idList) {
		
		if (idList != null) {
			for (String i : idList) {
				logger.info("El usuario "+usersService.getUser(Long.parseLong(i))+" ha sido borrado del sistema por un administrador.");
				usersService.deleteUser(Long.parseLong(i));
			}
		}
		return "redirect:/user/list";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Validated User user, BindingResult result, Model model) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			logger.warn("Error al intentar registrarse en el sistema.");
			return "signup";
		}
		user.setRole(rolesService.getRoles()[0]);
		usersService.addUser(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		logger.info("Nuevo usuario en el sistema: "+user.getEmail());
		return "redirect:home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping("/home")
	public String home(HttpSession s, Model model, Pageable pageable, Principal principal, @RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> offerList = new PageImpl<Offer>(new LinkedList<Offer>());
		
		if (s.getAttribute("error") != null) {
			model.addAttribute("error", "Error.buy.money");
			s.removeAttribute("error");
		}
		
		if (searchText != null && !searchText.isEmpty())
			offerList = offersService.searchOffersByTitle(pageable, searchText, user);
		else
			offerList = offersService.getOffersToBuy(pageable, user); 
		
		model.addAttribute("offerList", offerList.getContent());
		model.addAttribute("page", offerList);
		return "/home";
	}
	
	@RequestMapping("/home/update")
	public String updateOffersTable(Model model, Pageable pageable, Principal principal) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> offerList = new PageImpl<Offer>(new LinkedList<Offer>());
		offerList = offersService.getOffersToBuy(pageable, user); 
		model.addAttribute("offerList", offerList.getContent());
		
		return "/home :: tableOffers";
	}
}
