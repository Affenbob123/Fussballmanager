package fussballmanager.mvc.kader;

import fussballmanager.service.spieler.Spieler;

public class EinUndAuswechselHelper {

	Spieler einzuwechselnderSpieler;
	
	Spieler auszuwechselnderSpieler;

	public Spieler getEinzuwechselnderSpieler() {
		return einzuwechselnderSpieler;
	}

	public void setEinzuwechselnderSpieler(Spieler einzuwechselnderSpieler) {
		this.einzuwechselnderSpieler = einzuwechselnderSpieler;
	}

	public Spieler getAuszuwechselnderSpieler() {
		return auszuwechselnderSpieler;
	}

	public void setAuszuwechselnderSpieler(Spieler auszuwechselnderSpieler) {
		this.auszuwechselnderSpieler = auszuwechselnderSpieler;
	}
}
