package fussballmanager.spielsimulation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import fussballmanager.service.spieler.PositionenTypen;
import fussballmanager.service.spieler.Spieler;

@Service
@Transactional
public class TorVersuchWahrscheinlicheit {

	public int wahrscheinlichkeitDasHeimmannschaftImAngriffIst(List<Spieler> spielerHeimmannschaft, List<Spieler> spielerGastmannschaft, double staerkeFaktor) {
		int wahrscheinlichkeit;

		double staerkeHeimmannschaft = 0.0;
		double staerkeGastmannschaft = 0.0;
		
		for(Spieler spieler : spielerHeimmannschaft) {
			staerkeHeimmannschaft = staerkeHeimmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
		}
		
		for(Spieler spieler : spielerGastmannschaft) {
			staerkeGastmannschaft = staerkeGastmannschaft + spieler.getStaerke().getDurchschnittsStaerke();
		}
		
		staerkeHeimmannschaft = staerkeHeimmannschaft * staerkeFaktor;
		wahrscheinlichkeit = (int) ((staerkeHeimmannschaft) * 100 / (staerkeHeimmannschaft + staerkeGastmannschaft));
		
		return wahrscheinlichkeit;
	}
	
	public double wahrscheinlichkeitDasDerAngriffZumTorschussFuehrt(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		double wahrscheinlichkeit;
		return 0;
	}
	
	public double wahrscheinlichkeitDasDerAngreiferEinenTorschussHat(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		double wahrscheinlichkeit;
		return 0;
	}
	
	public double wahrscheinlichkeitDasDerAngreiferEinenTorversuchHat(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		int wahrscheinlichkeit;
		return 0;
	}
	
	public int wahrscheinlichkeitTorwartGegenTorwart(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
		Spieler torwartDesAngreifers = null;
		Spieler torwartDesVerteidigers = null;
		double durchschnittsStaerkeTorwartAngreifer = 0.0;
		double durchschnittsStaerkeTorwartVerteidiger = 0.0;
		
		for(Spieler spieler : spielerAngreifer) {
			if(spieler.getPosition().equals(PositionenTypen.TW)) {
				torwartDesAngreifers = spieler;
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getPosition().equals(PositionenTypen.TW)) {
				torwartDesVerteidigers = spieler;
			}
		}
		
		if(torwartDesVerteidigers == null) {
			return 100;
		}
		
		if(torwartDesAngreifers == null) {
			return 10;
		}
		
		durchschnittsStaerkeTorwartAngreifer = torwartDesAngreifers.getStaerke().getDurchschnittsStaerke();
		durchschnittsStaerkeTorwartVerteidiger = torwartDesVerteidigers.getStaerke().getDurchschnittsStaerke();
		
		return (int) (durchschnittsStaerkeTorwartAngreifer / 
				(durchschnittsStaerkeTorwartAngreifer + durchschnittsStaerkeTorwartVerteidiger));
	}
	
