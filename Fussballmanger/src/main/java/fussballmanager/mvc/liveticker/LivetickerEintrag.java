package fussballmanager.mvc.liveticker;

import java.util.List;

import fussballmanager.mvc.spiel.SpielEintrag;
import fussballmanager.mvc.spiel.SpielEreignisEintrag;

public class LivetickerEintrag {

	SpielEintrag spielEintrag;
	
	List<SpielEreignisEintrag> spielEreignisEintraege;
	
	public SpielEintrag getSpielEintrag() {
		return spielEintrag;
	}

	public void setSpielEintrag(SpielEintrag spielEintrag) {
		this.spielEintrag = spielEintrag;
	}

	public List<SpielEreignisEintrag> getSpielEreignisEintraege() {
		return spielEreignisEintraege;
	}

	public void setSpielEreignisEintraege(List<SpielEreignisEintrag> spielEreignisEintraege) {
		this.spielEreignisEintraege = spielEreignisEintraege;
	}
}
