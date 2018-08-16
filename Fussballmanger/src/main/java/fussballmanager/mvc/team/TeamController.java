package fussballmanager.mvc.team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.Trainingslager;
import fussballmanager.service.team.AusrichtungsTypen;
import fussballmanager.service.team.EinsatzTypen;
import fussballmanager.service.team.FormationsTypen;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TeamController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TeamController.class);
	
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
	
	@GetMapping("/team/{teamId}")
	public String getTeamListe(Model model, Authentication auth, @PathVariable("teamId") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Team aktuellesTeam = teamService.findeTeam(id);
		
		aktuellerUser.setAktuellesTeam(aktuellesTeam);
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		List<Spieler> alleSpielerEinesTeams = spielerService.findeAlleSpielerEinesTeams(aktuellesTeam);
		DecimalFormat zahlenFormat = new DecimalFormat("0.0");
		
		model.addAttribute("zahlenFormat", zahlenFormat);
		model.addAttribute("alleSpielerDesAktuellenTeams", alleSpielerEinesTeams);
		model.addAttribute("alleFormationsTypen", FormationsTypen.values());
		model.addAttribute("alleEinsatzTypen", EinsatzTypen.values());
		model.addAttribute("alleAusrichtungsTypen", AusrichtungsTypen.values());
		model.addAttribute("aufstellungsPositionsTypTrainingslager", AufstellungsPositionsTypen.TRAININGSLAGER);
		model.addAttribute("aufstellungsPositionsTypErsatz", AufstellungsPositionsTypen.ERSATZ);
		model.addAttribute("aufstellungsPositionsTypGesperrt", AufstellungsPositionsTypen.GESPERRT);
		model.addAttribute("aufstellungsPositionsTypVerletzt", AufstellungsPositionsTypen.VERLETZT);
		model.addAttribute("alleAufstellungsPositionsTypen", aktuellesTeam.getFormationsTyp().getAufstellungsPositionsTypen());
		model.addAttribute("alleSpielerAufErsatzbank", spielerService.findeAlleSpielerEinesTeamsAufErsatzbank(aktuellesTeam));
		model.addAttribute("einzuwechselnderSpieler", new Spieler());
		model.addAttribute("anzahlDerEinwechslungen", teamService.findeTeam(id).getAnzahlAuswechselungen());
		model.addAttribute("summeSpielerWerte", erstelleSummeDerSpielerWerteListe(alleSpielerEinesTeams));
		
		return "kader/spielerliste";
	}
	
	@PostMapping("/team/{teamId}/formation")
	public String aendereFormation(Model model, Authentication auth, @PathVariable("teamId") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setFormationsTyp(aktuellesTeam.getFormationsTyp());
		teamService.aenderFormationEinesTeams(team, spielerService.findeAlleSpielerEinesTeams(team));
		
		return "redirect:/team/{teamId}";
	}
	
	@PostMapping("/team/{teamId}/einsatz")
	public String aendereEinsatz(Model model, Authentication auth, @PathVariable("teamId") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setEinsatzTyp(aktuellesTeam.getEinsatzTyp());
		teamService.aendereEinsatzEinesTeams(team);
		
		return "redirect:/team/{teamId}";
	}
	
	@PostMapping("/team/{teamId}/ausrichtung")
	public String aendereAusrichtung(Model model, Authentication auth, @PathVariable("teamId") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setAusrichtungsTyp(aktuellesTeam.getAusrichtungsTyp());
		teamService.aktualisiereTeam(team);
		
		return "redirect:/team/{teamId}";
	}
	
	@PostMapping("/team/{teamId}/einwechseln")
	public String aendereFormation(Model model, Authentication auth, @PathVariable("teamId") Long id, @ModelAttribute("einzuwechselnderSpieler") Spieler spieler) {
		Spieler einzugewechselterSpieler = spielerService.findeSpieler(spieler.getId());
		LOG.info("{}, {}, {}, {}, {}", einzugewechselterSpieler.getId(), einzugewechselterSpieler.getName(), einzugewechselterSpieler.getAlter(), 
				einzugewechselterSpieler.getPosition().getPositionsName(), einzugewechselterSpieler.getTeam());
		spielerService.wechsleSpielerEin(einzugewechselterSpieler, spieler.getAufstellungsPositionsTyp());
		
		return "redirect:/team/{teamId}";
	}
	
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
	
	public SummeSpielerWerte erstelleSummeDerSpielerWerteListe(List<Spieler> spielerDesTeams) {
		SummeSpielerWerte summeDerSpielerWerte = new SummeSpielerWerte();
		
		for(Spieler spieler : spielerDesTeams) {
			summeDerSpielerWerte.setAlter(summeDerSpielerWerte.getAlter() + spieler.getAlter());
			summeDerSpielerWerte.setErfahrung(summeDerSpielerWerte.getErfahrung() + spieler.getErfahrung());
			summeDerSpielerWerte.setGehalt(summeDerSpielerWerte.getGehalt() + spieler.getGehalt());
			summeDerSpielerWerte.setMotivation(summeDerSpielerWerte.getMotivation() + spieler.getMotivation());
			summeDerSpielerWerte.setReinStaerke(summeDerSpielerWerte.getReinStaerke() + spieler.getSpielerStaerke().getReinStaerke());
			summeDerSpielerWerte.setStaerke(summeDerSpielerWerte.getStaerke() + spieler.getSpielerStaerke().getStaerke());
			summeDerSpielerWerte.setZuwachs(summeDerSpielerWerte.getZuwachs() + spieler.getSpielerZuwachs());
		}
		return summeDerSpielerWerte;
	}
}
