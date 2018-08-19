package fussballmanager.mvc.sekretariat;
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
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class SekretariatController {
	
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
	
	@GetMapping("/")
	public String getTeamListe(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		List<Team> alleTeamsEinesUsers = teamService.findeAlleTeamsEinesUsers(aktuellerUser);
		
		model.addAttribute("alleTeamsDesAktuellenUsers", alleTeamsEinesUsers);
		
		return "sekretariat/teamliste";
	}
	
	@PostMapping("/team/{id}/loeschen")
	public String teamLoeschen(Model model, Authentication auth, @PathVariable("id") Long id) {
		for(Team t : teamService.findeAlleTeamsEinesUsers(userService.findeUser(auth.getName()))) {
			if(t.equals(teamService.findeTeam(id))) {
				Team team = teamService.findeTeam(id);
				team.setUser(null);
				team.setName("Unbenanntes Team");
				teamService.aktualisiereTeam(team);
				return "redirect:/";
			}
		}
		return "redirect:/";
	}
	
	@PostMapping("/team/neu")
	public String neuesTeam(Model model, Authentication auth) {
		User user = userService.findeUser(auth.getName());
		Team team = teamService.findeErstesDummyTeam(user.getLand());
		
		team.setUser(user);
		teamService.aktualisiereTeam(team);
		//TODO wenn land voll ist
		//TODO Dummy teams
		return "redirect:/";
	}
	
	@GetMapping("/teams/umbenennen")
	public String getTeamListeZumUmbenennen(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		TeamListeWrapper teamListWrapper = new TeamListeWrapper();
		
		List<Team> alleTeamsEinesUsers = teamService.findeAlleTeamsEinesUsers(aktuellerUser);
		teamListWrapper.setTeamList(alleTeamsEinesUsers);
		
		model.addAttribute("alleTeamsDesAktuellenUsers", alleTeamsEinesUsers);
		model.addAttribute("teamWrapper", teamListWrapper);
		
		return "sekretariat/teamlistezumumbenennen";
	}
	
	@PostMapping("/teams/umbenennen")
	public String teamUmbennenen(Model model, Authentication auth, @ModelAttribute("teamWrapper") TeamListeWrapper teamWrapper) {		
		List<Team> alleTeamsEinesUsers = teamWrapper.getTeamList();
		
		for(Team t : alleTeamsEinesUsers) {
			Team team = teamService.findeTeam(t.getId());
			team.setName(t.getName());
			teamService.aktualisiereTeam(team);
		}
		return "redirect:/";
	}
}

