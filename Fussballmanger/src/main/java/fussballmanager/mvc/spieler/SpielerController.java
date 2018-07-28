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

import fussballmanager.mvc.sekretariat.TeamListeWrapper;
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
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("spieler", spielerService.findeSpieler(id));
		
		return "spieler";
	}
	
	@GetMapping("/team/{id}/spieler/umbenennen")
	public String getSpielerListeZumUmbenennen(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		SpielerListeWrapper spielerListeWrapper= new SpielerListeWrapper();
		
		
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
}