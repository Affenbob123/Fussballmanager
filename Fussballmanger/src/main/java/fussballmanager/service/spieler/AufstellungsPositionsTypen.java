package fussballmanager.service.spieler;

public enum AufstellungsPositionsTypen {
	
	TW("Torwart", 1, RollenTypen.TORWART, 0, 1, 2),
	LV("Linker Verteidiger", 2, RollenTypen.VERTEIDIGER, 2, 3, 2),
	LIV("Linker Innenverteidiger", 3, RollenTypen.VERTEIDIGER, 1, 3, 3),
	LIB("Libero", 4, RollenTypen.VERTEIDIGER, 1, 3, 3),
	RIV("Rechter Innenverteidiger", 5, RollenTypen.VERTEIDIGER, 1, 3, 3),
	RV("Rechter Verteidiger", 6, RollenTypen.VERTEIDIGER, 2, 3, 2),
	LM("Linkes Mittelfeld", 7, RollenTypen.MITTELFELD, 4, 2, 2),
	DM("Defensives Mittelfeld", 8, RollenTypen.MITTELFELD, 3, 3, 3),
	RM("Rechtes Mittelfeld", 9, RollenTypen.MITTELFELD, 4, 2, 2),
	ZM("Zentrales Mittelfeld", 10, RollenTypen.MITTELFELD, 4, 2, 1),
	OM("Offensives Mittelfeld", 11, RollenTypen.MITTELFELD, 5, 2, 1),
	LS("Linkes Stürmer", 12, RollenTypen.ANGREIFER, 6, 2, 1),
	MS("Mittelstürmer", 13, RollenTypen.ANGREIFER, 6, 2, 1),
	RS("Rechtes Stürmer", 14, RollenTypen.ANGREIFER, 6, 2, 1),
	ERSATZ("Ersatzbank", 15, null, 0, 0, 0), 
	VERLETZT("Verletzt", 16, null, 0, 0, 0),
	GESPERRT("Gesperrt", 17, null, 0, 0, 0),
	TRAININGSLAGER("Trainingslager", 18, null, 0, 0, 0);
	
    private final String positionsName;
    private final int rangfolge;
    private final RollenTypen rollenTyp;
	private final int torversuchWahrscheinlichkeit;
	private final int gelbeKarteWahrscheinlichkeit;
	private final int roteKarteWahrscheinlichkeit;
    
    AufstellungsPositionsTypen(String positionsName, int rangfolge, RollenTypen rollenTyp, int torversuchWahrscheinlichkeit, 
    		int gelbeKarteWahrscheinlichkeit, int roteKarteWahrscheinlichkeit) {
    	this.positionsName = positionsName;
    	this.rangfolge = rangfolge;
    	this.rollenTyp = rollenTyp;
		this.torversuchWahrscheinlichkeit = torversuchWahrscheinlichkeit;
		this.gelbeKarteWahrscheinlichkeit = gelbeKarteWahrscheinlichkeit;
		this.roteKarteWahrscheinlichkeit = roteKarteWahrscheinlichkeit;
    }
    
    public String getPositionsName() {
    	return this.positionsName;
    }
    
    public int getRangfolge() {
    	return this.rangfolge;
    }

	public RollenTypen getRollenTyp() {
		return rollenTyp;
	}
	
	public int getTorversuchWahrscheinlichkeit() {
		return torversuchWahrscheinlichkeit;
	}

	public int getGelbeKarteWahrscheinlichkeit() {
		return gelbeKarteWahrscheinlichkeit;
	}

	public int getRoteKarteWahrscheinlichkeit() {
		return roteKarteWahrscheinlichkeit;
	}
}
