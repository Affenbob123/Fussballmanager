package fussballmanager.service.team;

import fussballmanager.service.spieler.AufstellungsPositionsTypen;

public enum FormationsTypen {
	
	DREIVIERDREI("3-4-3", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIB, 
			AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, AufstellungsPositionsTypen.RM, AufstellungsPositionsTypen.OM, 
			AufstellungsPositionsTypen.LS, AufstellungsPositionsTypen.MS, AufstellungsPositionsTypen.RS}),
	DREIFFUENFZWEI("3-5-2", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIB, 
			AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, AufstellungsPositionsTypen.RM, AufstellungsPositionsTypen.ZM, 
			AufstellungsPositionsTypen.OM, AufstellungsPositionsTypen.LS, AufstellungsPositionsTypen.RS}),
	VIERDREIDREI("4-3-3", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIV, 
			AufstellungsPositionsTypen.RIV,AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.ZM, AufstellungsPositionsTypen.RM, 
			AufstellungsPositionsTypen.LS, AufstellungsPositionsTypen.MS, AufstellungsPositionsTypen.RS}),
	VIERVIERZWEI("4-4-2", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIV, 
			AufstellungsPositionsTypen.RIV,AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, AufstellungsPositionsTypen.RM, 
			AufstellungsPositionsTypen.OM, AufstellungsPositionsTypen.LS, AufstellungsPositionsTypen.RS}),
	VIERFUENFEINS("4-5-1", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIV, 
			AufstellungsPositionsTypen.RIV,AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, AufstellungsPositionsTypen.RM, 
			AufstellungsPositionsTypen.ZM, AufstellungsPositionsTypen.OM, AufstellungsPositionsTypen.MS}),
	FUENFDREIZWEI("5-3-2", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIV, 
			AufstellungsPositionsTypen.LIB, AufstellungsPositionsTypen.RIV,AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, 
			AufstellungsPositionsTypen.RM, AufstellungsPositionsTypen.LS, AufstellungsPositionsTypen.RS}),
	FUENFVIEREINS("5-4-1", new AufstellungsPositionsTypen[] {AufstellungsPositionsTypen.TW, AufstellungsPositionsTypen.LV, AufstellungsPositionsTypen.LIV, 
			AufstellungsPositionsTypen.LIB, AufstellungsPositionsTypen.RIV,AufstellungsPositionsTypen.RV, AufstellungsPositionsTypen.LM, AufstellungsPositionsTypen.DM, 
			AufstellungsPositionsTypen.RM, AufstellungsPositionsTypen.OM, AufstellungsPositionsTypen.MS});
	
	private final String bezeichnung;
	private final AufstellungsPositionsTypen[] aufstellungsPositionsTypen;
	
	FormationsTypen(String bezeichnung, AufstellungsPositionsTypen[] aufstellungsPositionsTypen) {
		this.aufstellungsPositionsTypen = aufstellungsPositionsTypen;
		this.bezeichnung = bezeichnung;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public AufstellungsPositionsTypen[] getAufstellungsPositionsTypen() {
		return aufstellungsPositionsTypen;
	}
	
	public boolean ueberpruefeObAufstellungEinePositionBeinhaltet(FormationsTypen formationsTyp, AufstellungsPositionsTypen aufstellungsPositionsTyp) {
		boolean b = false;
		for(AufstellungsPositionsTypen a : formationsTyp.getAufstellungsPositionsTypen()) {
			if(a.equals(aufstellungsPositionsTyp)) {
				b = true;
				break;
			}
		}
		
		return b;
	}
}
