package fussballmanager.mvc.liveticker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.Spiel;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class LiveTickerController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;

	@GetMapping("/liveticker")
	public String getLiveticker(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		//TODO für alle teams des Users
		Team hauptteam = teamService.findeAlleTeamsEinesUsers(aktuellerUser).get(0);
		
		List<Spiel> alleSpieleDesUsers = 
				spielService.findeAlleSpieleEinesSpieltagesEinesTeams(spieltagService.findeAktuellenSpieltag(), hauptteam);
		model.addAttribute("alleSpieleDesUsers", alleSpieleDesUsers);
		
		return "liveticker";
	}
}
