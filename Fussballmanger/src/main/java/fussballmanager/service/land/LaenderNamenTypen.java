package fussballmanager.service.land;

public enum LaenderNamenTypen {
	
	DEUTSCHLAND("Deutschland");
//	ENGLAND("England"),
//	SPANIEN("Spanien"),
//	ITALIEN("Italien"),
//	FRANKREICH("Frankreich");
    
    private final String name;
    
    LaenderNamenTypen(String name){
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
}
