package fussballmanager.mvc.spiel;

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
import org.springframework.web.bind.annotation.PathVariable;

import fussballmanager.ScheduldTasks;
import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spiel.spielereignisse.SpielEreignis;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class SpielController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpielController.class);
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@GetMapping("/spiel/{id}")
	public String getSpielDetails(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		Spiel spiel = spielService.findeSpiel(id);
		
		model.addAttribute("spielEintrag", erstelleSpielEintrag(spiel));
		model.addAttribute("spielEreignisEintaerge", erstelleSpielEreignisEintraegeFuerEinSpiel(id));
		
		return "spieldetails";
	}

	public List<SpielEreignisEintrag> erstelleSpielEreignisEintraegeFuerEinSpiel(Long id) {
		List<SpielEreignisEintrag> spielEreignisEintraege = new ArrayList<>();
		List<SpielEreignis> alleSpielEreignisseEinesSpiels = spielService.findeSpiel(id).getSpielEreignisse();
		LOG.info("{}", alleSpielEreignisseEinesSpiels);
		for (SpielEreignis spielEreignis : alleSpielEreignisseEinesSpiels) {
			spielEreignisEintraege.add(erstelleEinenSpielEreignisEintrag(spielEreignis));
		}
		Collections.sort(spielEreignisEintraege);

		LOG.info("{}", spielEreignisEintraege.size());
		return spielEreignisEintraege;
	}
	
	public SpielEreignisEintrag erstelleEinenSpielEreignisEintrag(SpielEreignis spielEreignis) {
		SpielEreignisEintrag spielEreignisEintrag = new SpielEreignisEintrag();		
		
		spielEreignisEintrag.setSpielminute(spielEreignis.getSpielminute());
		spielEreignisEintrag.setSpielerName(spielEreignis.getSpieler().getName());
		spielEreignisEintrag.setTeamName(spielEreignis.getTeam().getName());
		spielEreignisEintrag.setSpielEreignisName(spielEreignis.getSpielereignisTyp().getBeschreibung());
		
		return spielEreignisEintrag;
	}
	
	public SpielEintrag erstelleSpielEintrag(Spiel spiel) {
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
		spielEintrag.setNameHeimmannschaft(spiel.getHeimmannschaft().getName());
		spielEintrag.setNameGastmannschaft(spiel.getGastmannschaft().getName());
		spielEintrag.setStaerkeHeimmannschaft(spiel.getHeimmannschaft().getStaerke());
		spielEintrag.setStaerkeGastmannschaft(spiel.getGastmannschaft().getStaerke());
		
		return spielEintrag;
	}
}
