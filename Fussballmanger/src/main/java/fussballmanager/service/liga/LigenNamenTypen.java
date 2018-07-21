package fussballmanager.service.liga;

public enum LigenNamenTypen {

	ERSTELIGA("1.Liga"),
	ZWEITELIGA("2.Liga"),
	DRITTELIGA("3.Liga");
    
    private final String name;
    
    LigenNamenTypen(String name){
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
}
