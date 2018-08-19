package fussballmanager.mvc.transfermarkt;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
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
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@GetMapping("/transfermarkt/{seite}")
	public String getTransfermarkt(Model model, Authentication auth, @PathVariable("seite") int seite, 
			@ModelAttribute("spielerSuche") SpielerSuche spielerSuche) {		
		List<Spieler> gesuchteSpielerDesTransfermarktes = spielerService.findeFuenfzehnSpielerAnhandDerSuchEingabenVomTransfermarkt(spielerSuche.getPosition(), 
				spielerSuche.getLand(), spielerSuche.getMinimalesAlter(), spielerSuche.getMaximalesAlter(), spielerSuche.getMinimaleStaerke(), 
				spielerSuche.getMaximaleStaerke(), spielerSuche.getMinimalerPreis(), spielerSuche.getMaximalerPreis(), seite);
		SpielerSuche spielerSucheFormular = spielerSuche;
		
		model.addAttribute("seite", seite);
		model.addAttribute("alleTransfermarktSpieler", gesuchteSpielerDesTransfermarktes);
		model.addAttribute("spielerSucheFormular", spielerSucheFormular);
		model.addAttribute("positionenTypen", PositionenTypen.values());
		model.addAttribute("laenderNamenTypen", LaenderNamenTypen.values());
		
		return "transfermarkt";
	}

	@PostMapping("/transfermarkt/{seite}/{id}")
	public String spielerKaufen(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Team aktuellesTeam = aktuellerUser.getAktuellesTeam();
		Spieler spieler = spielerService.findeSpieler(id);
		
		spielerService.spielerVomTransfermarktKaufen(spieler, aktuellesTeam);
		
		return "redirect:/transfermarkt/{seite}";
	}
	
	@PostMapping("/transfermarkt/{seite}/suche")
	public String spielerSuche(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@ModelAttribute("spielerSucheFormular") SpielerSuche spielerSuche) {
		redirectAttributes.addFlashAttribute("spielerSuche", spielerSuche);	
		
		return "redirect:/transfermarkt/{seite}"; 
	}
}
