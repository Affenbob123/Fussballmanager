package fussballmanager.mvc.kader;

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
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class AufstellungsController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AufstellungsController.class);
	
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
	public String getAufstellung(Model model, Authentication auth, @PathVariable("teamId") Long teamId) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		DecimalFormat zahlenFormat = new DecimalFormat("0.0");
		Team team = teamService.findeTeam(teamId);
		List<Spieler> alleSpielerAufSpielfeld = spielerService.findeAlleSpielerEinesTeamsInAufstellung(team);
		List<Spieler> alleSpielerNichtAufSpielfeld = spielerService.findeAlleSpielerEinesTeamsAufErsatzbank(team);
		
		model.addAttribute("zahlenFormat", zahlenFormat);
		model.addAttribute("alleSpielerAufSpielfeld", alleSpielerAufSpielfeld);
		model.addAttribute("alleSpielerNichtAufSpielfeld", alleSpielerNichtAufSpielfeld);
		
		for(Spieler spieler : alleSpielerAufSpielfeld) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TW)) {
				model.addAttribute("tw", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LV)) {
				model.addAttribute("lv", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIV)) {
				model.addAttribute("liv", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIB)) {
				model.addAttribute("lib", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RIV)) {
				model.addAttribute("riv", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RV)) {
				model.addAttribute("rv", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LM)) {
				model.addAttribute("lm", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.DM)) {
				model.addAttribute("dm", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RM)) {
				model.addAttribute("rm", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ZM)) {
				model.addAttribute("zm", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.OM)) {
				model.addAttribute("om", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LS)) {
				model.addAttribute("ls", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.MS)) {
				model.addAttribute("ms", spieler);
			}
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RS)) {
				model.addAttribute("rs", spieler);
			}
		}
		model.addAttribute("aufstellungsPositionsTypTW", AufstellungsPositionsTypen.TW);
		model.addAttribute("aufstellungsPositionsTypLV", AufstellungsPositionsTypen.LV);
		model.addAttribute("aufstellungsPositionsTypLIV", AufstellungsPositionsTypen.LIV);
		model.addAttribute("aufstellungsPositionsTypLIB", AufstellungsPositionsTypen.LIB);
		model.addAttribute("aufstellungsPositionsTypRIV", AufstellungsPositionsTypen.RIV);
		model.addAttribute("aufstellungsPositionsTypRV", AufstellungsPositionsTypen.RV);
		model.addAttribute("aufstellungsPositionsTypLM", AufstellungsPositionsTypen.LM);
		model.addAttribute("aufstellungsPositionsTypDM", AufstellungsPositionsTypen.DM);
		model.addAttribute("aufstellungsPositionsTypRM", AufstellungsPositionsTypen.RM);
		model.addAttribute("aufstellungsPositionsTypZM", AufstellungsPositionsTypen.ZM);
		model.addAttribute("aufstellungsPositionsTypOM", AufstellungsPositionsTypen.OM);
		model.addAttribute("aufstellungsPositionsTypLS", AufstellungsPositionsTypen.LS);
		model.addAttribute("aufstellungsPositionsTypMS", AufstellungsPositionsTypen.MS);
		model.addAttribute("aufstellungsPositionsTypRS", AufstellungsPositionsTypen.RS);
		
		return "kader/aufstellung";
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
}
