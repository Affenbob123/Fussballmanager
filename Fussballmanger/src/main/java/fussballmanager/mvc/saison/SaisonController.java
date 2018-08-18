package fussballmanager.mvc.saison;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class SaisonController {
	
	@Autowired
	LandService landService;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	SpielService spielService;

	@GetMapping("/team/{teamId}/saison")
	public String getSaison(Model model, Authentication auth, @PathVariable("teamId") Long teamId) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		List<SpielEintrag> spielEintraegeEinerSaison = erstelleSpielEintraegeEinesTeams(aktuellerUser.getAktuellesTeam(), 
				saisonService.findeAktuelleSaison());
		Collections.sort(spielEintraegeEinerSaison);
		
		model.addAttribute("alleSpieleEinesTeamsInEinerSaison", spielEintraegeEinerSaison);
		model.addAttribute("spielTypTurnier", SpieleTypen.TURNIERSPIEL);
		model.addAttribute("spielTypFreundschaft", SpieleTypen.FREUNDSCHAFTSSPIEL);
		model.addAttribute("spielTypPokal", SpieleTypen.POKALSPIEL);
		return "saison";
	}
	
	public List<SpielEintrag> erstelleSpielEintraegeEinesTeams(Team team, Saison saison) {
		List<SpielEintrag> spielEintraege = new ArrayList<>();
		List<Spiel> alleSpielEinesTeamsInEinerSaison = spielService.findeAlleSpieleEinesTeamsInEinerSaison(team, saison);
		Collections.sort(alleSpielEinesTeamsInEinerSaison);
		
		for (Spiel spiel : alleSpielEinesTeamsInEinerSaison) {
			spielEintraege.add(erstelleSpielEintragEinesTeams(spiel));
		}
		return spielEintraege;
	}
	
	public SpielEintrag erstelleSpielEintragEinesTeams(Spiel spiel) {
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
		spielEintrag.setSpielTyp(spiel.getSpielTyp());
		spielEintrag.setSpieltag(spiel.getSpieltag().getSpieltagNummer());
		spielEintrag.setSpielbeginn(spiel.getSpielTyp().getSpielBeginn());
		spielEintrag.setHeimmannschaft(spiel.getHeimmannschaft());
		spielEintrag.setGastmannschaft(spiel.getGastmannschaft());
		if(spiel.getHeimmannschaft() != null) {
			spielEintrag.setStaerkeHeimmannschaft(spiel.getHeimmannschaft().getStaerke());
		}
		if(spiel.getGastmannschaft() != null) {
			spielEintrag.setStaerkeGastmannschaft(spiel.getGastmannschaft().getStaerke());
		}
		
		return spielEintrag;
	}
}
