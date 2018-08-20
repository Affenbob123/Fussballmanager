package fussballmanager.service.benachrichtigung;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
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
	
	public Benachrichtigung findeBenachrichtigung(Long id) {
		return benachrichtigungRepository.getOne(id);
	}
	
	public List<Benachrichtigung> findeBenachrichtigungen() {
		return benachrichtigungRepository.findAll();
	}
	
	public List<Benachrichtigung> findeAlleBenachrichtigungenEinesUsers(User user) {
		List<Team> teamsDesEmpfaengers = teamService.findeAlleTeamsEinesUsers(user);
		return benachrichtigungRepository.findByGelesenAndEmpfaengerIn(false, teamsDesEmpfaengers);
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

	public void erstelleFreundschaftsspielAnfrage(Team absender, List<Team> empfaenger,
			FreunschaftsspieleAnfrageTypen freundschaftsspielAnfrageTyp) {
		spieltagService.findeAktuellenSpieltag();
		LocalTime aktuelleUhrzeit = LocalTime.now(ZoneId.of("Europe/Berlin"));
		
		for(Team team : empfaenger) {
			Benachrichtigung benachrichtigung = new Benachrichtigung();
			benachrichtigung.setAbsender(absender);
			benachrichtigung.setEmpfaenger(team);
			benachrichtigung.setSpieltag(spieltagService.findeAktuellenSpieltag());
			benachrichtigung.setUhrzeit(aktuelleUhrzeit);
			benachrichtigung.setBenachrichtigungsText(freundschaftspielAnfrageText(absender, team, freundschaftsspielAnfrageTyp));
			legeBenachrichtigungAn(benachrichtigung);
		}
	}

	private String freundschaftspielAnfrageText(Team absender, Team team,
			FreunschaftsspieleAnfrageTypen freundschaftsspielAnfrageTyp) {
		String s; 
		s = "Das Team " + absender.getName() + " hat dir eine Freundschaftsspielanfrage gesendet. Er möchte " 
		+ freundschaftsspielAnfrageTyp.getBenachrichtungsText() + " vereinbaren.";
		return s;
	}
	
	
}
