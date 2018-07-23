package fussballmanager.service.spielereignisse;

public enum SpielEreignisTypen {
	TORVERSUCHGEHALTEN("Torversuch wurde gehalten!"),
	TORVERSUCH(""),
	TORVERSUCHGETROFFEN(""),
	GELBEKARTE(""),
	GELBROTEKARTE(""),
	ROTEKARTE(""),
	NIX("");
	
	final String beschreibung;
	
	SpielEreignisTypen (String beschreibung){
		this.beschreibung = beschreibung;
	}

	public String getBeschreibung() {
		return beschreibung;
	}
}