	public int wahrscheinlichkeitAbwehrGegenAngriff(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
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
			if(spieler.getPosition().equals(PositionenTypen.LV) || spieler.getPosition().equals(PositionenTypen.LIV) 
					|| spieler.getPosition().equals(PositionenTypen.LIB) || spieler.getPosition().equals(PositionenTypen.RIV) || 
					spieler.getPosition().equals(PositionenTypen.RV)) {
				abwehrDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getPosition().equals(PositionenTypen.LS) || spieler.getPosition().equals(PositionenTypen.MS) || 
					spieler.getPosition().equals(PositionenTypen.RS)) {
				sturmDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, geschwindigkeit, physis der Verteidigung des Angreifers
		for(Spieler spieler : abwehrDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer)
					+ (spieler.getStaerke().getPhysis() * gewichtungPhysisAngreifer)) / 100);
		}
				
		//berechnet den durchschnittswert von schnelligkeit, Verteidigung, Physis der Angreifer des Verteidigers
		for(Spieler spieler : sturmDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (int) ((staerkenDesAngreifers * 100) / (staerkenDesAngreifers + staerkenDesVerteidigers));
	}
	
	public int wahrscheinlichkeitMittelfeldGegenMittelfeld(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
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
			if(spieler.getPosition().equals(PositionenTypen.LM) || spieler.getPosition().equals(PositionenTypen.DM) 
					|| spieler.getPosition().equals(PositionenTypen.RM) || spieler.getPosition().equals(PositionenTypen.ZM) || 
					spieler.getPosition().equals(PositionenTypen.OM)) {
				mittelfeldDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getPosition().equals(PositionenTypen.LM) || spieler.getPosition().equals(PositionenTypen.DM) 
					|| spieler.getPosition().equals(PositionenTypen.RM) || spieler.getPosition().equals(PositionenTypen.ZM) || 
					spieler.getPosition().equals(PositionenTypen.OM)) {
				mittelfeldDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, physis, geschwindigkeit, schießen des Mittelfeldes des Angreifers
		for(Spieler spieler : mittelfeldDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getStaerke().getPhysis() * gewichtungPhysisAngreifer) + 
					(spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer) + (spieler.getStaerke().getSchießen() * gewichtungSchießenAngreifer)) / 100);
		}
		
		//berechnet den durchschnittswert von physis, verteidigung, geschwindigkeit des Mittelfeldes des Verteidigers
		for(Spieler spieler : mittelfeldDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (int) ((staerkenDesAngreifers * 100) / (staerkenDesAngreifers + staerkenDesVerteidigers));
	}
	
	public int wahrscheinlichkeitAngriffGegenAbwehr(List<Spieler> spielerAngreifer, List<Spieler> spielerVerteidiger, double staerkeFaktor) {
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
			if(spieler.getPosition().equals(PositionenTypen.LS) || spieler.getPosition().equals(PositionenTypen.MS) 
					|| spieler.getPosition().equals(PositionenTypen.RS)) {
				angriffDesAngreifers.add(spieler);
			}
		}
		
		for(Spieler spieler : spielerVerteidiger) {
			if(spieler.getPosition().equals(PositionenTypen.LV) || spieler.getPosition().equals(PositionenTypen.LIV) 
					|| spieler.getPosition().equals(PositionenTypen.LIB) || spieler.getPosition().equals(PositionenTypen.RIV) || 
					spieler.getPosition().equals(PositionenTypen.RV)) {
				verteidigungDesVerteidigers.add(spieler);
			}
		}
		
		//berechnet den durchschnittswert von passen, dribbeln, physis, geschwindigkeit, schießen des Mittelfeldes des Angreifers
		for(Spieler spieler : angriffDesAngreifers) {
			staerkenDesAngreifers = staerkenDesAngreifers + (((spieler.getStaerke().getPassen() * gewichtungPassenAngreifer) + 
					(spieler.getStaerke().getDribbeln() * gewichtungDribbelnAngreifer) + (spieler.getStaerke().getPhysis() * gewichtungPhysisAngreifer) + 
					(spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitAngreifer) + (spieler.getStaerke().getSchießen() * gewichtungSchießenAngreifer)) / 100);
		}
		
		//berechnet den durchschnittswert von physis, verteidigung, geschwindigkeit des Mittelfeldes des Verteidigers
		for(Spieler spieler : verteidigungDesVerteidigers) {
			staerkenDesVerteidigers = staerkenDesVerteidigers + (((spieler.getStaerke().getVerteidigen() * gewichtungVerteidigungVerteiger) + 
					(spieler.getStaerke().getGeschwindigkeit() * gewichtungGeschwindigkeitVerteiger) + 
					(spieler.getStaerke().getPhysis() * gewichtungPhysisVerteiger)) / 100);
		}
		
		staerkenDesAngreifers = staerkenDesAngreifers * staerkeFaktor;
		
		return (int) ((staerkenDesAngreifers * 100) / (staerkenDesAngreifers + staerkenDesVerteidigers));
	}
}
