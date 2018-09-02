package fussballmanager.mvc.sekretariat.benachrichtigungen;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.benachrichtigung.Benachrichtigung;
import fussballmanager.service.benachrichtigung.BenachrichtigungService;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class BenachrichtigungenController {
	
	private static final Logger LOG = LoggerFactory.getLogger(BenachrichtigungenController.class);

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
	
	@GetMapping("/benachrichtigungen/{seite}")
	public String getBenachrichtungen(Model model, Authentication auth, @PathVariable("seite") int seite) {
		User aktuellerUser = userService.findeUser(auth.getName());
		List<Benachrichtigung> alleBenachrichtigungenDerSeite = benachrichtigungService.findeBenachrichtigungenNachSeite(aktuellerUser, seite);
		model.addAttribute("alleBenachrichtigungenDesAktuellenUsers", benachrichtigungService.findeAlleBenachrichtigungenEinesUsers(aktuellerUser));
		model.addAttribute("alleBenachrichtigungenDesAktuellenUsersDerAktuellenSeite", alleBenachrichtigungenDerSeite);
		model.addAttribute("aktuelleSeite", seite);
		
		return "sekretariat/benachrichtigungen";
	}
	
	@PostMapping("/benachrichtigung")
	public String interagiereMitBenachrichtigung(Model model, Authentication auth) {
		return "redirect:/benachrichtigungen/{seite}";
	}
	
	@PostMapping("/benachrichtigungen/{seite}/{benachrichtigungId}/gelesen")
	public String setNachrichtenGelesen(Model model, Authentication auth, @PathVariable("benachrichtigungId") Long benachrichtigungId, 
			@PathVariable("seite") int seite) {
		Benachrichtigung benachrichtigung = benachrichtigungService.findeBenachrichtigung(benachrichtigungId);
		benachrichtigung.setGelesen(true);
		benachrichtigungService.aktualisiereBenachrichtigung(benachrichtigung);
		
		return "redirect:/benachrichtigungen/" + seite;
	}
	
	@PostMapping("/benachrichtigungen/{seite}/{benachrichtigungId}/loeschen")
	public String loescheBenachrichtigung(Model model, Authentication auth, @PathVariable("benachrichtigungId") Long benachrichtigungId, 
			@PathVariable("seite") int seite) {
		benachrichtigungService.loescheBenachrichtigung(benachrichtigungService.findeBenachrichtigung(benachrichtigungId));
		
		return "redirect:/benachrichtigungen/" + seite;
	}
}
