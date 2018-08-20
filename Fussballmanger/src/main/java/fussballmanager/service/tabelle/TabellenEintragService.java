package fussballmanager.service.tabelle;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.liga.Liga;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.saison.Saison;
import fussballmanager.service.saison.SaisonService;
import fussballmanager.service.team.Team;
import fussballmanager.service.team.TeamService;

@Service
@Transactional
public class TabellenEintragService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TabellenEintragService.class);

	@Autowired
	TabellenEintragRepository tabellenEintragRepository;
	
	@Autowired
	SaisonService saisonService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	LigaService ligaService;
	
	public TabellenEintrag findeTabellenEintrag(Long id) {
		return tabellenEintragRepository.getOne(id);
	}
	
	public TabellenEintrag findeTabellenEintragDurchTeamUndSaison(Team team, Saison saison) {
		return tabellenEintragRepository.findByTeamAndSaison(team, saison);
	}
	
	public List<TabellenEintrag> findeTabellenEintraege() {
		return tabellenEintragRepository.findAll();
	}
	
	public List<TabellenEintrag> findeAlleTabellenEintraegeEinerLiga(Liga liga) {
		return tabellenEintragRepository.findByLiga(liga);
	}
	
	public void legeTabellenEintragAn(TabellenEintrag tabellenEintrag) {
		tabellenEintragRepository.save(tabellenEintrag);
	}
	
	public void aktualisiereTabellenEintrag(TabellenEintrag tabellenEintrag) {
		tabellenEintragRepository.save(tabellenEintrag);
	}
	
	public void loescheTabellenEintrag(TabellenEintrag tabellenEintrag) {
		tabellenEintragRepository.delete(tabellenEintrag);
	}
	
	public void einenTabellenEintragAktualisieren(TabellenEintrag tabellenEintrag) {
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		
		tabellenEintrag.setSiege(teamService.siegeEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setUnentschieden(teamService.unentschiedenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setNiederlagen(teamService.niederlagenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setSpiele(tabellenEintrag.getSiege() + tabellenEintrag.getUnentschieden() + tabellenEintrag.getNiederlagen());
		tabellenEintrag.setTore(teamService.toreEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setGegentore(teamService.gegenToreEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setTorDifferenz(tabellenEintrag.getTore() - tabellenEintrag.getGegentore());
		tabellenEintrag.setPunkte(teamService.punkteEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setGelbeKarten(teamService.gelbeKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setGelbRoteKarten(teamService.gelbeRoteKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		tabellenEintrag.setRoteKarten(teamService.roteKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
		
		aktualisiereTabellenEintrag(tabellenEintrag);
		setPlatzierung(tabellenEintrag.getLiga());
	}
	
	public void alleTabellenEintraegeAktualisieren() {
		List<Liga> alleLigen = ligaService.findeAlleLigen();
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		for(Liga liga : alleLigen) {
			List<TabellenEintrag> alleTabellenEintraegeEinerLigaUndSaison = findeAlleTabellenEintraegeEinerLigaInEinerSaison(liga, aktuelleSaison);
			
			for(TabellenEintrag tabellenEintrag : alleTabellenEintraegeEinerLigaUndSaison) {
				tabellenEintrag.setSiege(teamService.siegeEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setUnentschieden(teamService.unentschiedenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setNiederlagen(teamService.niederlagenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setSpiele(tabellenEintrag.getSiege() + tabellenEintrag.getUnentschieden() + tabellenEintrag.getNiederlagen());
				tabellenEintrag.setTore(teamService.toreEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setGegentore(teamService.gegenToreEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setTorDifferenz(tabellenEintrag.getTore() - tabellenEintrag.getGegentore());
				tabellenEintrag.setPunkte(teamService.punkteEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setGelbeKarten(teamService.gelbeKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setGelbRoteKarten(teamService.gelbeRoteKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				tabellenEintrag.setRoteKarten(teamService.roteKartenEinesTeamsInEinerSaison(tabellenEintrag.getTeam(), aktuelleSaison));
				
				aktualisiereTabellenEintrag(tabellenEintrag);
			}
			setPlatzierung(liga);
		}
	}

	public void erstelleTabellenEintragFuerJedesTeam() {
		Saison aktuelleSaison = saisonService.findeAktuelleSaison();
		List<Liga> alleLigen = ligaService.findeAlleLigen();
		
		for(Liga liga : alleLigen) {
			List<Team> alleTeamsEinerLiga = teamService.findeAlleTeamsEinerLiga(liga);
			int platzierung = 1;
			for(Team team : alleTeamsEinerLiga) {
				TabellenEintrag tabellenEintrag = new TabellenEintrag(team.getLiga(), aktuelleSaison, team);
				tabellenEintrag.setPlatzierung(platzierung);
				legeTabellenEintragAn(tabellenEintrag);
				platzierung++;
			}
		}
	}
	
	public void setPlatzierung(Liga liga) {
		int platzierung = 0;
		List<TabellenEintrag> tabellenEintraege = findeAlleTabellenEintraegeEinerLigaInEinerSaison(liga, saisonService.findeAktuelleSaison());
		Collections.sort(tabellenEintraege);
				
		for(TabellenEintrag tabellenEintrag : tabellenEintraege) {
			platzierung++;
			tabellenEintrag.setPlatzierung(platzierung);
			aktualisiereTabellenEintrag(tabellenEintrag);
		}
	}

	public List<TabellenEintrag> findeAlleTabellenEintraegeEinerLigaInEinerSaison(Liga liga, Saison saison) {
		List<TabellenEintrag> tabellenEintraege = tabellenEintragRepository.findByLigaAndSaison(liga, saison);
		
		return tabellenEintraege;
	}
}
