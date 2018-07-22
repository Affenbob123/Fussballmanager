package fussballmanager.mvc.team;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class TeamController {
	
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
	
	@GetMapping("/")
	public String getTeamListe(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		List<Team> alleTeamsEinesUsers = teamService.findeAlleTeamsEinesUsers(aktuellerUser);
		
		model.addAttribute("alleTeamsDesAktuellenUsers", alleTeamsEinesUsers);
		
		
		return "teamliste";
	}
	
	@GetMapping("/team/{id}")
	public String getTeamListe(Model model, Authentication auth, @PathVariable("id") Long id) {
		Team aktuellesTeam = teamService.findeTeam(id);
		List<Spieler> alleSpielerEinesTeams = spielerService.findeAlleSpielerEinesTeams(aktuellesTeam);
		
		model.addAttribute("alleSpielerDesAktuellenTeams", alleSpielerEinesTeams);
		
		return "spielerliste";
	}
}
