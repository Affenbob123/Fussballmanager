package fussballmanager.service.land;

public enum LaenderNamenTypen {
	
	DEUTSCHLAND("Deutschland", "/laenderflaggen/deutschland.svg.png"),
	ENGLAND("England", "/laenderflaggen/england.svg.png"),
	SPANIEN("Spanien", "/laenderflaggen/spanien.svg.png"),
	ITALIEN("Italien","/laenderflaggen/italien.svg.png"),
	FRANKREICH("Frankreich", "/laenderflaggen/frankreich.svg.png");
    
    private final String name;
    private final String bild;
    
    LaenderNamenTypen(String name, String bild){
    	this.name = name;
    	this.bild = bild;
    }
    
    public String getName() {
    	return this.name;
    }

	public String getBild() {
		return bild;
	}
}
