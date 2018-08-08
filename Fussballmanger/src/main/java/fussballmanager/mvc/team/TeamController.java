package fussballmanager.mvc.team;

import java.text.DecimalFormat;
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

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
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
	
	@GetMapping("/team/{id}")
	public String getTeamListe(Model model, Authentication auth, @PathVariable("id") Long id) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Team aktuellesTeam = teamService.findeTeam(id);
		
		aktuellerUser.setAktuellesTeam(aktuellesTeam);
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		List<Spieler> alleSpielerEinesTeams = spielerService.findeAlleSpielerEinesTeams(aktuellesTeam);
		DecimalFormat zahlenFormat = new DecimalFormat("#.0");
		
		model.addAttribute("zahlenFormat", zahlenFormat);
		model.addAttribute("alleSpielerDesAktuellenTeams", alleSpielerEinesTeams);
		model.addAttribute("alleFormationsTypen", FormationsTypen.values());
		model.addAttribute("alleEinsatzTypen", EinsatzTypen.values());
		model.addAttribute("alleAusrichtungsTypen", AusrichtungsTypen.values());
		model.addAttribute("alleAufstellungsPositionsTypen", aktuellesTeam.getFormationsTyp().getAufstellungsPositionsTypen());
		model.addAttribute("alleSpielerAufErsatzbank", spielerService.findeAlleSpielerEinesTeamsAufErsatzbank(aktuellesTeam));
		model.addAttribute("einzuwechselnderSpieler", new Spieler());
		model.addAttribute("anzahlDerEinwechslungen", teamService.findeTeam(id).getAnzahlAuswechselungen());
		
		return "spielerliste";
	}
	
	@PostMapping("/team/{id}/formation")
	public String aendereFormation(Model model, Authentication auth, @PathVariable("id") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setFormationsTyp(aktuellesTeam.getFormationsTyp());
		teamService.aenderFormationEinesTeams(team);
		
		return "redirect:/team/{id}";
	}
	
	@PostMapping("/team/{id}/einsatz")
	public String aendereEinsatz(Model model, Authentication auth, @PathVariable("id") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setEinsatzTyp(aktuellesTeam.getEinsatzTyp());
		teamService.aendereEinsatzEinesTeams(team);
		
		return "redirect:/team/{id}";
	}
	
	@PostMapping("/team/{id}/ausrichtung")
	public String aendereAusrichtung(Model model, Authentication auth, @PathVariable("id") Long id, @ModelAttribute("aktuellesTeam") Team aktuellesTeam) {
		Team team = teamService.findeTeam(id);
		team.setAusrichtungsTyp(aktuellesTeam.getAusrichtungsTyp());
		teamService.aktualisiereTeam(team);
		
		return "redirect:/team/{id}";
	}
	
	@PostMapping("/team/{id}/einwechseln")
	public String aendereFormation(Model model, Authentication auth, @PathVariable("id") Long id, @ModelAttribute("einzuwechselnderSpieler") Spieler spieler) {
		Spieler einzugewechselterSpieler = spielerService.findeSpieler(spieler.getId());
		LOG.info("{}, {}, {}, {}, {}", einzugewechselterSpieler.getId(), einzugewechselterSpieler.getName(), einzugewechselterSpieler.getAlter(), 
				einzugewechselterSpieler.getPosition().getPositionsName(), einzugewechselterSpieler.getTeam());
		spielerService.wechsleSpielerEin(einzugewechselterSpieler, spieler.getAufstellungsPositionsTyp());
		
		return "redirect:/team/{id}";
	}
}
