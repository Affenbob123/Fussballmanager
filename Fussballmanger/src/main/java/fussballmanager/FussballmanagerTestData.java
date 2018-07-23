package fussballmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import fussballmanager.service.land.LandService;
import fussballmanager.service.liga.LigaService;
import fussballmanager.service.spiel.SpielService;
import fussballmanager.service.spieler.SpielerService;
import fussballmanager.service.team.TeamService;
import fussballmanager.service.torversuch.TorVersuchService;
import fussballmanager.service.user.User;
import fussballmanager.service.user.UserService;


@Component
@Profile("test")
public class FussballmanagerTestData {
	
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
	SpielService spielService;
	
	@Autowired
	TorVersuchService torService;
		
	LocalDateTime spielbeginn = LocalDateTime.of(LocalDate.of(2018, 4 , 16), LocalTime.of(18, 30));
	LocalDateTime spielbeginn1 = LocalDateTime.of(LocalDate.of(2018, 4 , 18), LocalTime.of(18, 30));
	LocalDateTime spielbeginn2 = LocalDateTime.of(LocalDate.of(2018, 4 , 20), LocalTime.of(18, 30));
	LocalDateTime spielbeginn3 = LocalDateTime.of(LocalDate.of(2018, 5 , 01), LocalTime.of(13, 07));
	LocalDateTime spielbeginn4 = LocalDateTime.of(LocalDate.of(2019, 4 , 28), LocalTime.of(14, 47));
	
	String LoginA = "a";

	
	String defaultProfilBild ="default.png";
	
	Random r = new Random();

	@PostConstruct
	public void erzeugeTestDaten() {
		//erzeugeTestUser();
	}
	
	private void erzeugeTestUser() {
		User userA = new User(LoginA, LoginA, false, LoginA, LoginA);
		userService.legeUserAn(userA);
	}
}
