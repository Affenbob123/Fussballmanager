package fussballmanager.service.liga;

public enum LigenNamenTypen {

	ERSTELIGA("1"),
	ZWEITELIGA("2"),
	DRITTELIGA_A("3A"),
	DRITTELIGA_B("3B"),
	VIERTELIGA_A("4A"),
	VIERTELIGA_B("4B"),
	VIERTELIGA_C("4C"),
	VIERTELIGA_D("4D"),
	FUENFTELIGA_A("5A"),
	FUENFTELIGA_B("5B"),
	FUENFTELIGA_C("5C"),
	FUENFTELIGA_D("5D"),
	FUENFTELIGA_E("5E"),
	FUENFTELIGA_F("5F"),
	FUENFTELIGA_G("5G"),
	FUENFTELIGA_H("5H");
//	SECHSTELIGA_A("6A"),
//	SECHSTELIGA_B("6B"),
//	SECHSTELIGA_C("6C"),
//	SECHSTELIGA_D("6D"),
//	SECHSTELIGA_E("6E"),
//	SECHSTELIGA_F("6F"),
//	SECHSTELIGA_G("6G"),
//	SECHSTELIGA_H("6H"),
//	SECHSTELIGA_I("6I"),
//	SECHSTELIGA_J("6J"),
//	SECHSTELIGA_K("6K"),
//	SECHSTELIGA_L("6L"),
//	SECHSTELIGA_M("6M"),
//	SECHSTELIGA_N("6N"),
//	SECHSTELIGA_O("6O"),
//	SECHSTELIGA_P("6P"),
//	SECHSTELIGA_Q("6Q"),
//	SECHSTELIGA_R("6R");
    
    private final String name;
    
    LigenNamenTypen(String name){
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
}
