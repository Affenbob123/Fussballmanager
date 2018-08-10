package fussballmanager.service.liga;

public enum LigenNamenTypen {

	ERSTELIGA("1", 1),
	ZWEITELIGA_A("2A", 2),
	ZWEITELIGA_B("2B", 3),
	DRITTELIGA_A("3A", 4),
	DRITTELIGA_B("3B", 5),
	DRITTELIGA_C("3C", 6),
	DRITTELIGA_D("3D", 7);
//	FUENFTELIGA_A("4A"),
//	FUENFTELIGA_B("4B"),
//	FUENFTELIGA_C("4C"),
//	FUENFTELIGA_D("4D"),
//	FUENFTELIGA_E("4E"),
//	FUENFTELIGA_F("4F"),
//	FUENFTELIGA_G("4G"),
//	FUENFTELIGA_H("4H"),
//	SECHSTELIGA_A("5A"),
//	SECHSTELIGA_B("5B"),
//	SECHSTELIGA_C("5C"),
//	SECHSTELIGA_D("5D"),
//	SECHSTELIGA_E("5E"),
//	SECHSTELIGA_F("5F"),
//	SECHSTELIGA_G("5G"),
//	SECHSTELIGA_H("5H"),
//	SECHSTELIGA_I("5I"),
//	SECHSTELIGA_J("5J"),
//	SECHSTELIGA_K("5K"),
//	SECHSTELIGA_L("5L"),
//	SECHSTELIGA_M("5M"),
//	SECHSTELIGA_N("5N"),
//	SECHSTELIGA_O("5O"),
//	SECHSTELIGA_P("5P"),
//	SECHSTELIGA_Q("5Q"),
//	SECHSTELIGA_R("5R");
    
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
