package fussballmanager.spielsimulation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import fussballmanager.service.spieler.AufstellungsPositionsTypen;
import fussballmanager.service.spieler.Spieler;

@Service
@Transactional
public class TorVersuchWahrscheinlicheit {

	public int wahrscheinlichkeitDasHeimmannschaftImAngriffIst(List<Spieler> spielerHeimmannschaft, List<Spieler> spielerGastmannschaft, double heimVorteil) {
		int wahrscheinlichkeit;

		double staerkeHeimmannschaft = 0.0;
		double staerkeGastmannschaft = 0.0;
		
		for(Spieler spieler : spielerHeimmannschaft) {
			staerkeHeimmannschaft = staerkeHeimmannschaft + spieler.getSpielerStaerke();
		}
		
		for(Spieler spieler : spielerGastmannschaft) {
			staerkeGastmannschaft = staerkeGastmannschaft + spieler.getSpielerStaerke();
		}
		
		staerkeHeimmannschaft = staerkeHeimmannschaft * heimVorteil;
		wahrscheinlichkeit = (int) ((staerkeHeimmannschaft) * 100 / (staerkeHeimmannschaft + staerkeGastmannschaft));
		
		return wahrscheinlichkeit;
	}
	
	public double wahrscheinlichkeitTorwartGegenTorwart(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		Spieler torwartDesAngreifers = null;
		Spieler torwartDesVerteidigers = null;
		double durchschnittsStaerkeTorwartAngreifer = 0.0;
		double durchschnittsStaerkeTorwartVerteidiger = 0.0;
		
		for(Spieler spieler : spielerAngreifer) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TW)) {
				torwartDesAngreifers = spieler;
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.TW)) {
				torwartDesVerteidigers = spieler;
			}
		}
		
		if(torwartDesVerteidigers == null) {
			return 1.0;
		}
		
		if(torwartDesAngreifers == null) {
			return 0.1;
		}
		
		durchschnittsStaerkeTorwartAngreifer = torwartDesAngreifers.getSpielerStaerke();
		durchschnittsStaerkeTorwartVerteidiger = torwartDesVerteidigers.getSpielerStaerke();
		
		durchschnittsStaerkeTorwartAngreifer = durchschnittsStaerkeTorwartAngreifer * staerkeFaktor;
		return (durchschnittsStaerkeTorwartAngreifer) / 
				(durchschnittsStaerkeTorwartAngreifer + durchschnittsStaerkeTorwartVerteidiger);
	}
	
	public double wahrscheinlichkeitAbwehrGegenAngriff(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		int gewichtungPassenAngreifer = 40;
		int gewichtungDribbelnAngreifer = 25;
		int gewichtungGeschwindigkeitAngreifer = 15;
		int gewichtungPhysisAngreifer = 20;
		
		int gewichtungGeschwindigkeitVerteiger = 50;
		int gewichtungPhysisVerteiger = 20;
		int gewichtungVerteidigungVerteiger = 30;
		
		List<Spieler> abwehrDesAngreifers = new ArrayList<>();
		List<Spieler> sturmDesVerteidigers = new ArrayList<>();
		
		double staerkenDesAngreifers = 0.0;
		double staerkenDesVerteidigers = 0.0;
		
		for(Spieler spieler : spielerAngreifer) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIB) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RIV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RV)) {
				abwehrDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LS) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.MS) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RS)) {
				sturmDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, geschwindigkeit, physis der Verteidigung des Angreifers
		for(Spieler spieler : abwehrDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getSpielerReinStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getSpielerReinStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer)
					+ (spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisAngreifer)) / 100);
		}
				
		//berechnet den durchschnittswert von schnelligkeit, Verteidigung, Physis der Angreifer des Verteidigers
		for(Spieler spieler : sturmDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getSpielerReinStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (staerkenDesAngreifers) / (staerkenDesAngreifers + staerkenDesVerteidigers);
	}
	
	public double wahrscheinlichkeitMittelfeldGegenMittelfeld(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		int gewichtungPassenAngreifer = 30;
		int gewichtungDribbelnAngreifer = 25;
		int gewichtungPhysisAngreifer = 15;
		int gewichtungGeschwindigkeitAngreifer = 10;
		int gewichtungSchießenAngreifer = 20;
		
		int gewichtungPhysisVerteiger = 40;
		int gewichtungVerteidigungVerteiger = 40;
		int gewichtungGeschwindigkeitVerteiger = 20;
		
		List<Spieler> mittelfeldDesAngreifers = new ArrayList<>();
		List<Spieler> mittelfeldDesVerteidigers = new ArrayList<>();
		
		double staerkenDesAngreifers = 0.0;
		double staerkenDesVerteidigers = 0.0;
		
		for(Spieler spieler : spielerAngreifer) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.DM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ZM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.OM)) {
				mittelfeldDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.DM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.ZM) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.OM)) {
				mittelfeldDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, physis, geschwindigkeit, schießen des Mittelfeldes des Angreifers
		for(Spieler spieler : mittelfeldDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getSpielerReinStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getSpielerReinStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisAngreifer) + 
					(spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer) + (spieler.getSpielerReinStaerke().getSchiessen() * gewichtungSchießenAngreifer)) / 100);
		}
		
		//berechnet den durchschnittswert von physis, verteidigung, geschwindigkeit des Mittelfeldes des Verteidigers
		for(Spieler spieler : mittelfeldDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getSpielerReinStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (staerkenDesAngreifers) / (staerkenDesAngreifers + staerkenDesVerteidigers);
	}
	
	public double wahrscheinlichkeitAngriffGegenAbwehr(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		int gewichtungPassenAngreifer = 10;
		int gewichtungDribbelnAngreifer = 15;
		int gewichtungPhysisAngreifer = 15;
		int gewichtungGeschwindigkeitAngreifer = 30;
		int gewichtungSchießenAngreifer = 30;
		
		int gewichtungPhysisVerteiger = 40;
		int gewichtungVerteidigungVerteiger = 40;
		int gewichtungGeschwindigkeitVerteiger = 20;
		
		List<Spieler> angriffDesAngreifers = new ArrayList<>();
		List<Spieler> verteidigungDesVerteidigers = new ArrayList<>();
		
		double staerkenDesAngreifers = 0.0;
		double staerkenDesVerteidigers = 0.0;
		
		for(Spieler spieler : spielerAngreifer) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LS) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.MS) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RS)) {
				angriffDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.LIB) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RIV) || 
					spieler.getAufstellungsPositionsTyp().equals(AufstellungsPositionsTypen.RV)) {
				verteidigungDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, physis, geschwindigkeit, schießen des Mittelfeldes des Angreifers
		for(Spieler spieler : angriffDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getSpielerReinStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getSpielerReinStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisAngreifer) + 
					(spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer) + (spieler.getSpielerReinStaerke().getSchiessen() * gewichtungSchießenAngreifer)) / 100);
		}
		
		//berechnet den durchschnittswert von physis, verteidigung, geschwindigkeit des Mittelfeldes des Verteidigers
		for(Spieler spieler : verteidigungDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getSpielerReinStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getSpielerReinStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getSpielerReinStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (staerkenDesAngreifers) / (staerkenDesAngreifers + staerkenDesVerteidigers);
	}
}
