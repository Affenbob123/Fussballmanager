package fussballmanager.service.spieler.spielerzuwachs;

public enum ZuwachsFaktorAlter {
	VIERZEHN(14, 30, 1.0),
	FUENFZEHN(15, 31, 1.5),
	SECHSZEHN(16, 32, 2.0),
	SIEBZEHN(17, 33, 2.5),
	ACHTZEHN(18, 34, 3.0),
	NEUNZEHN(19, 35, 3.5),
	ZWANZIG(20, 36, 4.0),
	EINUNDZWANZIG(21, 37, 3.5),
	ZWEIUNDZWANZIG(22, 38, 3.0),
	DREIUNDZWANZIG(23, 39, 2.5),
	VIERUNDZWANZIG(24, 40, 2.0),
	FUENFUNDZWANZIG(25, 41, 1.5),
	SECHSUNDZWANZIG(26, 42, 1.0),
	SIEBENUNDZWANZIG(27, 43, 0.5),
	ACHTUNDZWANZIG(28, 44, 0.0),
	NEUNUNDZWANZIG(29, 45, -0.5),
	DREISSIG(30, 46, -1.0),
	EINUNDDREISSIG(31, 47, -1.5),
	ZWEIUNDDREISSIG(32, 48, -2.0),
	DREIUNDDREISSIG(33, 49, -2.5),
	VIERUNDDREISSIG(34, 50, -3.0);

	private final int alter;
	
	private final int trainerAlter;
	
	private final double zuwachsFaktor;
	
	ZuwachsFaktorAlter(int alter, int trainerAlter, double zuwachsFaktor) {
		this.alter = alter;
		this.trainerAlter = trainerAlter;
		this.zuwachsFaktor = zuwachsFaktor;
	}

	public int getAlter() {
		return alter;
	}

	public int getTrainerAlter() {
		return trainerAlter;
	}

	public double getZuwachsFaktor() {
		return zuwachsFaktor;
	}
}
