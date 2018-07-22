package fussballmanager.mvc.spieler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import fussballmanager.service.spieler.SpielerService;

@Controller
public class SpielerController {
	
	@Autowired
	SpielerService spielerService;
	
	@GetMapping("/spieler/{id}")
	public String getSpieler(Model model, Authentication auth, @PathVariable("id") Long id) {
		model.addAttribute("spieler", spielerService.findeSpieler(id));
		
		return "spieler";
	}
}