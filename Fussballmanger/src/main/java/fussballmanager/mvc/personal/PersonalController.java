package fussballmanager.mvc.personal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import fussballmanager.service.personal.PersonalService;

@Controller
public class PersonalController {
	
	@Autowired
	PersonalService personalService;

	@GetMapping("/personal/{personalId}")
	public String getPersonal(Model model, Authentication auth, @PathVariable("personalId") Long personalId) {
		model.addAttribute("personal", personalService.findePersonal(personalId));
		
		return "personal";
	}
	
	@PostMapping("/personal/{personalId}/transfermarkt")
	public String personalAufTransfermarktStellen(Model model, Authentication auth, @PathVariable("personalId") Long personalId) {	
		
		return "redirect:/personal/{personalId}";
	}
	
	@PostMapping("/personal/{personalId}/transfermarkt/entfernen")
	public String personalVomTransfermarktEntfernen(Model model, Authentication auth, @PathVariable("personalId") Long personalId) {
		
		return "redirect:/personal/{personalId}";
	}
	
	@PostMapping("/personal/{personalId}/entlassen")
	public String entlassePersonal(Model model, Authentication auth, @PathVariable("personalId") Long personalId,
			HttpServletRequest request) {
		personalService.loeschePersonal(personalService.findePersonal(personalId));
		String referer = request.getHeader("Referer");
		
		return "redirect:/" + referer;
	}
}
