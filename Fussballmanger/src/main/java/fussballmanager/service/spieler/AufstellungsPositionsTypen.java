package fussballmanager.service.spieler;

public enum AufstellungsPositionsTypen {

	TW("Torwart", 1),
	LV("Linker Verteidiger", 2),
	LIV("Linker Innenverteidiger", 3),
	LIB("Libero", 4),
	RIV("Rechter Innenverteidiger", 5),
	RV("Rechter Verteidiger", 6),
	LM("Linkes Mittelfeld", 7),
	DM("Defensives Mittelfeld", 8),
	RM("Rechtes Mittelfeld", 9),
	ZM("Zentrales Mittelfeld", 10),
	OM("Offensives Mittelfeld", 11),
	LS("Linkes Stürmer", 12),
	MS("Mittelstürmer", 13),
	RS("Rechtes Stürmer", 14),
	ERSATZ("Ersatzbank", 15),
	TRANSFERMARKT("Transfermarkt", 16);
	
    
    private final String positionsName;
    private final int rangfolge;
    
    AufstellungsPositionsTypen(String positionsName, int rangfolge){
    	this.positionsName = positionsName;
    	this.rangfolge = rangfolge;
    }
    
    public String getPositionsName() {
    	return this.positionsName;
    }
    
    public int getRangfolge() {
    	return this.rangfolge;
    }
}
