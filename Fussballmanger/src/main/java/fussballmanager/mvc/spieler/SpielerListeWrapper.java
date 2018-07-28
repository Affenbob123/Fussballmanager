package fussballmanager.mvc.spieler;

import java.util.List;

import fussballmanager.service.spieler.Spieler;

public class SpielerListeWrapper {

	private List<Spieler> spielerListe;
	   
	public List<Spieler> getSpielerListe() {
		return spielerListe;
	}

	public void setSpielerListe(List<Spieler> spielerListe) {
		this.spielerListe = spielerListe;
	}
}
