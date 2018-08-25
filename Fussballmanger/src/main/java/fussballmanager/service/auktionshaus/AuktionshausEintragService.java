package fussballmanager.service.auktionshaus;

import java.time.LocalDateTime;
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
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;

@Service
@Transactional
public class AuktionshausEintragService {

	private static final Logger LOG = LoggerFactory.getLogger(AuktionshausEintragService.class);

	@Autowired
	AuktionshausEintragRepository auktionshausEintragRepository;
	
	@Autowired
	SpielerService spielerService;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	SpieltagService spieltagService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	UserService userService;
	
	public AuktionshausEintrag findeAuktionshausEintrag(Long id) {
		return auktionshausEintragRepository.getOne(id);
	}
	
	public List<AuktionshausEintrag> findeAlleAuktionshausEintraege() {
		return auktionshausEintragRepository.findAll();
	}
	
	public void legeAuktionshausEintragAn(AuktionshausEintrag auktionshausEintrag) {
		auktionshausEintragRepository.save(auktionshausEintrag);
	}
	
	public void aktualisiereAuktionshausEintrag(AuktionshausEintrag auktionshausEintrag) {
		auktionshausEintragRepository.save(auktionshausEintrag);
	}
	
	public void loescheAuktionshausEintrag(AuktionshausEintrag auktionshausEintrag) {
		auktionshausEintragRepository.delete(auktionshausEintrag);
	}

	public List<AuktionshausEintrag> findeAlleAktionshausEintraegeFuerProtage(int fuerProtage) {
		if(fuerProtage == 1) {
			return auktionshausEintragRepository.findByFuerProtage(true);
		}
		if(fuerProtage == -1) {
			return auktionshausEintragRepository.findByFuerProtage(false);
		}
		return findeAlleAuktionshausEintraege();
	}

	public void ueberpruefeAlleAuktionshausEintraege() {
		List<AuktionshausEintrag> alleAuktionshausEintraege = findeAlleAuktionshausEintraege();
		LocalDateTime aktuelleZeit = LocalDateTime.now(ZoneId.of("Europe/Berlin"));
		for(AuktionshausEintrag auktionshausEintrag : alleAuktionshausEintraege) {
			if(aktuelleZeit.isAfter(auktionshausEintrag.getAblaufDatum())) {
				auktionshausEintragBeenden(auktionshausEintrag);
			}
		}
	}

	public void auktionshausEintragBeenden(AuktionshausEintrag auktionshausEintrag) {
		User verkaeufer = auktionshausEintrag.getTeam().getUser();
		Team verkauftesTeam = auktionshausEintrag.getTeam();
		User kaeufer = auktionshausEintrag.getHoechstBieter();
		
		if(auktionshausEintrag.isFuerProtage()) {
			if(auktionshausEintrag.getTeam().equals(verkaeufer.getAktuellesTeam())) {
				List<Team> teamsDesVerkaeufers = teamService.findeAlleTeamsEinesUsers(verkaeufer);
				teamsDesVerkaeufers.remove(verkaeufer.getAktuellesTeam());
				Team neuesAktuellesTeamDesVerkaeufers = teamsDesVerkaeufers.get(0);
				verkaeufer.setAktuellesTeam(neuesAktuellesTeamDesVerkaeufers);
				userService.aktualisiereUser(verkaeufer);
			}
			verkaeufer.setProtage(verkaeufer.getProtage() + (int) berechneVerkaufspreisNachGebuehren(auktionshausEintrag.getAktuellesGebot()));
			userService.aktualisiereUser(verkaeufer);
			verkauftesTeam.setUser(kaeufer);
			teamService.aktualisiereTeam(verkauftesTeam);
		} else {
			if(auktionshausEintrag.getTeam().equals(verkaeufer.getAktuellesTeam())) {
				List<Team> teamsDesVerkaeufers = teamService.findeAlleTeamsEinesUsers(verkaeufer);
				teamsDesVerkaeufers.remove(verkaeufer.getAktuellesTeam());
				Team neuesAktuellesTeamDesVerkaeufers = teamsDesVerkaeufers.get(0);
				verkaeufer.setAktuellesTeam(neuesAktuellesTeamDesVerkaeufers);
				userService.aktualisiereUser(verkaeufer);
			}
			verkaeufer.getAktuellesTeam().getBilanz().setSonstigeEinnahmen(verkaeufer.getAktuellesTeam().getBilanz().getSonstigeEinnahmen() 
					+ berechneVerkaufspreisNachGebuehren(auktionshausEintrag.getAktuellesGebot()));
			teamService.aktualisiereTeam(verkaeufer.getAktuellesTeam());
			verkauftesTeam.setUser(kaeufer);
			teamService.aktualisiereTeam(verkauftesTeam);
		}
		loescheAuktionshausEintrag(auktionshausEintrag);
	}
	
	private long berechneVerkaufspreisNachGebuehren(long endGebot) {
		long verkaufspreisNachGebuehren = (long) (endGebot * 0.9);
		
		return verkaufspreisNachGebuehren;
	}
}
