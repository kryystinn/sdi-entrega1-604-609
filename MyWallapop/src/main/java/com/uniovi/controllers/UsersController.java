package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.uniovi.entities.User;
import com.uniovi.entities.Offer;
import com.uniovi.services.OffersService;
import com.uniovi.services.RolesService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UsersController {
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
		model.addAttribute("usersList", usersService.getUsers());
		return "user/list";
	}

	@RequestMapping(value = "/user/add")
	public String getUser(Model model) {
		model.addAttribute("rolesList", rolesService.getRoles());
		return "user/add";
	}

	@RequestMapping(value = "/user/add", method = RequestMethod.POST)
	public String setUser(@ModelAttribute User user) {
		usersService.addUser(user);
		return "redirect:/user/list";
	}

	@RequestMapping("/user/details/{id}")
	public String getDetail(Model model, @PathVariable Long id) {
		model.addAttribute("user", usersService.getUser(id));
		return "user/details";
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value="checkboxes", required = false) List<String> idList) {
		
		if (idList.size() != 0) {
			for (String i : idList) {
				usersService.deleteUser(Long.parseLong(i));
			}
		}
		return "redirect:/user/list";
	}

	@RequestMapping(value = "/user/edit")
	public String getEdit(Model model, @PathVariable Long id) {
		User user = usersService.getUser(id);
		model.addAttribute("user", user);
		return "user/edit";
	}

	@RequestMapping(value = "/user/edit/{id}", method = RequestMethod.POST)
	public String setEdit(Model model, @PathVariable Long id, @ModelAttribute User user) {
		user.setId(id);
		usersService.addUser(user);
		return "redirect:/user/details/" + id;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Validated User user, BindingResult result, Model model) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors())
			return "signup";
		user.setRole(rolesService.getRoles()[0]);
		usersService.addUser(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		return "redirect:home";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping("/home")
	public String home(Model model, Pageable pageable, Principal principal, @RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		
		Page<Offer> offerList = new PageImpl<Offer>(new LinkedList<Offer>());
		
		if (searchText != null && !searchText.isEmpty())
			offerList = offersService.searchOffersByTitle(pageable, searchText, user);
		else
			offerList = offersService.getOffersToBuy(pageable, user); 
		
		model.addAttribute("offerList", offerList.getContent());
		model.addAttribute("page", offerList);
		return "/home";
	}
	
	@RequestMapping("/home/update")
	public String updateHome(Model model, Pageable pageable, Principal principal, @RequestParam(value = "", required = false) String searchText) {
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Page<Offer> offerList = new PageImpl<Offer>(new LinkedList<Offer>());
		offerList = offersService.getOffersToBuy(pageable, user); 
		model.addAttribute("offerList", offerList.getContent());
		return "/home :: tableOffers";
	}
	
	
	@RequestMapping(value="/offer/{id}/buy", method=RequestMethod.GET)
	public String setBoughtTrue(HttpSession session, Model model, Principal principal, @PathVariable Long id){
		String email = principal.getName();
		User user = usersService.getUserByEmail(email);
		Offer offerToBuy = offersService.searchOfferById(id);
		
		if (usersService.buyOffer(user, offerToBuy)) {
			session.setAttribute("balance", user.getMoney());
		}
		return "redirect:/home";
	}
}
