package fussballmanager.service.personal;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fussballmanager.service.land.Land;
import fussballmanager.service.team.Team;


@Service
@Transactional
public class PersonalService {
	
	private final int  minTalentwert = 0;
	private final int maxTalentwert = 200;
	private final int maxPersonalAnzahl = 5;

	@Autowired
	PersonalRepository personalRepository;
	
	public Personal findePersonal(Long id) {
		return personalRepository.getOne(id);
	}
	
	public List<Personal> findeAllePersonaler() {
		return personalRepository.findAll();
	}
	
	public List<Personal> findeAllePersonalerEinesTeamsNachPersonalTyp(Team team, PersonalTypen personalTyp) {
		return personalRepository.findByTeamAndPersonalTyp(team, personalTyp);
	}
	
	public void legePersonalAn(Personal personal) {
		personalRepository.save(personal);
	}
	
	public void aktualisierePersonal(Personal personal) {
		personalRepository.save(personal);
	}
	
	public void loeschePersonal(Personal personal) {
		personalRepository.delete(personal);
	}
	
	public void erstelleStandardTrainer(Team team) {
		Land nationalitaet = team.getLand();
		int alter = 30;
		int talentwert = erzeugeZufaelligenTalentwert();
		int erfahrung = 0;
		PersonalTypen personalTyp = PersonalTypen.TRAINER;
		double staerke = 200.0;
		long preis = (long) (staerke * 1000);
		Personal personal = new Personal(team, nationalitaet, alter, talentwert, erfahrung, personalTyp, staerke, preis);
		Personal personal2 = new Personal(team, nationalitaet, alter, talentwert, erfahrung, personalTyp, 100, preis);
		
		legePersonalAn(personal);
		legePersonalAn(personal2);
	}
	
	private int erzeugeZufaelligenTalentwert() {
		Random r = new Random();
		return r.nextInt((maxTalentwert - minTalentwert) + 1) + minTalentwert;
	}

	public double ermittleStaerkeNachPersonalTyp(Team team, PersonalTypen personalTyp) {
		List<Personal> trainer = findeAllePersonalerEinesTeamsNachPersonalTyp(team, personalTyp);
		double gesamtStaerke = 0.0;
		
		Collections.sort(trainer);
		for(int i = 1; i <= trainer.size(); i++) {
			double trainerStaerke = trainer.get(i-1).getStaerke() / i;
			gesamtStaerke = gesamtStaerke + trainerStaerke;
		}
		return gesamtStaerke;
	}
}
