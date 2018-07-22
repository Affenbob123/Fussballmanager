package fussballmanager.mvc.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.UserService;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String getLoginSeite() {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginVersuch() {
		return "redirect:/";
	}
}
