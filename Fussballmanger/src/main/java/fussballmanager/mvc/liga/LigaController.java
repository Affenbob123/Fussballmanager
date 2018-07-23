package fussballmanager.mvc.liga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spielereignisse.SpielEreignisService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class LigaController {
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	LigaService ligaService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	SpielEreignisService spielEreignisService;

	@GetMapping("/liga/{land}/{ligaName}")
	public String getLiga(Model model, Authentication auth, @PathVariable("land") String land, @PathVariable("ligaName") String ligaName) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("alleTeamsDerAktuellenLiga", erstelleLigaTabelle(land, ligaName));
		model.addAttribute("alleSpieleEinerLiga", spielService.findeAlleSpieleEinerLiga(ligaService.findeLiga(land, ligaName)));
		model.addAttribute("alleSpielEreignisseDerLiga", spielEreignisService.findeAlleSpielEreignisseEinerLiga(ligaService.findeLiga(land, ligaName)));
		
		return "tabelle";
	}
	
	@GetMapping("/ligen")
	public String getAlleLigen(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		
		model.addAttribute("alleLigen", ligaService.findeAlleLigen());
		
		return "ligenliste";
	}
	
	public List<LigaEintrag> erstelleLigaTabelle(String land, String ligaName) {
		List<LigaEintrag> ligaEintraege = new ArrayList<>();
		for (Team team : teamService.findeAlleTeamsEinerLiga(ligaService.findeLiga(land, ligaName))) {
			ligaEintraege.add(erstelleEineZeileDerLigaTabelle(team));
		}
		return ligaEintraege;
	}
	
	public LigaEintrag erstelleEineZeileDerLigaTabelle(Team team) {
		LigaEintrag ligaEintrag = new LigaEintrag();
		
		ligaEintrag.setId(team.getId());
		ligaEintrag.setPlatzierung(1);
		ligaEintrag.setTeamName(team.getName());
		//TODO Vervollständigen
		return ligaEintrag;
	}
	
}
