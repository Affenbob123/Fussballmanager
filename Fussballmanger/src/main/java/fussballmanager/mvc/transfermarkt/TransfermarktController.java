package fussballmanager.mvc.transfermarkt;

import java.util.Collections;
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
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TransfermarktController {
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/transfermarkt")
	public String getTransfermarkt(Model model, Authentication auth, @ModelAttribute("minimumWerteSpielerSuche") Spieler minimumWerteSpielerSuche, 
			@ModelAttribute("maximumWerteSpielerSuche") Spieler maximumWerteSpielerSuche) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		List<Spieler> alleTransfermarktSpieler = spielerService.findeAlleSpielerNachAufstellungsPositionsTyp(AufstellungsPositionsTypen.TRANSFERMARKT);
		Collections.sort(alleTransfermarktSpieler);
		
		model.addAttribute("alleTransfermarktSpieler", alleTransfermarktSpieler);
		model.addAttribute("minimumWerteSpielerSuche", new Spieler());
		model.addAttribute("maximumWerteSpielerSuche", new Spieler());
		
		return "transfermarkt";
	}

	@PostMapping("/transfermarkt/{id}")
	public String spielerKaufen(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Team aktuellesTeam = aktuellerUser.getAktuellesTeam();
		Spieler spieler = spielerService.findeSpieler(id);
		
		spielerService.spielerVomTransfermarktKaufen(spieler, aktuellesTeam);
		
		return "redirect:/transfermarkt";
	}
	
	@PostMapping("/transfermarkt")
	public String spielerSuche(Model model, Authentication auth, @ModelAttribute("minimumWerteSpielerSuche") Spieler minimumWerteSpielerSuche, 
			@ModelAttribute("maximumWerteSpielerSuche") Spieler maximumWerteSpielerSuche) {
		
		return "redirect:/transfermarkt"; 
	}
}
