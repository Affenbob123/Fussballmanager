package fussballmanager.service.finanzen;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.saison.spieltag.SpieltagService;
import fussballmanager.service.spieler.Spieler;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.spieler.spielerzuwachs.SpielerZuwachsService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class BilanzService {

	private static final Logger LOG = LoggerFactory.getLogger(SpielerZuwachsService.class);

	@Autowired
	BilanzRepository bilanzRepository;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	public Bilanz findeBilanz(Long id) {
		return bilanzRepository.getOne(id);
	}
	
	public List<Bilanz> findeAlleBilanzen() {
		return bilanzRepository.findAll();
	}
	
	public void legeBilanzAn(Bilanz bilanz) {
		bilanzRepository.save(bilanz);
	}
	
	public void aktualisiereBilanz(Bilanz bilanz) {
		bilanzRepository.save(bilanz);
	}
	
	public void loescheBilanz(Bilanz bilanz) {
		bilanzRepository.delete(bilanz);
	}
	
	public void erfasseEinUndAusgabenEinesTeamsAmSpieltagEnde(Team team, List<Spieler> alleSpielerEinesTeams) {
		Bilanz bilanzDesTeams = team.getBilanz();
		
		//TODO einnahmen, trainingsausgaben
		bilanzDesTeams.setStadionEinnahmen(bilanzDesTeams.getStadionEinnahmen() + 1000000);
		bilanzDesTeams.setSponsorenEinnahmen(0);
		bilanzDesTeams.setPraemienEinnahmen(0);
		bilanzDesTeams.setGehaelterAusgaben(bilanzDesTeams.getGehaelterAusgaben() + teamService.berechneGehaelterEinesTeams(team, alleSpielerEinesTeams));
		bilanzDesTeams.setTrainingsAusgaben(0);
		bilanzDesTeams.setSonstigeAusgaben(0);

		aktualisiereBilanz(bilanzDesTeams);
	}
	
	public void erfasseTransfermarktEinkauf(Team team, Spieler spieler) {
		Bilanz bilanzDesTeams = team.getBilanz();
		
		bilanzDesTeams.setSpielerEinkaufAusgaben(spieler.getPreis());
		aktualisiereBilanz(bilanzDesTeams);
	}
	
	public void erfasseTransfermarktVerkauf(Team team, Spieler spieler) {
		Bilanz bilanzDesTeams = team.getBilanz();
		
		bilanzDesTeams.setSpielerVerkaufEinnahmen(spieler.getPreis());
		aktualisiereBilanz(bilanzDesTeams);
	}
}
