package fussballmanager.service.spieler;

public enum PositionenTypen {

	TW("Torwart"),
	LV("Linker Verteidiger"),
	LIV("Linker Innenverteidiger"),
	LIB("Libero"),
	RIV("Rechter Innenverteidiger"),
	RV("Rechter Verteidiger"),
	LM("Linkes Mittelfeld"),
	DM("Defensives Mittelfeld"),
	RM("Rechtes Mittelfeld"),
	ZM("Zentrales Mittelfeld"),
	OM("Offensives Mittelfeld"),
	LS("Linkes Mittelfeld"),
	MS("Mittelstürmer"),
	RS("Rechtes Mittelfeld");
    
    private final String position;
    
    PositionenTypen(String position){
    	this.position = position;
    }
    
    public String getPosition() {
    	return this.position;
    }
}
