package fussballmanager.mvc.sekretariat.benachrichtigungen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.benachrichtigung.BenachrichtigungService;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.UserService;

@Controller
public class BenachrichtigungenController {

	@Autowired
	LandService landService;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	BenachrichtigungService benachrichtigungService;
	
	@GetMapping("/benachrichtigungen")
	public String getBenachrichtungen(Model model, Authentication auth) {
		model.addAttribute("alleBenachrichtigungenDesAktuellenUsers", benachrichtigungService.findeAlleBenachrichtigungenEinesUsers(userService.findeUser(auth.getName())));
		
		return "sekretariat/benachrichtigungen";
	}
	
	@PostMapping("/benachrichtigung")
	public String interagiereMitBenachrichtigung(Model model, Authentication auth) {
		return "redirect:/benachrichtigungen";
	}
}
