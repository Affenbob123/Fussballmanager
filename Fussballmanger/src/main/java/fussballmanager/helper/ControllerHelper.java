package fussballmanager.helper;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import fussballmanager.service.benachrichtigung.BenachrichtigungService;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@ControllerAdvice
public class ControllerHelper {
	
	@Autowired
	LandService landService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	BenachrichtigungService benachrichtigungService;
	
	@ModelAttribute
	public void globaleModelAttributes(Model model, Authentication auth) {
		if(auth != null) {
			User aktuellerUser = userService.findeUser(auth.getName());
			Team aktuellesTeam = aktuellerUser.getAktuellesTeam();
			model.addAttribute("aktuellesTeam", aktuellesTeam);
			model.addAttribute("aktuellerUser", aktuellerUser);
			model.addAttribute("auth", auth);
			model.addAttribute("alleUngelesenenBenachrichtigungenDesAktuellenUsers", benachrichtigungService.findeAlleBenachrichtigungenEinesUsers(aktuellerUser));
		}
		DecimalFormat zahlenFormat = new DecimalFormat("0.0");
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		model.addAttribute("zahlenFormat", zahlenFormat);
	}
}
