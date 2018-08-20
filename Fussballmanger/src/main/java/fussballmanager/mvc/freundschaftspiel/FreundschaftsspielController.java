package fussballmanager.mvc.freundschaftspiel;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.mvc.liga.LigaAuswahlHelper;
import fussballmanager.service.benachrichtigung.BenachrichtigungService;
import fussballmanager.service.benachrichtigung.FreunschaftsspieleAnfrageTypen;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.tabelle.TabellenEintrag;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.UserService;

@Controller
public class FreundschaftsspielController {

private static final Logger LOG = LoggerFactory.getLogger(FreundschaftsspielController.class);
	
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

	@GetMapping("/freundschaftsspiele/{landName}/{ligaName}")
	public String getLiga(Model model, Authentication auth, @PathVariable("landName") String landName, 
			@PathVariable("ligaName") String ligaName) {
		Liga liga = ligaService.findeLiga(landName, ligaName);
		LigaAuswahlHelper ligaAuswahlHelper = new LigaAuswahlHelper();
		ligaAuswahlHelper.setLand(landName);
		ligaAuswahlHelper.setLiga(ligaName);
		
		List<TabellenEintrag> alleTabellenEintraegeEinerLiga = tabellenEintragService.findeAlleTabellenEintraegeEinerLiga(liga);
		Collections.sort(alleTabellenEintraegeEinerLiga);
		
		model.addAttribute("ligaAuswahlHelper", ligaAuswahlHelper);
		model.addAttribute("alleLaender", landService.findeAlleLaender());
		model.addAttribute("alleLigenEinesLandes", ligaService.findeAlleLigenEinesLandes(landService.findeLandDurchLandName(landName)));
		model.addAttribute("alleSaisons", saisonService.findeAlleSaisons());		
		model.addAttribute("alleTabellenEintraegeEinerLiga", alleTabellenEintraegeEinerLiga);
		model.addAttribute("freundschaftsspieleWrapper", new FreundschaftsspieleWrapper());
		model.addAttribute("freunschaftsspieleAnfrageTypen", FreunschaftsspieleAnfrageTypen.values());

		return "freundschaftsspiele";
	}
	
	@PostMapping("/freundschaftsspiele/{landName}/{ligaName}")
	public String wechsleLandLigaSaisonOderSpieltag(@ModelAttribute("ligaAuswahlHelper") LigaAuswahlHelper ligaAuswahlHelper) {
		return "redirect:/freundschaftsspiele/" + ligaAuswahlHelper.getLand() + "/" + ligaAuswahlHelper.getLiga();
	}
	
	@PostMapping("/freundschaftsspiele/{landName}/{ligaName}/anfrage")
	public String frageFreundschaftsspieleAn(Model model, Authentication auth, @ModelAttribute("freundschaftsspieleWrapper") FreundschaftsspieleWrapper freundschaftsspieleWrapper) {
		LOG.info("{}, {}", freundschaftsspieleWrapper.getAbsender().getName(), freundschaftsspieleWrapper.getEmpfaenger());
		benachrichtigungService.erstelleFreundschaftsspielAnfrage(freundschaftsspieleWrapper.getAbsender(), 
				freundschaftsspieleWrapper.getEmpfaenger(), freundschaftsspieleWrapper.getFreundschaftsspielAnfrageTyp());
		
		return "redirect:/freundschaftsspiele/" + freundschaftsspieleWrapper.getAbsender().getLand().getLandNameTyp().getName() 
				+ "/" + freundschaftsspieleWrapper.getAbsender().getLiga().getLigaNameTyp().getName();
	}
}
