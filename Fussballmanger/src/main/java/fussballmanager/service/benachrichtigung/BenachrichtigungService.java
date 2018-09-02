package fussballmanager.service.benachrichtigung;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachs;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsRepository;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;

@Service
@Transactional
public class BenachrichtigungService {

	private static final Logger LOG = LoggerFactory.getLogger(BenachrichtigungService.class);

	@Autowired
	BenachrichtigungRepository benachrichtigungRepository;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	SpielService spielService;
	
	public Benachrichtigung findeBenachrichtigung(Long id) {
		return benachrichtigungRepository.getOne(id);
	}
	
	public List<Benachrichtigung> findeBenachrichtigungen() {
		return benachrichtigungRepository.findAll();
	}
	
	public List<Benachrichtigung> findeAlleBenachrichtigungenEinesUsers(User user) {
		List<Team> teamsDesEmpfaengers = teamService.findeAlleTeamsEinesUsers(user);
		return benachrichtigungRepository.findByEmpfaengerIn(teamsDesEmpfaengers);
	}
	
	public List<Benachrichtigung> findeAlleUngelesenenBenachrichtigungenEinesUsers(User user) {
		List<Team> teamsDesEmpfaengers = teamService.findeAlleTeamsEinesUsers(user);
		return benachrichtigungRepository.findByEmpfaengerInAndGelesen(teamsDesEmpfaengers, false);
	}
	
	public List<Benachrichtigung> findeBenachrichtigungenNachSeite(User user, int seite) {
		int seitenLaenge = 10;
		int ersteNachricht = (seite - 1) * seitenLaenge;
		int letzteNachricht = (seite - 1) * seitenLaenge + seitenLaenge;
		List<Benachrichtigung> alleBenachrichtigungenEinesUsers= findeAlleBenachrichtigungenEinesUsers(user);
		List<Benachrichtigung> result = new ArrayList<>();
		
		Collections.reverse(alleBenachrichtigungenEinesUsers);
		if(letzteNachricht > alleBenachrichtigungenEinesUsers.size()) {
			result = alleBenachrichtigungenEinesUsers.subList(ersteNachricht, alleBenachrichtigungenEinesUsers.size());
		} else {
			result = alleBenachrichtigungenEinesUsers.subList(ersteNachricht, letzteNachricht);
		}
		return result;
	}
	
	public void legeBenachrichtigungAn(Benachrichtigung benachrichtigung) {
		benachrichtigungRepository.save(benachrichtigung);
	}
	
	public void aktualisiereBenachrichtigung(Benachrichtigung benachrichtigung) {
		benachrichtigungRepository.save(benachrichtigung);
	}
	
	public void loescheBenachrichtigung(Benachrichtigung benachrichtigung) {
		benachrichtigungRepository.delete(benachrichtigung);
	}

	public void erstelleFreundschaftsspielAnfrage(Team absender, List<Team> empfaenger, BenachrichtigungsTypen benachrichtigungsTyp) {
		spieltagService.findeAktuellenSpieltag();
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		for(Team team : empfaenger) {
			Benachrichtigung benachrichtigung = new Benachrichtigung();
			benachrichtigung.setAbsender(absender);
			benachrichtigung.setEmpfaenger(team);
			benachrichtigung.setSpieltag(spieltagService.findeAktuellenSpieltag());
			benachrichtigung.setUhrzeit(aktuelleUhrzeit);
			benachrichtigung.setBenachrichtigungsText(freundschaftspielAnfrageText(absender, team, benachrichtigungsTyp));
			benachrichtigung.setBenachrichtungsTyp(benachrichtigungsTyp);
			benachrichtigung.setAntwortTyp(AntwortTypen.ANNEHMEN);
			legeBenachrichtigungAn(benachrichtigung);
		}
	}

