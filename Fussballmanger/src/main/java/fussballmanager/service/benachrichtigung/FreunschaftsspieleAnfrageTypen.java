package fussballmanager.service.benachrichtigung;

public enum FreunschaftsspieleAnfrageTypen {

	ALLETEAMSALLETEAMS("Alle Teams von dir gegen alle Teams von Ihm", "mit allen Teams gegen all deine Teams Freundschaftsspiele"),
	EINTEAMALLETEAMS("Dein Team gegen alle Teams von Ihm", "mit seinem Team gegen all deine Teams Freundschaftsspiele"),
	ALLETEAMSEINTEAM("Alle Teams von dir gegen ein Team von Ihm", "mit allen seinen Teams gegen dein Team Freundschaftsspiele"),
	EINTEAMEINTEAM("Dein Team gegen sein Team", "gegen dein Team ein Freundschaftsspiel");
	
	private final String bezeichnung;
	private final String benachrichtungsText;
	
	FreunschaftsspieleAnfrageTypen(String bezeichnung, String benachrichtungsText) {
		this.bezeichnung = bezeichnung;
		this.benachrichtungsText = benachrichtungsText;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public String getBenachrichtungsText() {
		return benachrichtungsText;
	}
}
