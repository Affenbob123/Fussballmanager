package fussballmanager.mvc.auktionshaus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.exceptions.DatumFormatException;
import fussballmanager.helper.DateHelper;
import fussballmanager.service.auktionshaus.AuktionshausEintrag;
import fussballmanager.service.auktionshaus.AuktionshausEintragService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class AuktionshausController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuktionshausController.class);
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuktionshausEintragService auktionshausEintragService;

	@GetMapping("/auktionshaus")
	public String getAuktionshaus(Model model, Authentication auth, @ModelAttribute("auktionshausAuswahl") AuktionshausEintrag auktionshausAuswahl) {
		model.addAttribute("auktionshausAuswahl", auktionshausAuswahl);
		model.addAttribute("alleAuktionshausEintraegeFuerProtageOderGeld", auktionshausEintragService.findeAlleAktionshausEintraegeFuerProtage(auktionshausAuswahl.isFuerProtage()));
		model.addAttribute("dateHelper", new DateHelper());
		
		return "auktionshaus/auktionshaus";
	}
	
	@PostMapping("/auktionshaus")
	public String waehleAuktionshaus(Model model, Authentication auth, RedirectAttributes redirectAttribtues,
			@ModelAttribute("auktionshausAuswahl") AuktionshausEintrag auktionshausAuswahl) {
		redirectAttribtues.addFlashAttribute("auktionshausAuswahl", auktionshausAuswahl);
		return "redirect:/auktionshaus";
	}
	
	@PostMapping("/auktionshaus/{auktionshausEintragId}")
	public String gebeGebotFuerEinTeamAb(Model model, Authentication auth, RedirectAttributes redirectAttribtues, @PathVariable("auktionshausEintragId") Long auktionshausEintragId,
			@ModelAttribute("auktionshausEintrag") AuktionshausEintrag neuesGebot) {
		AuktionshausEintrag auktionshausEintrag = auktionshausEintragService.findeAuktionshausEintrag(auktionshausEintragId);
		if(auktionshausEintrag.getAktuellesGebot() >= neuesGebot.getAktuellesGebot()) {
			redirectAttribtues.addFlashAttribute("errorMessage", "Ungültiges Gebot! Du musst mehr bieten als das aktuelle Höchstgebot!");
			return "redirect:/auktionshaus";
		}
		User aktuellerUser = userService.findeUser(auth.getName());
		auktionshausEintrag.setHoechstBieter(aktuellerUser);
		auktionshausEintrag.setAktuellesGebot(neuesGebot.getAktuellesGebot());
		auktionshausEintragService.aktualisiereAuktionshausEintrag(auktionshausEintrag);
		if(auktionshausEintrag.getAktuellesGebot() >= auktionshausEintrag.getSofortKaufPreis()) {
			auktionshausEintragService.auktionshausEintragBeenden(auktionshausEintrag);
		}
		return "redirect:/auktionshaus";
	}
	
	@PostMapping("/auktionshaus/{auktionshausEintragId}/abbrechen")
	public String brecheAuktionshausEintragAb(Model model, Authentication auth, @PathVariable("auktionshausEintragId") Long auktionshausEintragId) {
		auktionshausEintragService.loescheAuktionshausEintrag(auktionshausEintragService.findeAuktionshausEintrag(auktionshausEintragId));
		return "redirect:/auktionshaus"; 
	}
	
	@GetMapping("/auktionshaus/teameinstellen")
	public String getAuktionshausTeamEinstellen(Model model, Authentication auth, RedirectAttributes redirecAttribute) {
		List<Team> alleTeamsEinesUsers = teamService.findeAlleTeamsEinesUsers(userService.findeUser(auth.getName()));
		List<AuktionshausEintrag> alleAuktionshausEintraege = auktionshausEintragService.findeAlleAuktionshausEintraege();
		for(AuktionshausEintrag auktionshausEintrag : alleAuktionshausEintraege) {
			if(alleTeamsEinesUsers.contains(auktionshausEintrag.getTeam())) {
				alleTeamsEinesUsers.remove(auktionshausEintrag.getTeam());
			}
		}
		
		if(alleTeamsEinesUsers.size() < 2) {
			redirecAttribute.addFlashAttribute("errorMessage", "Du benötigst mindestens zwei Teams um ein Team ins Auktionshaus stellen zu können!");
			return "redirect:/auktionshaus";
		}
		model.addAttribute("naechstenSiebenSpieltage", spieltagService.findeNaechstenSiebenSpieltage());
		model.addAttribute("newAktionshausEintragHelper", new AuktionshausEintragHelper());
		model.addAttribute("teamsDesUsers", alleTeamsEinesUsers);
		
		return "auktionshaus/auktionshauseinstellen";
	}
	
	@PostMapping("/auktionshaus/teameinstellen")
	public String stelleTeamInsAuktionshausEin(Model model, Authentication auth, RedirectAttributes redirecAttribute, 
			 @ModelAttribute("newAktionshausEintragHelper") @Valid AuktionshausEintragHelper auktionshausEintragHelper, BindingResult bindingResult) {
		DateHelper dateHelper = new DateHelper();
		LocalDateTime aktuellesDatum = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
		if(auktionshausEintragHelper.getBeschreibung().equals("")) {
			redirecAttribute.addFlashAttribute("errorMessage", "Das Beschreibungsfeld darf nicht leer gelassen werden!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		if(auktionshausEintragHelper.getBeschreibung().length() > 300) {
			redirecAttribute.addFlashAttribute("errorMessage", "Es können maximal 300 Zeichen verwendet werden!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		if(auktionshausEintragHelper.getStartGebotPreis() < 0) {
			redirecAttribute.addFlashAttribute("errorMessage", "Der Startgebotpreis darf nicht kleiner 0 sein!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		if(auktionshausEintragHelper.getSofortKaufPreis() <= auktionshausEintragHelper.getStartGebotPreis()) {
			redirecAttribute.addFlashAttribute("errorMessage", "Der Sofortkaufpreis darf nicht kleiner oder gleich dem Startgebotpreis sein!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		if(auktionshausEintragHelper.getAblaufDatum().isEmpty()) {
			redirecAttribute.addFlashAttribute("errorMessage", "Datum darf nicht leer sein!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		try {
			LocalDateTime eingabeDatum = dateHelper.htmlStringToDate(auktionshausEintragHelper.getAblaufDatum());
			if(aktuellesDatum.plusHours(24).isAfter(eingabeDatum)) {
				redirecAttribute.addFlashAttribute("errorMessage", "Eine Auktion muss mindestens 24 Stunden dauern!");
				return "redirect:/auktionshaus/teameinstellen";
			}
			if(aktuellesDatum.plusDays(7).isBefore(eingabeDatum)) {
				redirecAttribute.addFlashAttribute("errorMessage", "Eine Auktion darf maximal 7 Tage lang dauern!");
				return "redirect:/auktionshaus/teameinstellen";
			}
		} catch(DateTimeParseException e) {
			redirecAttribute.addFlashAttribute("errorMessage", "Das eingegebene Datum ist ungültig!");
			return "redirect:/auktionshaus/teameinstellen";
		}
		if(bindingResult.hasErrors()) {
			redirecAttribute.addFlashAttribute("errorMessage", "Überprüfe deine Eingaben!");
			return "redirect:/auktionshaus/teameinstellen";
		}
	
		AuktionshausEintrag auktionshausEintrag = new AuktionshausEintrag();
		auktionshausEintrag.setBeschreibung(auktionshausEintragHelper.getBeschreibung());
		auktionshausEintrag.setFuerProtage(auktionshausEintragHelper.isFuerProtage());
		auktionshausEintrag.setTeam(auktionshausEintragHelper.getTeam());
		auktionshausEintrag.setStartGebotPreis(auktionshausEintragHelper.getStartGebotPreis());
		auktionshausEintrag.setSofortKaufPreis(auktionshausEintragHelper.getSofortKaufPreis());
		auktionshausEintrag.setAblaufDatum(dateHelper.htmlStringToDate(auktionshausEintragHelper.getAblaufDatum()));
		auktionshausEintragService.legeAuktionshausEintragAn(auktionshausEintrag);

		LOG.info("Team ins Auktionshaus eingestellt: Team:{}, SK:{}, Ablaufzeit: {}", auktionshausEintragHelper.getTeam().getName(), auktionshausEintragHelper.getSofortKaufPreis(),
				auktionshausEintragHelper.getAblaufDatum());
		return "redirect:/auktionshaus";
	}
}
