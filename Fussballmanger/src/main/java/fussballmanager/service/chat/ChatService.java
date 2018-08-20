package fussballmanager.service.chat;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.mvc.sekretariat.nachricht.NachrichtController;
import fussballmanager.service.chat.nachricht.Nachricht;
import fussballmanager.service.chat.nachricht.NachrichtRepository;
import fussballmanager.service.chat.nachricht.NachrichtService;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.Spieltag;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Service
@Transactional
public class ChatService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChatService.class);

	@Autowired
	UserService userService;
	
	@Autowired
	ChatRepository chatRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	NachrichtService nachrichtService;
	
	@Autowired
	SpieltagService spieltagService;
	
	public Chat findeChat(Long id) {
		return chatRepository.getOne(id);
	}
	
	public List<Chat> findeAlleChats() {
		return chatRepository.findAll();
	}
	
	public void legeChatAn(Chat chat) {
		chatRepository.save(chat);
	}
	
	public void aktualisiereChat(Chat chat) {
		chatRepository.save(chat);
	}
	
	public void loescheChat(Chat chat) {
		chatRepository.delete(chat);
	}

	public List<Chat> findeAlleChatsEinesUsers(User user) {
		List<Chat> alleChatsDesUsers = chatRepository.findByUser(user);
		Collections.sort(alleChatsDesUsers);
		return alleChatsDesUsers;
	}
	
	public Chat findeChatEinesUsersMitLetzerNachricht(User user) {
		List<Chat> alleChatsDesUsers = findeAlleChatsEinesUsers(user);
		LOG.info("hallo{}", alleChatsDesUsers.size());

		Collections.sort(alleChatsDesUsers);
		
		return alleChatsDesUsers.get(0);
	}

	public void erstelleNeueNachricht(User user, Chat chat, String nachricht) {
		Nachricht n = new Nachricht(user, spieltagService.findeAktuellenSpieltag(), nachricht, LocalTime.now(ZoneId.of("Europe/Berlin")));
		Chat c = findeChat(chat.getId());
		nachrichtService.legeNachrichtAn(n);
		if(c.getNachrichten().size() > 30) {
			Nachricht zuloeschendeNachricht = c.getNachrichten().get(0);
			c.getNachrichten().remove(0);
			nachrichtService.loescheNachricht(zuloeschendeNachricht);
		}
		c.getNachrichten().add(n);
		aktualisiereChat(chat);
	}

	public void erstelleAlleChat() {
		Chat alleChat = new Chat();
		alleChat.setChatName("All-Chat");
		legeChatAn(alleChat);
	}
	
	public void fuegeUserAlleChatHinzu(User user) {
		Chat alleChat = findeChat(1L);
		alleChat.getUser().add(user);
		
		aktualisiereChat(alleChat);
	}
}
