package fussballmanager.mvc.liga;

import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class LigaController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LigaController.class);
	
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
	SpielEreignisService spielEreignisService;

	@GetMapping("/liga/{landName}/{ligaName}")
	public String getLiga(Model model, Authentication auth, @PathVariable("landName") String landName, 
			@PathVariable("ligaName") String ligaName, @ModelAttribute("land") Land land, 
			@ModelAttribute("liga") Liga liga, @ModelAttribute("saison") Saison saison, 
			@ModelAttribute("spieltag") Spieltag spieltag) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("spielEreignisService", spielEreignisService);
		
		Land ausgewaehltesLand = landService.findeLandDurchLandName(landName);;
		Liga ausgewaehlteLiga = ligaService.findeLiga(landName, ligaName);
		Saison ausgewaehlteSaison;
		Spieltag ausgewaehlterSpieltag;
		
//		if(land.getLandNameTyp().equals(null)) {
//			ausgewaehltesLand = 
//		} else {
//			ausgewaehltesLand = 
//		}
//		
//		if(spieltag.getSpieltagNummer() == 0) {
//			ausgewaehlterSpieltag = spieltagService.findeAktuellenSpieltag();
//		} else {
//			ausgewaehlterSpieltag = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltag.getSpieltagNummer(), ausgewaehlteSaison);
//		}
		
		if(saison.getSaisonNummer() == 0) {
			ausgewaehlteSaison = saisonService.findeAktuelleSaison();
		} else {
			ausgewaehlteSaison = saisonService.findeSaisonDurchSaisonNummer(saison.getSaisonNummer());
		}
		
		if(spieltag.getSpieltagNummer() == 0) {
			ausgewaehlterSpieltag = spieltagService.findeAktuellenSpieltag();
		} else {
			ausgewaehlterSpieltag = spieltagService.findeSpieltagDurchSpieltagUndSaison(spieltag.getSpieltagNummer(), ausgewaehlteSaison);
		}
		
		model.addAttribute("land", ausgewaehltesLand);
		model.addAttribute("alleLaender", landService.findeAlleLaender());
		
		model.addAttribute("liga", ausgewaehlteLiga);
		model.addAttribute("alleLigenEinesLandes", ligaService.findeAlleLigenEinesLandes(landName));
				
		model.addAttribute("saison", ausgewaehlteSaison);
		model.addAttribute("alleSaisons", saisonService.findeAlleSaisons());
		
		model.addAttribute("spieltag", ausgewaehlterSpieltag);
		model.addAttribute("alleSpieltage", spieltagService.findeAlleSpieltageEinerSaison(ausgewaehlteSaison));
		
		model.addAttribute("findeAlleSpieleEinerLigaEinerSaisonEinesSpieltages", 
				spielService.findeAlleSpieleEinerLigaEinerSaisonEinesSpieltages(ligaService.findeLiga(landName, ligaName), ausgewaehlteSaison, ausgewaehlterSpieltag));
		model.addAttribute("alleTeamsDerAktuellenLiga", erstelleLigaTabelle(landName, ligaName));
		
		return "tabelle";
	}
	
	@PostMapping("/liga/{landName}/{ligaName}/land")
	public String aenderLand(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@PathVariable("landName") String landName, @PathVariable("ligaName") String ligaName, 
			@ModelAttribute("land") Land land) {
		landName = land.getLandNameTyp().getName();
		redirectAttributes.addFlashAttribute("land", land);
		
		return "redirect:/liga/" + landName + "/{ligaName}";
	}
	
	@PostMapping("/liga/{landName}/{ligaName}/liga")
	public String aenderLiga(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@PathVariable("landName") String landName, @PathVariable("ligaName") String ligaName, 
			@ModelAttribute("liga") Liga liga) {
		ligaName = liga.getLigaNameTyp().getName();
		redirectAttributes.addFlashAttribute("liga", liga);
		
		return "redirect:/liga/{landName}/" + ligaName;
	}
	
	@PostMapping("/liga/{landName}/{ligaName}/saison")
	public String aenderLigaSaison(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@PathVariable("landName") String landName, @PathVariable("ligaName") String ligaName, 
			@ModelAttribute("saison") Saison saison) {
		redirectAttributes.addFlashAttribute("saison", saison);
		
		return "redirect:/liga/{landName}/{ligaName}";
	}
	
	@PostMapping("/liga/{landName}/{ligaName}/spieltag")
	public String aenderLigaSpieltag(Model model, Authentication auth, RedirectAttributes redirectAttributes, 
			@PathVariable("landName") String landName, @PathVariable("ligaName") String ligaName, 
			@ModelAttribute("spieltag") Spieltag spieltag) {
		redirectAttributes.addFlashAttribute("spieltag", spieltag);
		
		return "redirect:/liga/{landName}/{ligaName}";
	}
	
	@GetMapping("/ligen")
	public String getAlleLigen(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("alleLigen", ligaService.findeAlleLigen());
		
		return "ligenliste";
	}
	
	public List<LigaEintrag> erstelleLigaTabelle(String land, String ligaName) {
		List<LigaEintrag> ligaEintraege = new ArrayList<>();
		for (Team team : teamService.findeAlleTeamsEinerLiga(ligaService.findeLiga(land, ligaName))) {
			ligaEintraege.add(erstelleEineZeileDerLigaTabelle(team));
		}
		return ligaEintraege;
	}
	
	public LigaEintrag erstelleEineZeileDerLigaTabelle(Team team) {
		LigaEintrag ligaEintrag = new LigaEintrag();
		
		ligaEintrag.setId(team.getId());
		ligaEintrag.setLand(team.getLand());
		ligaEintrag.setLiga(team.getLiga());
		ligaEintrag.setPlatzierung(1);
		ligaEintrag.setTeamName(team.getName());
		//TODO Vervollständigen
		return ligaEintrag;
	}
	
}
