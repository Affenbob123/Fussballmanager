package fussballmanager.mvc.spieler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.mvc.sekretariat.TeamListeWrapper;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class SpielerController {
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@GetMapping("/spieler/{id}")
	public String getSpieler(Model model, Authentication auth, @PathVariable("id") Long id) {
		model.addAttribute("spieler", spielerService.findeSpieler(id));
		
		return "kader/spieler";
	}
	
	@PostMapping("/spieler/{id}/talentwert")
	public String spielerTalentwertermitteln(Model model, Authentication auth, @PathVariable("id") Long id) {
		Spieler spieler = spielerService.findeSpieler(id);
		spielerService.ermittleTalentwert(spieler);
		
		return "redirect:/spieler/{id}";
	}
	
	@PostMapping("/spieler/{id}/transfermarkt")
	public String spielerAufTransfermarktStellen(Model model, Authentication auth, @PathVariable("id") Long id, @ModelAttribute("spieler") Spieler spieler) {
		Spieler s = spielerService.findeSpieler(id);
		spielerService.spielerAufTransfermarktStellen(s, spieler.getPreis());
		
		return "redirect:/spieler/{id}";
	}
	
	@PostMapping("/spieler/{id}/transfermarkt/entfernen")
	public String spielerVonTransfermarktNehmen(Model model, Authentication auth, @PathVariable("id") Long id) {
		Spieler spieler = spielerService.findeSpieler(id);
		spielerService.spielerVonTransfermarktNehmen(spieler);
		
		return "redirect:/spieler/{id}";
	}
	
	@PostMapping("/spieler/{id}/entlassen")
	public String spielerEntlassen(Model model, Authentication auth, @PathVariable("id") Long id) {
		Spieler spieler = spielerService.findeSpieler(id);
		spielerService.spielerEntlassen(spieler);
		
		return "redirect:/spieler/{id}";
	}
}