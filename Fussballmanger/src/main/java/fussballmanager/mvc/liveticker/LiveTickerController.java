package fussballmanager.mvc.liveticker;

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
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.mvc.spiel.SpielEintrag;
import fussballmanager.mvc.spiel.SpielEreignisEintrag;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.SpieleTypen;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.spiel.spielereignisse.SpielEreignisTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;
import fussballmanager.spielsimulation.torversuch.Torversuch;
import fussballmanager.spielsimulation.torversuch.TorversuchService;
import fussballmanager.spielsimulation.torversuch.TorversuchTypen;

@Controller
public class LiveTickerController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LiveTickerController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	TorversuchService torversuchService;

	@GetMapping("/liveticker")
	public String getLiveticker(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		List<Team> alleTeamsDesUsersImLiveticker = teamService.findeAlleTeamsEinesUsersImLiveticker(aktuellerUser, true);
		List<Spiel> alleSpieleDerTeamsImLiveticker = new ArrayList<>();
		
		for(Team team : alleTeamsDesUsersImLiveticker) {
			alleSpieleDerTeamsImLiveticker.add(spielService.findeSpielEinesTeamsInSaisonUndSpieltagUndSpielTyp(team, 
					saisonService.findeAktuelleSaison(), spieltagService.findeAktuellenSpieltag(), SpieleTypen.LIGASPIEL));
		}
		
		List<Torversuch> alleTorversucheDerTeamsImLiveticker = erstelleListeAnTorversuchen(alleTeamsDesUsersImLiveticker);
		
		if(!alleTorversucheDerTeamsImLiveticker.isEmpty()) {
			model.addAttribute("torversuch", alleTorversucheDerTeamsImLiveticker.get(0));
			model.addAttribute("torversuchTypen", TorversuchTypen.values());
		}
		model.addAttribute("livetickerEintraege", erstelleLivetickerEintraege(alleSpieleDerTeamsImLiveticker));
		
		return "liveticker";
	}
	
	@PostMapping("/liveticker")
	public String versucheTorversuchHalten(Model model, Authentication auth, @ModelAttribute("torversuchRichtungSpeichern") Torversuch torversuch) {
		Torversuch versuchteTorversuch = torversuchService.findeTorversuch(torversuch.getId());
		versuchteTorversuch.setRichtungVomUser(torversuch.getRichtungVomUser());
		torversuchService.versucheTorversuchHalten(versuchteTorversuch);
		
		return "redirect:/liveticker";
	}

	private List<LivetickerEintrag> erstelleLivetickerEintraege(List<Spiel> spiele) {
		List<LivetickerEintrag> livetickerEintraege = new ArrayList<>();
		
		for(Spiel spiel : spiele) {
			livetickerEintraege.add(erstelleLivetickerEintrag(spiel));
		}
		
		return livetickerEintraege;
	}
	
	private LivetickerEintrag erstelleLivetickerEintrag(Spiel spiel) {
		LivetickerEintrag livetickerEintrag = new LivetickerEintrag();
		
		livetickerEintrag.setSpielEintrag(erstelleSpielEintrag(spiel));
		livetickerEintrag.setSpielEreignisEintraege(erstelleSpielEreignisEintraegeFuerEinSpiel(spiel.getId()));
		
		return livetickerEintrag;
	}

	private List<SpielEreignisEintrag> erstelleSpielEreignisEintraegeFuerEinSpiel(Long id) {
		List<SpielEreignisEintrag> spielEreignisEintraege = new ArrayList<>();
		List<SpielEreignis> alleSpielEreignisseEinesSpiels = spielService.findeSpiel(id).getSpielEreignisse();
		for (SpielEreignis spielEreignis : alleSpielEreignisseEinesSpiels) {
			spielEreignisEintraege.add(erstelleEinenSpielEreignisEintrag(spielEreignis));
		}
		Collections.sort(spielEreignisEintraege);

		return spielEreignisEintraege;
	}
	
	private SpielEreignisEintrag erstelleEinenSpielEreignisEintrag(SpielEreignis spielEreignis) {
		SpielEreignisEintrag spielEreignisEintrag = new SpielEreignisEintrag();		
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGEHALTEN)) {
			spielEreignisEintrag.setSpielerName(spielEreignis.getTorwart().getName());
			spielEreignisEintrag.setTeam(spielEreignis.getVerteidiger());
		}
		
		if(spielEreignis.getSpielereignisTyp().equals(SpielEreignisTypen.TORVERSUCHGETROFFEN)) {
			spielEreignisEintrag.setSpielerName(spielEreignis.getTorschuetze().getName());
			spielEreignisEintrag.setTeam(spielEreignis.getAngreifer());
		}
		spielEreignisEintrag.setSpielminute(spielEreignis.getSpielminute());
		spielEreignisEintrag.setSpielEreignisName(spielEreignis.getSpielereignisTyp().getBeschreibung());
		spielEreignisEintrag.setSpielEreignisTyp(spielEreignis.getSpielereignisTyp());
		
		return spielEreignisEintrag;
	}
	
	private SpielEintrag erstelleSpielEintrag(Spiel spiel) {
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
	
	private List<Torversuch> erstelleListeAnTorversuchen(List<Team> teams) {
		List<Torversuch> torversucheAllerTeamsImLiveticker = new ArrayList<>();
		
		for(Team team : teams) {
			torversucheAllerTeamsImLiveticker.addAll(torversuchService.findeErhalteneTorversucheEinesTeams(team));
		}
		return torversucheAllerTeamsImLiveticker;
	}
}
