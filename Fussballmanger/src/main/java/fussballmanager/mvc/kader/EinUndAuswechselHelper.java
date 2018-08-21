package fussballmanager.mvc.kader;

import fussballmanager.service.spieler.Spieler;

public class EinUndAuswechselHelper {

	Spieler einzuwechselnderSpieler;
	
	String aufstellungsPositionsTyp;

	public Spieler getEinzuwechselnderSpieler() {
		return einzuwechselnderSpieler;
	}

	public void setEinzuwechselnderSpieler(Spieler einzuwechselnderSpieler) {
		this.einzuwechselnderSpieler = einzuwechselnderSpieler;
	}

	public String getAufstellungsPositionsTyp() {
		return aufstellungsPositionsTyp;
	}

	public void setAufstellungsPositionsTyp(String aufstellungsPositionsTyp) {
		this.aufstellungsPositionsTyp = aufstellungsPositionsTyp;
	}
	
	
}
