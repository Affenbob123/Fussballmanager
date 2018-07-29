package fussballmanager.service.spiel.spielereignisse;

public enum SpielEreignisTypen {
	TORVERSUCHGEHALTEN("Torversuch wurde gehalten!"),
	TORVERSUCH("Torversuch!"),
	TORVERSUCHGETROFFEN("Tor wurde geschossen!"),
	GELBEKARTE("gelbe Karte"),
	GELBROTEKARTE("gelbrote Karte"),
	ROTEKARTE("rote Karte"),
	VERLETZUNG("Verletzung"),
	NIX("Nix!");
	
	final String beschreibung;
	
	SpielEreignisTypen (String beschreibung){
		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {
		return beschreibung;
	}
}
