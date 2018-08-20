package fussballmanager.service.chat;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import fussballmanager.service.chat.nachricht.Nachricht;
import fussballmanager.service.user.User;

@Entity
public class Chat implements Comparable<Chat>{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	
	private String chatName;
	
	@ManyToMany
	private List<User> user;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Nachricht> nachrichten;
		
	public Chat() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	public List<Nachricht> getNachrichten() {
		return nachrichten;
	}

	public void setNachrichten(List<Nachricht> nachrichten) {
		this.nachrichten = nachrichten;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}

	@Override
	public int compareTo(Chat compareTo) {
		if(compareTo.getNachrichten().isEmpty()) {
			return 1;
		}
		
		if(this.getNachrichten().isEmpty()) {
			return -1;
		}
		Nachricht compareToNeusteNachricht = compareTo.getNachrichten().get(compareTo.getNachrichten().size() - 1);
		
		return compareToNeusteNachricht.compareTo(this.getNachrichten().get(this.getNachrichten().size() - 1));
	}
}
