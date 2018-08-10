package fussballmanager.service.liga;

public enum LigenNamenTypen {

	ERSTELIGA("1", 1),
	ZWEITELIGA_A("2A", 2),
	ZWEITELIGA_B("2B", 3),
	DRITTELIGA_A("3A", 4),
	DRITTELIGA_B("3B", 5),
	DRITTELIGA_C("3C", 6),
	DRITTELIGA_D("3D", 7),
	FUENFTELIGA_A("4A", 8),
	FUENFTELIGA_B("4B", 9),
	FUENFTELIGA_C("4C", 10),
	FUENFTELIGA_D("4D", 11),
	FUENFTELIGA_E("4E", 12),
	FUENFTELIGA_F("4F", 13),
	FUENFTELIGA_G("4G", 14),
	FUENFTELIGA_H("4H", 15),
	SECHSTELIGA_A("5A", 16),
	SECHSTELIGA_B("5B", 17),
	SECHSTELIGA_C("5C", 18),
	SECHSTELIGA_D("5D", 19),
	SECHSTELIGA_E("5E", 20),
	SECHSTELIGA_F("5F", 21),
	SECHSTELIGA_G("5G", 22),
	SECHSTELIGA_H("5H", 23),
	SECHSTELIGA_I("5I", 24),
	SECHSTELIGA_J("5J", 25),
	SECHSTELIGA_K("5K", 26),
	SECHSTELIGA_L("5L", 27),
	SECHSTELIGA_M("5M", 28),
	SECHSTELIGA_N("5N", 29),
	SECHSTELIGA_O("5O", 30),
	SECHSTELIGA_P("5P", 31);
    
    private final String name;
    private final int ligaRangfolge;
    
    LigenNamenTypen(String name, int ligaRangfolge){
    	this.name = name;
    	this.ligaRangfolge = ligaRangfolge;
    }
    
    public String getName() {
    	return this.name;
    }

	public int getLigaRangfolge() {
		return ligaRangfolge;
	}
}