	public void benachrichtigungAngenommen(Benachrichtigung benachrichtigung) {
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		spielService.erstelleFreundschaftsspiele(benachrichtigung.getBenachrichtungsTyp(), benachrichtigung.getAbsender(), benachrichtigung.getEmpfaenger());
		Benachrichtigung neueBenachrichtigung = new Benachrichtigung();
		neueBenachrichtigung.setAbsender(benachrichtigung.getEmpfaenger());
		neueBenachrichtigung.setEmpfaenger(benachrichtigung.getAbsender());
		neueBenachrichtigung.setBenachrichtungsTyp(benachrichtigung.getBenachrichtungsTyp());
		neueBenachrichtigung.setSpieltag(spieltagService.findeAktuellenSpieltag());
		neueBenachrichtigung.setUhrzeit(aktuelleUhrzeit);
		neueBenachrichtigung.setBenachrichtigungsText(freundschaftspielAnfrageAngenommenText(neueBenachrichtigung));
		LOG.info("Absender: {}, Empfänger: {}", benachrichtigung.getAbsender().getName(), benachrichtigung.getEmpfaenger().getName());
		LOG.info("Absender: {}, Empfänger: {}", neueBenachrichtigung.getAbsender().getName(), neueBenachrichtigung.getEmpfaenger().getName());
		legeBenachrichtigungAn(neueBenachrichtigung);
	}

	public void benachrichtigungAbgelehnt(Benachrichtigung benachrichtigung) {
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		Benachrichtigung neueBenachrichtigung = new Benachrichtigung();
		neueBenachrichtigung.setAbsender(benachrichtigung.getEmpfaenger());
		neueBenachrichtigung.setEmpfaenger(benachrichtigung.getAbsender());
		neueBenachrichtigung.setBenachrichtungsTyp(benachrichtigung.getBenachrichtungsTyp());
		neueBenachrichtigung.setSpieltag(spieltagService.findeAktuellenSpieltag());
		neueBenachrichtigung.setUhrzeit(aktuelleUhrzeit);
		neueBenachrichtigung.setBenachrichtigungsText(freundschaftspielAnfrageAbgelehntText(neueBenachrichtigung));		
		LOG.info("Absender: {}, Empfänger: {}", benachrichtigung.getAbsender().getName(), benachrichtigung.getEmpfaenger().getName());
		LOG.info("Absender: {}, Empfänger: {}", neueBenachrichtigung.getAbsender().getName(), neueBenachrichtigung.getEmpfaenger().getName());
		legeBenachrichtigungAn(neueBenachrichtigung);
	}
	
	private String freundschaftspielAnfrageText(Team absender, Team team, BenachrichtigungsTypen benachrichtigungsTyp) {
		String s; 
		s = "Das Team " + absender.getName() + " hat dir eine Freundschaftsspielanfrage gesendet. Es möchte " 
		+ benachrichtigungsTyp.getBezeichnung() + " vereinbaren.";
		return s;
	}
	
	private String freundschaftspielAnfrageAngenommenText(Benachrichtigung benachrichtigung) {
		String s; 
		s = "Das Team " + benachrichtigung.getAbsender().getName() + " hat deine Freundschaftsspielanfrage angenommen.";
		return s;
	}
	
	private String freundschaftspielAnfrageAbgelehntText(Benachrichtigung benachrichtigung) {
		String s; 
		s = "Das Team " + benachrichtigung.getAbsender().getName() + " hat deine Freundschaftsspielanfrage abgelehnt.";
		return s;
	}

	public BenachrichtigungsTypen ermittleBenachrichtigungsTypAusFreundschaftsspielTyp(
			FreunschaftsspieleAnfrageTypen freundschaftsspielAnfrageTyp) {
		if(freundschaftsspielAnfrageTyp.equals(FreunschaftsspieleAnfrageTypen.ALLETEAMSALLETEAMS)) {
			return BenachrichtigungsTypen.FREUNDSCHAFTSSPIELALLEGEGENALLE;
		}
		if(freundschaftsspielAnfrageTyp.equals(FreunschaftsspieleAnfrageTypen.ALLETEAMSEINTEAM)) {
			return BenachrichtigungsTypen.FREUNDSCHAFTSSPIELALLEGEGENEIN;			
					}
		if(freundschaftsspielAnfrageTyp.equals(FreunschaftsspieleAnfrageTypen.EINTEAMALLETEAMS)) {
			return BenachrichtigungsTypen.FREUNDSCHAFTSSPIELEINGEGENALLE;
		}
		return BenachrichtigungsTypen.FREUNDSCHAFTSSPIELEINGEGENEIN;
	}
}
