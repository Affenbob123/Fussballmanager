package fussballmanager.mvc.kader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import fussballmanager.mvc.team.TrainingslagerWrapper;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TrainingslagerController {

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
	
	@GetMapping("/team/{teamId}/trainingslager")
	public String getTrainingslager(Model model, Authentication auth, @PathVariable("teamId") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Team aktuellesTeam = teamService.findeTeam(id);
		
		aktuellerUser.setAktuellesTeam(aktuellesTeam);
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		Collection<Spieler> alleSpielerImTrainingslager = spielerService.findeAlleSpielerEinesTeamsDieImTrainingslagerSind(aktuellesTeam);
		List<Spieler> alleSpielerfuerDieDasTrainingslagerGebuchtWerdenSoll = spielerService.alleSpielerFuerDieDasTrainingsalgerGebuchtWerdenSoll(aktuellesTeam);
		List<Spieler> alleSpielerGebuchtOderImTrainingslager = new ArrayList<>();
		alleSpielerGebuchtOderImTrainingslager.addAll(alleSpielerImTrainingslager);
		alleSpielerGebuchtOderImTrainingslager.addAll(alleSpielerfuerDieDasTrainingslagerGebuchtWerdenSoll);
				List<Spieler> alleSpielerNichtImTrainingslager = 
				spielerService.findeAlleSpielerEinesTeamsDieNichtImTrainingslagerSindUndNochTLTageFreiHaben(aktuellesTeam);
		DecimalFormat zahlenFormat = new DecimalFormat("0.0");
		TrainingslagerWrapper trainingslagerWrapperNichtImTrainingslager = new TrainingslagerWrapper();
		TrainingslagerWrapper trainingslagerWrapperImTrainingslager = new TrainingslagerWrapper();
		List<Trainingslager> alleTrainingslagerTypen = new ArrayList<Trainingslager>();
		
		trainingslagerWrapperNichtImTrainingslager.setSpieler(alleSpielerNichtImTrainingslager);
		trainingslagerWrapperImTrainingslager.setSpieler(alleSpielerGebuchtOderImTrainingslager);
		
		for(Trainingslager trainingslager : Trainingslager.values()) {
			if(!(trainingslager.equals(Trainingslager.KEIN_TRAININGSLAGER))) {
				alleTrainingslagerTypen.add(trainingslager);
			}
		}
		model.addAttribute("trainingslagerWrapperNichtImTrainingslager", trainingslagerWrapperNichtImTrainingslager);
		model.addAttribute("trainingslagerWrapperImTrainingslager", trainingslagerWrapperImTrainingslager);
		model.addAttribute("alleTrainingslagerTypen", alleTrainingslagerTypen);
		model.addAttribute("zahlenFormat", zahlenFormat);
		model.addAttribute("spielerAusTrainingslagerHolen", new Spieler());
		model.addAttribute("aufstellungsPositionsTypTrainingslager", AufstellungsPositionsTypen.TRAININGSLAGER);
		
		return "kader/trainingslager";
	}
	
	@PostMapping("/team/{teamId}/trainingslager")
	public String bucheTrainingslager(Model model, Authentication auth, @ModelAttribute("trainingslagerWrapperNichtImTrainingslager") TrainingslagerWrapper trainingslagerWrapper) {
		for(Spieler s : trainingslagerWrapper.getSpieler()) {
			Spieler spieler = spielerService.findeSpieler(s.getId());
			spieler.setTrainingslagerTage(s.getTrainingslagerTage());
			spieler.setTrainingsLager(trainingslagerWrapper.getTrainingslager());
			
			if(spieler.getTrainingslagerTage() <= 0) {
				spieler.setTrainingsLager(Trainingslager.KEIN_TRAININGSLAGER);
			}
			spielerService.aktualisiereSpieler(spieler);
		}
		return "redirect:/team/{teamId}/trainingslager";
	}
	
	@PostMapping("/team/{teamId}/trainingslager/abbrechen")
	public String brecheTrainingslagerAb(Model model, Authentication auth, @ModelAttribute("spielerAusTrainingslagerHolen") Spieler spieler) {
		Spieler s = spielerService.findeSpieler(spieler.getId());
		
		if(s.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TRAININGSLAGER)) {
			s.setTrainingslagerTage(1);
		} else {
			s.setTrainingslagerTage(0);
			s.setTrainingsLager(Trainingslager.KEIN_TRAININGSLAGER);
		}
		
		spielerService.aktualisiereSpieler(s);
		return "redirect:/team/{teamId}/trainingslager";
	}
}
