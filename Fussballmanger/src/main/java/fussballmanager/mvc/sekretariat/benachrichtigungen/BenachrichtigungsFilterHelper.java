package fussballmanager.mvc.sekretariat.benachrichtigungen;

import fussballmanager.service.benachrichtigung.BenachrichtigungsTypen;

public class BenachrichtigungsFilterHelper {

	private BenachrichtigungsTypen benachrichtigungsTyp;

	public BenachrichtigungsTypen getBenachrichtigungsTyp() {
		return benachrichtigungsTyp;
	}

	public void setBenachrichtigungsTyp(BenachrichtigungsTypen benachrichtigungsTyp) {
		this.benachrichtigungsTyp = benachrichtigungsTyp;
	}
}
