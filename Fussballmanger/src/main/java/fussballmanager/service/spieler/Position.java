package fussballmanager.service.spieler;

public enum Position {

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
    
    Position(String position){
    	this.position = position;
    }
    
    public String getPosition() {
    	return this.position;
    }
}
