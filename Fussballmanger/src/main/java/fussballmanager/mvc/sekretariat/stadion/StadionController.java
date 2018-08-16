package fussballmanager.mvc.sekretariat.stadion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.team.stadion.Stadion;
import fussballmanager.service.team.stadion.StadionAusbauTypen;
import fussballmanager.service.team.stadion.StadionService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class StadionController {

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
	
	@Autowired
	StadionService stadionService;
	
	@GetMapping("/team/{id}/stadion")
	public String getStadion(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		model.addAttribute("stadionAusbauTyp", null);
		model.addAttribute("stadion", new Stadion());
		model.addAttribute("imbissTyp", StadionAusbauTypen.IMBISS);
		return "sekretariat/stadion";
	}
	
	@PostMapping("/team/{id}/stadion")
	public String baueStadionAus(Model model, Authentication auth,  @ModelAttribute("stadion") Stadion stadion) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Stadion s = aktuellerUser.getAktuellesTeam().getStadion();
		s.setAktuellAusgebauterTyp(stadion.getAktuellAusgebauterTyp());
		s.setUebrigeAusbauTage(s.getAktuellAusgebauterTyp().getAusbauDauer());
		stadionService.aktualisiereStadion(s);
		return "redirect:/team/{id}/stadion";
	}
	
	@PostMapping("/team/{id}/stadion/abbrechen")
	public String brecheStadionAusbauAb(Model model, Authentication auth,  @ModelAttribute("stadion") Stadion stadion) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Stadion s = aktuellerUser.getAktuellesTeam().getStadion();
		s.setAktuellAusgebauterTyp(null);
		stadionService.aktualisiereStadion(s);
		return "redirect:/team/{id}/stadion";
	}
	
	@PostMapping("/team/{id}/stadion/sitzplaetze")
	public String baueSitzplaetzeAus(Model model, Authentication auth, @ModelAttribute("stadion") Stadion stadion) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Stadion s = aktuellerUser.getAktuellesTeam().getStadion();
		s.setSitzplatzAusbauTage(stadion.getSitzplatzAusbauTage());
		stadionService.aktualisiereStadion(s);
		return "redirect:/team/{id}/stadion";
	}
	
	@PostMapping("/team/{id}/stadion/sitzplaetze/abbrechen")
	public String brecheSitzplatzausbauAb(Model model, Authentication auth,  @ModelAttribute("stadion") Stadion stadion) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Stadion s = aktuellerUser.getAktuellesTeam().getStadion();
		s.setSitzplatzAusbauTage(stadion.getSitzplatzAusbauTage());
		stadionService.aktualisiereStadion(s);
		return "redirect:/team/{id}/stadion";
	}
}
