package fussballmanager.service.chat.nachricht;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fussballmanager.service.chat.Chat;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.staerke.SpielerReinStaerke;
import fussballmanager.service.spieler.staerke.SpielerReinStaerkeRepository;
import fussballmanager.service.tabelle.TabellenEintragService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Service
@Transactional
public class NachrichtService {

	@Autowired
	UserService userService;
	
	@Autowired
	NachrichtRepository nachrichtRepository;
	
	@Autowired
	SpielService spielService;
	
	@Autowired
	TabellenEintragService tabellenEintragService;
	
	@Autowired
	SaisonService saisonService;
	
	public Nachricht findeNachricht(Long id) {
		return nachrichtRepository.getOne(id);
	}
	
	public List<Nachricht> findeNachrichten() {
		return nachrichtRepository.findAll();
	}
	
	public void legeNachrichtAn(Nachricht nachricht) {
		nachrichtRepository.save(nachricht);
	}
	
	public void aktualisiereNachricht(Nachricht nachricht) {
		nachrichtRepository.save(nachricht);
	}
	
	public void loescheNachricht(Nachricht nachricht) {
		nachrichtRepository.delete(nachricht);
	}
}
