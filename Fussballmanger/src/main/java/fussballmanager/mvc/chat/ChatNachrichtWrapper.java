package fussballmanager.mvc.chat;

import fussballmanager.service.chat.Chat;
import fussballmanager.service.chat.nachricht.Nachricht;

public class ChatNachrichtWrapper {

	private Chat chat;
	
	private String nachricht;

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public String getNachricht() {
		return nachricht;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}
}
