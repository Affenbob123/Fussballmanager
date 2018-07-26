package fussballmanager.service.spieler;

public enum RollenTypen {
	TORWART(1.0, 0.5, 0.5, 0.5),
	VERTEIDIGER(0.5, 0.9, 0.8, 0.7),
	MITTELFELD(0.5, 0.8, 0.9, 0.8),
	ANGREIFER(0.5, 0.7, 0.8, 0.9);
	
	private final double staerkeFaktorAlsTorwart;
	private final double staerkeFaktorAlsVerteidiger;
	private final double staerkeFaktorAlsMittelfeld;
	private final double staerkeFaktorAlsAngreifer;
	
	RollenTypen(double staerkeFaktorAlsTorwart, double staerkeFaktorAlsVerteidiger, 
			double staerkeFaktorAlsMittelfeld, double staerkeFaktorAlsAngreifer) {
		this.staerkeFaktorAlsTorwart = staerkeFaktorAlsTorwart;
		this.staerkeFaktorAlsVerteidiger = staerkeFaktorAlsVerteidiger;
		this.staerkeFaktorAlsMittelfeld = staerkeFaktorAlsMittelfeld;
		this.staerkeFaktorAlsAngreifer = staerkeFaktorAlsAngreifer;
	}

	public double getStaerkeFaktorAlsTorwart() {
		return staerkeFaktorAlsTorwart;
	}

	public double getStaerkeFaktorAlsVerteidiger() {
		return staerkeFaktorAlsVerteidiger;
	}

	public double getStaerkeFaktorAlsMittelfeld() {
		return staerkeFaktorAlsMittelfeld;
	}

	public double getStaerkeFaktorAlsAngreifer() {
		return staerkeFaktorAlsAngreifer;
	}
}
