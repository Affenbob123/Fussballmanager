package fussballmanager.mvc.protage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProtageController {

	private static final Logger LOG = LoggerFactory.getLogger(ProtageController.class);
	
	@GetMapping("/protage")
	public String getProtage(Model model, Authentication auth) {
		ProtageWrapper protageWrapper = new ProtageWrapper();
		
		model.addAttribute("zahlungsMoeglichkeitenTypen", ZahlungsmoeglichkeitenTypen.values());
		model.addAttribute("protageWrapper", protageWrapper);
		
		return "protage";
	}
	
	@PostMapping("/protage")
	public String kaufeProtage(Model model, Authentication auth, @ModelAttribute("protageWrapper") ProtageWrapper protageWrapper) {
		LOG.info("{}, {}", protageWrapper.getProtage(), protageWrapper.getZahlungsmoeglichkeit());
		return "redirect:/protage";
	}
}
