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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.spieler.PositionenTypen;
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
	public String getTransfermarkt(Model model, Authentication auth, @ModelAttribute("spielerSuche") SpielerSuche spielerSuche) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		List<Spieler> gesuchteSpielerDesTransfermarktes = spielerService.findeAlleSpielerAnhandDerSuchEingaben(spielerSuche.getPosition(), 
				spielerSuche.getLand(), spielerSuche.getMinimalesAlter(), spielerSuche.getMaximalesAlter(), spielerSuche.getMinimaleStaerke(), 
				spielerSuche.getMaximaleStaerke(), spielerSuche.getMinimalerPreis(), spielerSuche.getMaximalerPreis());
		SpielerSuche spielerSucheFormular = spielerSuche;
		
		model.addAttribute("alleTransfermarktSpieler", gesuchteSpielerDesTransfermarktes);
		model.addAttribute("spielerSucheFormular", spielerSucheFormular);
		model.addAttribute("positionenTypen", PositionenTypen.values());
		model.addAttribute("laenderNamenTypen", LaenderNamenTypen.values());
		
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
	public String spielerSuche(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@ModelAttribute("spielerSucheFormular") SpielerSuche spielerSuche) {
	
		redirectAttributes.addFlashAttribute("spielerSuche", spielerSuche);	
		
		return "redirect:/transfermarkt"; 
	}
}
