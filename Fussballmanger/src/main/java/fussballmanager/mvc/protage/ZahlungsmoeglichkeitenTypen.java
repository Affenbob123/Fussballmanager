package fussballmanager.mvc.protage;

public enum ZahlungsmoeglichkeitenTypen {

	KREDITKARTE("/zahlungsmoeglichkeiten/icon_creditcard.png"),
	LASTSCHRIFT("/zahlungsmoeglichkeiten/icon_lastschrift.png"),
	PAYPAL("/zahlungsmoeglichkeiten/icon_paypal.png"),
	SOFORTUEBERWEISUNG("/zahlungsmoeglichkeiten/icon_sofortueberweisung.png"),
	UEBERWEISUNG("/zahlungsmoeglichkeiten/icon_ueberweisung.png");
	
	private final String bild;
	
	ZahlungsmoeglichkeitenTypen(String bild) {
		this.bild = bild;
	}

	public String getBild() {
		return bild;
	}
}
