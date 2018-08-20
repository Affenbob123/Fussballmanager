package fussballmanager.mvc.chat;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fussballmanager.mvc.sekretariat.nachricht.NachrichtController;
import fussballmanager.service.chat.Chat;
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
public class ChatController {

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
	
	@GetMapping("/chat")
	public String getNachrichten(Model model, Authentication auth, @ModelAttribute("aktiverChat") Chat chat) {
		User aktuellerUser = userService.findeUser(auth.getName());
		Chat aktiverChat = chat;
		if(chat.getUser() == null) {
			aktiverChat = chatService.findeAlleChatsEinesUsers(aktuellerUser).get(0);
		}
		model.addAttribute("aktiverChat", aktiverChat);
		model.addAttribute("neuerAktiverChat", new ChatNachrichtWrapper());
		model.addAttribute("chatNachrichtWrapper", new ChatNachrichtWrapper());
		model.addAttribute("alleChatsEinesUsers", chatService.findeAlleChatsEinesUsers(aktuellerUser));
		model.addAttribute("alleNachrichtenDesAktivenChat", aktiverChat.getNachrichten());
		return "chat/chat";
	}

	@PostMapping("/chat")
	public String sendeNachricht(Model model, Authentication auth, RedirectAttributes redirectAttributes, @ModelAttribute("chatNachrichtWrapper") ChatNachrichtWrapper chatNachrichtWrapper) {
		Chat chat = chatService.findeChat(chatNachrichtWrapper.getChat().getId());
		User user = userService.findeUser(auth.getName());
		chatService.erstelleNeueNachricht(user, chat, chatNachrichtWrapper.getNachricht());
		
		redirectAttributes.addFlashAttribute("aktiverChat", chat);
		return "redirect:/chat";
	}
	
	@PostMapping("/chat/auswahl")
	public String waehleChatAus(Model model, Authentication auth, RedirectAttributes redirectAttributes, @ModelAttribute("neuerAktiverChat") ChatNachrichtWrapper chatNachrichtWrapper) {
		Chat aktuellerChat = chatService.findeChat(chatNachrichtWrapper.getChat().getId());
		
		redirectAttributes.addFlashAttribute("aktiverChat", aktuellerChat);
		return "redirect:/chat";
	}
	
	@GetMapping("/chat/neu")
	public String getChatNeu(Model model, Authentication auth) {
		List<User> alleUser = userService.findeAlleNormalenUser();
		alleUser.remove(userService.findeUser(auth.getName()));
		
		Chat chatErstellen = new Chat();
		model.addAttribute("chatErstellen", chatErstellen);
		model.addAttribute("alleUser", alleUser);
		
		return "chat/neuerchat";
	}
	
	@PostMapping("/chat/neu")
	public String erstelleChat(Model model, Authentication auth, @ModelAttribute("chatErstellen") Chat neuerChat) {
		neuerChat.getUser().add(userService.findeUser(auth.getName()));
		chatService.legeChatAn(neuerChat);
		return "redirect:/chat";
	}
	
	@GetMapping("/chat/{chatId}/bearbeiten")
	public String getChatBearbeiten(Model model, Authentication auth, @PathVariable("chatId") Long chatId) {
		List<User> alleUser = userService.findeAlleNormalenUser();
		alleUser.remove(userService.findeUser(auth.getName()));
		Chat zuBearbeitendeChat = chatService.findeChat(chatId);
		
		model.addAttribute("zuBearbeitendeChat", zuBearbeitendeChat);
		model.addAttribute("alleUser", alleUser);
		
		return "chat/bearbeitechat";
	}
	
	@PostMapping("/chat/{chatId}/bearbeiten")
	public String bearbeiteChat(Model model, Authentication auth, @PathVariable("chatId") Long chatId, @ModelAttribute("chatErstellen") Chat bearbeiteterChat) {
		Chat chat = chatService.findeChat(chatId);
		chat.setChatName(bearbeiteterChat.getChatName());
		List<User> userImChat = bearbeiteterChat.getUser();
		userImChat.add(userService.findeUser(auth.getName()));
		chat.setUser(userImChat);
		chatService.aktualisiereChat(chat);
		return "redirect:/chat";
	}
	
	@PostMapping("/chat/{chatId}/loeschen")
	public String loescheChat(Model model, Authentication auth, @PathVariable("chatId") Long chatId) {
		chatService.loescheChat(chatService.findeChat(chatId));
		return "redirect:/chat";
	}
}
