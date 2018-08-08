package fussballmanager.service.spieler.spielerzuwachs;

public enum Trainingslager {

	KEIN_TRAININGSLAGER(1.0),
	KLINGO(3.0),
	JUGENDINTERNAT(4.0);
	
	public final double internatFaktor;
	
	Trainingslager(double internatFaktor) {
		this.internatFaktor = internatFaktor;
	}

	public double getInternatFaktor() {
		return internatFaktor;
	}
}
