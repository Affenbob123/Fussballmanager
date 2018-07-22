package fussballmanager.mvc.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping("/accounterstellen")
	public String getErstelleUser(Model model, Authentication auth) {
		model.addAttribute("neuerUser", new User());
		model.addAttribute("laenderNamenTypen", LaenderNamenTypen.values());
		
		return "usercreate";
	}
	
	@PostMapping("/accounterstellen")
	public String erstelleUser(Model model, Authentication auth, @ModelAttribute("neuerUser") User neuerUser) {
		userService.legeUserAn(neuerUser);
		
		return "redirect:/";
	}
}
