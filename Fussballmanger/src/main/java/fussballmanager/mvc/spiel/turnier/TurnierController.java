package fussballmanager.mvc.spiel.turnier;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.mvc.spiel.SpielEintrag;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.turnier.Turnier;
import fussballmanager.service.spiel.turnier.TurnierService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TurnierController {

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
	TurnierService turnierService;
	
	@Autowired
	SpielService spielService;
	
	@GetMapping("/turniere")
	public String getTurniere(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		model.addAttribute("auth", auth);
		
		List<Turnier> alleTurniere = turnierService.findeAlleTurniere();
		Collections.sort(alleTurniere);
		model.addAttribute("alleTurniere", alleTurniere);
		
		return "turnier/turnierliste";
	}
	
	@GetMapping("/turnier/{turnierId}")
	public String getTurniere(Model model, Authentication auth, @PathVariable("turnierId") Long turnierId) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Turnier aktuellesTurnier = turnierService.findeTurnier(turnierId);
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		model.addAttribute("auth", auth);
		
		model.addAttribute("ausgewaehltesTurnier", turnierService.findeTurnier(turnierId));
		model.addAttribute("alleTeamsDesAktuellenUser", teamService.findeAlleTeamsEinesUsers(aktuellerUser));
		model.addAttribute("alleSpieleEinesTurniers", erstelleSpielEintraegeEinesTurniers(aktuellesTurnier));
		
		return "turnier/turnier";
	}
	
	@PostMapping("/turnier/{turnierId}")
	public String fuegeTeamsZumTurnierHinzu(Model model, Authentication auth, @ModelAttribute("ausgewaehltesTurnier") Turnier turnier) {
		
		Turnier t = turnierService.findeTurnier(turnier.getId());
		Long turnierId = t.getId();
		int anzahlTeamsInTurnier = t.getTeams().size();
		if((anzahlTeamsInTurnier + turnier.getTeams().size()) > 128) {
			//TODO errormessage
			return "redirect:/turnier/" + turnierId;
		}
		t.getTeams().addAll(turnier.getTeams());
		turnierService.aktualisiereTurnier(t);
		
		return "redirect:/turnier/" + turnierId;
	}
	
	@PostMapping("/turnier/{turnierId}/loeschen")
	public String loescheTurnier(Model model, Authentication auth, @ModelAttribute("ausgewaehltesTurnier") Turnier turnier) {
		Turnier t = turnierService.findeTurnier(turnier.getId());
		turnierService.loescheTurnier(t);
		return "redirect:/turniere";
	}
	
	@PostMapping("/turnier/{turnierId}/loeschen/{teamId}")
	public String loescheTeamAusTurnier(Model model, Authentication auth, @PathVariable("teamId") Long teamId, 
			@PathVariable("turnierId") Long turnierId) {
		Team team = teamService.findeTeam(teamId);
		Turnier turnier = turnierService.findeTurnier(turnierId);
		turnier.getTeams().remove(team);
		turnierService.aktualisiereTurnier(turnier);
		return "redirect:/turnier/{turnierId}";
	}
	
	@PostMapping("/turnier/{turnierId}/turnierstatus")
	public String aendereTurnierStatus(Model model, Authentication auth, @ModelAttribute("ausgewaehltesTurnier") Turnier turnier) {
		Turnier t = turnierService.findeTurnier(turnier.getId());
		t.setGeschlossen(turnier.isGeschlossen());
		turnierService.aktualisiereTurnier(t);
		Long turnierId = t.getId();
		return "redirect:/turnier/" + turnierId;
	}
	
	@GetMapping("/turnier/erstellen")
	public String getTurnierErstellen(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		model.addAttribute("turnier", new Turnier());
		model.addAttribute("alleSpieltageNachDemAktuellen", spieltagService.findeAlleAktuellenSpieltageFuerTurnier());
		
		return "turnier/turniererstellen";
	}
	
	@PostMapping("/turnier/erstellen")
	public String erstelleTurnier(Model model, Authentication auth, @ModelAttribute("turnier") Turnier turnier) {
		turnier.setUser(userService.findeUser(auth.getName()));
		turnierService.legeTurnierAn(turnier);
		Long turnierId = turnier.getId();
		return "redirect:/turnier/" + turnierId;
	}
	
	public List<SpielEintrag> erstelleSpielEintraegeEinesTurniers(Turnier tunier) {
		List<SpielEintrag> spielEintraege = new ArrayList<>();
		List<Spiel> alleTurnierSpiele = tunier.getTurnierSpiele();
		
		for (Spiel turnierspiel : alleTurnierSpiele) {
			spielEintraege.add(erstelleSpielEintragTurniers(turnierspiel));
		}
		return spielEintraege;
	}
	
	public SpielEintrag erstelleSpielEintragTurniers(Spiel turnierSpiel) {
		SpielEintrag spielEintrag = new SpielEintrag();
		
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		if((turnierSpiel.getSpieltag().getSpieltagNummer() > spieltagService.findeAktuellenSpieltag().getSpieltagNummer()) ||
				aktuelleUhrzeit.isBefore(turnierSpiel.getSpielTyp().getSpielBeginn())) {
			spielEintrag.setToreHeimmannschaft(-1);
			spielEintrag.setToreGastmannschaft(-1);
			spielEintrag.setToreHeimmannschaftHalbzeit(-1);
			spielEintrag.setToreGastmannschaftHalbzeit(-1);
		} else {
			spielEintrag.setToreHeimmannschaft(turnierSpiel.getToreHeimmannschaft());
			spielEintrag.setToreGastmannschaft(turnierSpiel.getToreGastmannschaft());
			if(turnierSpiel.getSpielTyp().getSpielBeginn().plusHours(1).isBefore(aktuelleUhrzeit)) {
				spielEintrag.setToreHeimmannschaftHalbzeit(-1);
				spielEintrag.setToreGastmannschaftHalbzeit(-1);
			} else {
				spielEintrag.setToreHeimmannschaftHalbzeit(turnierSpiel.getToreHeimmannschaftZurHalbzeit());
				spielEintrag.setToreGastmannschaftHalbzeit(turnierSpiel.getToreGastmannschaftZurHalbzeit());
			}
			
		}
		spielEintrag.setId(turnierSpiel.getId());
		spielEintrag.setSpielTyp(turnierSpiel.getSpielTyp());
		spielEintrag.setSpieltag(turnierSpiel.getSpieltag().getSpieltagNummer());
		spielEintrag.setSpielbeginn(turnierSpiel.getSpielTyp().getSpielBeginn());
		spielEintrag.setHeimmannschaft(turnierSpiel.getHeimmannschaft());
		spielEintrag.setGastmannschaft(turnierSpiel.getGastmannschaft());
		if(turnierSpiel.getHeimmannschaft() != null) {
			spielEintrag.setStaerkeHeimmannschaft(turnierSpiel.getHeimmannschaft().getStaerke());
		}
		if(turnierSpiel.getGastmannschaft() != null) {
			spielEintrag.setStaerkeGastmannschaft(turnierSpiel.getGastmannschaft().getStaerke());
		}
		spielEintrag.setkOSpielTyp(turnierSpiel.getkOSpielTyp());
		
		return spielEintrag;
	}
}
