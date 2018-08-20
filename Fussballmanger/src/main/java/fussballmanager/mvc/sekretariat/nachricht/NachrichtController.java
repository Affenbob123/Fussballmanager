package fussballmanager.mvc.sekretariat.nachricht;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.chat.ChatService;
import fussballmanager.service.chat.nachricht.NachrichtService;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Controller
public class NachrichtController {
	
	private static final Logger LOG = LoggerFactory.getLogger(NachrichtController.class);
	
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
	NachrichtService nachrichtService;
	
	@Autowired
	ChatService chatService;
	
	@GetMapping("/nachrichten")
	public String getNachrichten(Model model, Authentication auth) {
		User aktuellerUser = userService.findeUser(auth.getName());
		
		model.addAttribute("alleChatsEinesUsers", chatService.findeAlleChatsEinesUsers(aktuellerUser));
		return "sekretariat/nachrichten";
	}

	@PostMapping("/nachricht")
	public String sendeNachricht(Model model, Authentication auth) {
		return "redirect:/nachrichten";
	}
	
	@PostMapping("/nachrichten/loeschen")
	public String loescheNachrichten(Model model, Authentication auth) {
		return "redirect:/nachrichten";
	}
}
