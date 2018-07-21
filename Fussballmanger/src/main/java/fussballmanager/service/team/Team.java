package fussballmanager.service.team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Team implements Comparable<Team>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	private String name;
		
	private double geld;
	
	
	public Team(String name, double geld) {
		this.name = name;
		this.geld = geld;
	}
	
	public Team() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGeld() {
		return geld;
	}

	public void setGeld(double geld) {
		this.geld = geld;
	}

	@Override
	public int compareTo(Team arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
