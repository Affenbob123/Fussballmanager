package fussballmanager.mvc.spieler;

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
	
	@GetMapping("/spieler/{id}")
	public String getSpieler(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("spieler", spielerService.findeSpieler(id));
		
		return "spieler";
	}
	
	@GetMapping("/team/{id}/spieler/umbenennen")
	public String getSpielerListeZumUmbenennen(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		SpielerListeWrapper spielerListeWrapper= new SpielerListeWrapper();
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		List<Spieler> spielerDesAktuellenTeams = spielerService.findeAlleSpielerEinesTeams(aktuellerUser.getAktuellesTeam());
		spielerListeWrapper.setSpielerListe(spielerDesAktuellenTeams);
		
		model.addAttribute("alleSpielerDesAktuellenTeams", spielerDesAktuellenTeams);
		model.addAttribute("spielerListeWrapper", spielerListeWrapper);
		
		return "spielerlistezumumbenennen";
	}
	
	@PostMapping("/team/{id}/spieler/umbenennen")
	public String spielerUmbennenen(Model model, Authentication auth, @PathVariable("id") Long id, 
			@ModelAttribute("spielerListeWrapper") SpielerListeWrapper spielerListeWrapper) {
		List<Spieler> spielerDesAktuellenTeams = spielerListeWrapper.getSpielerListe();
		
		for(Spieler s : spielerDesAktuellenTeams) {
			Spieler spieler = spielerService.findeSpieler(s.getId());
			spieler.setName(s.getName());
			spielerService.aktualisiereSpieler(spieler);
		}
		return "redirect:/team/{id}";
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