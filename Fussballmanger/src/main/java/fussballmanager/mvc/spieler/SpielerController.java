package fussballmanager.mvc.spieler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class SpielerController {
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/spieler/{id}")
	public String getSpieler(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("spieler", spielerService.findeSpieler(id));
		
		return "spieler";
	}
}