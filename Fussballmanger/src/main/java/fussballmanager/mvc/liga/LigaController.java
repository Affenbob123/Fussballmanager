package fussballmanager.mvc.liga;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.mvc.spiel.SpielEintrag;
import fussballmanager.service.land.Land;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.tabelle.TabellenEintrag;
import fussballmanager.service.tabelle.TabellenEintragService;
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
	TabellenEintragService tabellenEintragService;

	@GetMapping("/liga/{landName}/{ligaName}")
	public String getLiga(Model model, Authentication auth, @PathVariable("landName") String landName, 
			@PathVariable("ligaName") String ligaName, @ModelAttribute("land") Land land, 
			@ModelAttribute("liga") Liga liga, @ModelAttribute("saison") Saison saison, 
			@ModelAttribute("spieltag") Spieltag spieltag) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		Land ausgewaehltesLand = landService.findeLandDurchLandName(landName);;
		Liga ausgewaehlteLiga = ligaService.findeLiga(landName, ligaName);
		Saison ausgewaehlteSaison;
		Spieltag ausgewaehlterSpieltag;
		
		if(saison.getSaisonNummer() == 0) {
			ausgewaehlteSaison = saisonService.findeAktuelleSaison();
		} else {
			ausgewaehlteSaison = saisonService.findeSaisonDurchSaisonNummer(saison.getSaisonNummer());
		}
		
		if(spieltag.getSpieltagNummer() == 0) {
			ausgewaehlterSpieltag = spieltagService.findeAktuellenSpieltag();
		} else {
			ausgewaehlterSpieltag = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(ausgewaehlteSaison, spieltag.getSpieltagNummer());
		}
		
		List<TabellenEintrag> alleTabellenEintraegeEinerLiga = tabellenEintragService.findeAlleTabellenEintraegeEinerLigaInEinerSaison(ausgewaehlteLiga, ausgewaehlteSaison);
		Collections.sort(alleTabellenEintraegeEinerLiga);
		
		model.addAttribute("land", ausgewaehltesLand);
		model.addAttribute("alleLaender", landService.findeAlleLaender());
		
		model.addAttribute("liga", ausgewaehlteLiga);
		model.addAttribute("alleLigenEinesLandes", ligaService.findeAlleLigenEinesLandes(landService.findeLandDurchLandName(landName)));
				
		model.addAttribute("saison", ausgewaehlteSaison);
		model.addAttribute("alleSaisons", saisonService.findeAlleSaisons());
		
		model.addAttribute("spieltag", ausgewaehlterSpieltag);
		model.addAttribute("alleSpieltage", spieltagService.findeAlleSpieltageEinerSaison(ausgewaehlteSaison));
		
		model.addAttribute("findeAlleSpieleEinerLigaEinerSaisonEinesSpieltages", 
				erstelleSpielEintraegeEinerLiga(landName, ligaName, ausgewaehlteSaison, ausgewaehlterSpieltag));
		model.addAttribute("alleTabellenEintraegeEinerLiga", alleTabellenEintraegeEinerLiga);

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
	
	public List<SpielEintrag> erstelleSpielEintraegeEinerLiga(String land, String ligaName, Saison saison, Spieltag spieltag) {
		List<SpielEintrag> spielEintraege = new ArrayList<>();
		List<Spiel> alleSpieleEinerLigaEinesSpieltages = 
				spielService.findeAlleSpieleEinerLigaUndSaisonUndSpieltag(ligaService.findeLiga(land, ligaName), saison, spieltag);
		for (Spiel spiel : alleSpieleEinerLigaEinesSpieltages) {
			spielEintraege.add(erstelleSpielEintragEinerLiga(spiel));
		}
		return spielEintraege;
	}
	
	public SpielEintrag erstelleSpielEintragEinerLiga(Spiel spiel) {
		SpielEintrag spielEintrag = new SpielEintrag();
		
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		//TODO WIEDER IN IF EINFÜGEN
		//(spiel.getSpieltag().getSpieltagNummer() > spieltagService.findeAktuellenSpieltag().getSpieltagNummer()) ||
		//aktuelleUhrzeit.isBefore(spiel.getSpielTyp().getSpielBeginn())
		if(false) {
			spielEintrag.setToreHeimmannschaft(-1);
			spielEintrag.setToreGastmannschaft(-1);
			spielEintrag.setToreHeimmannschaftHalbzeit(-1);
			spielEintrag.setToreGastmannschaftHalbzeit(-1);
		} else {
			spielEintrag.setToreHeimmannschaft(spiel.getToreHeimmannschaft());
			spielEintrag.setToreGastmannschaft(spiel.getToreGastmannschaft());
			if(spiel.getSpielTyp().getSpielBeginn().plusHours(1).isBefore(aktuelleUhrzeit)) {
				spielEintrag.setToreHeimmannschaftHalbzeit(-1);
				spielEintrag.setToreGastmannschaftHalbzeit(-1);
			} else {
				spielEintrag.setToreHeimmannschaftHalbzeit(spiel.getToreHeimmannschaftZurHalbzeit());
				spielEintrag.setToreGastmannschaftHalbzeit(spiel.getToreGastmannschaftZurHalbzeit());
			}
			
		}
		
		spielEintrag.setId(spiel.getId());
		spielEintrag.setSpieltag(spiel.getSpieltag().getSpieltagNummer());
		spielEintrag.setSpielbeginn(spiel.getSpielTyp().getSpielBeginn());
		spielEintrag.setHeimmannschaft(spiel.getHeimmannschaft());
		spielEintrag.setGastmannschaft(spiel.getGastmannschaft());
		spielEintrag.setStaerkeHeimmannschaft(spiel.getHeimmannschaft().getStaerke());
		spielEintrag.setStaerkeGastmannschaft(spiel.getGastmannschaft().getStaerke());
		
		return spielEintrag;
	}
}
