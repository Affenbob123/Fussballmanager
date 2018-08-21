package fussballmanager.service.benachrichtigung;

public enum BenachrichtigungsTypen {

	STADION("Stadionausbau", ""),
	FREUNDSCHAFTSSPIELALLEGEGENALLE("Freundschaftsspielanfrage Alle-Alle", "Freundschaftsspiele Alle-Teams gegen Alle-Teams"),
	FREUNDSCHAFTSSPIELEINGEGENALLE("Freundschaftsspielanfrage Ein Team-Alle", "Freundschaftsspiele Ein-Team gegen Alle-Teams"),
	FREUNDSCHAFTSSPIELALLEGEGENEIN("Freundschaftsspielanfrage Alle-Ein Team", "Freundschaftsspiele Alle-Teams gegen Eins-Team"),
	FREUNDSCHAFTSSPIELEINGEGENEIN("Freundschaftsspielanfrage Ein Team-Ein Team", "Freundschaftsspiele Eins-Team gegen Eins-Team"),
	TRANSFERMARKT("Transfermarkt", ""),
	TURNIER("Turnier", ""),
	POKAL("Pokal", ""),
	PROTAGE("Protage", ""),
	SPIELERANGEBOT("Spielerangebot", ""),
	AUKTIONSHAUS("Auktionshaus", ""),
	LIGAAENDERUNG("Liga", ""),
	SPIELERBENACHRICHTIGUNG("Spielerereignis", ""),
	TEAMBENACHRICHTIGUNG("Teamereignis", "");
	
	public final String bezeichnung;
	private final String text;
	
	BenachrichtigungsTypen(String bezeichnung, String text) {
		this.bezeichnung = bezeichnung;
		this.text = text;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public String getText() {
		return text;
	}
}
