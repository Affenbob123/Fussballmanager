package fussballmanager.mvc.sekretariat.statistik;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.helper.SpielstatusHelper;
import fussballmanager.mvc.transfermarkt.SpielerSuche;
import fussballmanager.service.land.LaenderNamenTypen;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.PositionenTypen;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class StatistikController {

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
	
	@GetMapping("/statistik/{seite}")
	public String getStatistik(Model model, Authentication auth, @PathVariable("seite") int seite,
			@ModelAttribute("statistikFormular") StatistikFormular statistikFormular) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("spielstatusHelper", new SpielstatusHelper());
		model.addAttribute("aktuellesTeam", aktuellerUser.getAktuellesTeam());
		model.addAttribute("aktuelleSaison", saisonService.findeAktuelleSaison());
		model.addAttribute("aktuellerSpieltag", spieltagService.findeAktuellenSpieltag());
		
		DecimalFormat zahlenFormat = new DecimalFormat("0.0");
		List<Spieler> alleSpielerNachSuche = spielerService.findeZwanzigSpielerNachSortierTyp(statistikFormular);
		
		model.addAttribute("zahlenFormat", zahlenFormat);
		model.addAttribute("statistikFormular", statistikFormular);
		model.addAttribute("positionenTypen", PositionenTypen.values());
		model.addAttribute("laenderNamenTypen", LaenderNamenTypen.values());
		model.addAttribute("alterListe", getAlterListe());
		model.addAttribute("sortierTypen", SortierTypen.values());
		model.addAttribute("alleSpielerNachSuche", alleSpielerNachSuche);
		return "sekretariat/statistik";
	}
	
	@PostMapping("/statistik/{seite}")
	public String filtereStatistik(Model model, Authentication auth, RedirectAttributes redirectAttributes,
			@ModelAttribute("statistikFormular") StatistikFormular statistikFormular) {
		redirectAttributes.addFlashAttribute("statistikFormular", statistikFormular);	
		return "redirect:/statistik/{seite}";
	}
	
	private List<Integer> getAlterListe() {
		List<Integer> alterListe = new ArrayList<>();
		for(int i = 14; i < 35; i++) {
			alterListe.add(i);
		}
		return alterListe;
	}
}
