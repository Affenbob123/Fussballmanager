package fussballmanager.mvc.liga;

import java.text.DecimalFormat;
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

	@GetMapping("/{landName}/{ligaName}/{saisonNummer}/{spieltagNummer}")
	public String getLiga(Model model, Authentication auth, @PathVariable("landName") String landName, 
			@PathVariable("ligaName") String ligaName, @PathVariable("saisonNummer") int saisonNummer, 
			@PathVariable("spieltagNummer") int spieltagNummer) {
		Land land = landService.findeLandDurchLandName(landName);
		Liga liga = ligaService.findeLiga(landName, ligaName);
		Saison saison = saisonService.findeSaisonDurchSaisonNummer(saisonNummer);
		Spieltag spieltag = spieltagService.findeSpieltagDurchSaisonUndSpieltagNummer(saison, spieltagNummer);
		LigaAuswahlHelper ligaAuswahlHelper = new LigaAuswahlHelper();
		ligaAuswahlHelper.setLand(landName);
		ligaAuswahlHelper.setLiga(ligaName);
		ligaAuswahlHelper.setSaison(saisonNummer);
		ligaAuswahlHelper.setSpieltag(spieltagNummer);
		
		List<TabellenEintrag> alleTabellenEintraegeEinerLiga = 
				tabellenEintragService.findeAlleTabellenEintraegeEinerLigaInEinerSaison(liga, saison);
		Collections.sort(alleTabellenEintraegeEinerLiga);
		
		model.addAttribute("ligaAuswahlHelper", ligaAuswahlHelper);
		model.addAttribute("alleLaender", landService.findeAlleLaender());
		model.addAttribute("alleLigenEinesLandes", ligaService.findeAlleLigenEinesLandes(landService.findeLandDurchLandName(landName)));
		model.addAttribute("alleSaisons", saisonService.findeAlleSaisons());		
		model.addAttribute("findeAlleSpieleEinerLigaEinerSaisonEinesSpieltages", 
				erstelleSpielEintraegeEinerLiga(land, liga, saison, spieltag));
		model.addAttribute("alleTabellenEintraegeEinerLiga", alleTabellenEintraegeEinerLiga);

		return "tabelle";
	}
	
	@PostMapping("/{landName}/{ligaName}/{saisonNummer}/{spieltagNummer}")
	public String wechsleLandLigaSaisonOderSpieltag(@ModelAttribute("ligaAuswahlHelper") LigaAuswahlHelper ligaAuswahlHelper) {
		return "redirect:/" + ligaAuswahlHelper.getLand() + "/" + ligaAuswahlHelper.getLiga()
				+ "/" + ligaAuswahlHelper.getSaison() + "/" + ligaAuswahlHelper.getSpieltag();
	}
	
	public List<SpielEintrag> erstelleSpielEintraegeEinerLiga(Land land, Liga liga, Saison saison, Spieltag spieltag) {
		List<SpielEintrag> spielEintraege = new ArrayList<>();
		List<Spiel> alleSpieleEinerLigaEinesSpieltages = 
				spielService.findeAlleLigaspieleEinerLigaUndSaisonUndSpieltag(liga, saison, spieltag);
		for (Spiel spiel : alleSpieleEinerLigaEinesSpieltages) {
			spielEintraege.add(erstelleSpielEintragEinerLiga(spiel));
		}
		return spielEintraege;
	}
	
	public SpielEintrag erstelleSpielEintragEinerLiga(Spiel spiel) {
		SpielEintrag spielEintrag = new SpielEintrag();
		
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
	
		if((spiel.getSpieltag().getSpieltagNummer() > spieltagService.findeAktuellenSpieltag().getSpieltagNummer()) ||
				aktuelleUhrzeit.isBefore(spiel.getSpielTyp().getSpielBeginn())) {
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
