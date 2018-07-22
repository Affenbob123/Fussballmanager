package fussballmanager.mvc.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String getLoginSeite() {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginVersuch() {
		return "redirect:/";
	}
}
