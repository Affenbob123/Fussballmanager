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


@Component
@Profile("test")
public class FussballmanagerTestData {
		
	LocalDateTime spielbeginn = LocalDateTime.of(LocalDate.of(2018, 4 , 16), LocalTime.of(18, 30));
	LocalDateTime spielbeginn1 = LocalDateTime.of(LocalDate.of(2018, 4 , 18), LocalTime.of(18, 30));
	LocalDateTime spielbeginn2 = LocalDateTime.of(LocalDate.of(2018, 4 , 20), LocalTime.of(18, 30));
	LocalDateTime spielbeginn3 = LocalDateTime.of(LocalDate.of(2018, 5 , 01), LocalTime.of(13, 07));
	LocalDateTime spielbeginn4 = LocalDateTime.of(LocalDate.of(2019, 4 , 28), LocalTime.of(14, 47));
	
	String dummyEins = "dummy1";
	String dummyZwei = "dummy2";
	String dummyDrei = "dummy3";
	String dummyVier = "dummy4";
	String dummyFuenf = "dummy5";
	String dummySechs = "dummy6";
	String dummySieben = "dummy7";
	String dummyAcht = "dummy8";
	
	String defaultProfilBild ="default.png";
	
	Random r = new Random();

	@PostConstruct
	public void erzeugeTestDaten() {

	}

	private void erzeugeTestTipps() {
		
	}
	
	private void erzeugeTestSpieler() {
		
	}
}
