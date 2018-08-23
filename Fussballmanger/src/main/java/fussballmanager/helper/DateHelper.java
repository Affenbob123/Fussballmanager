package fussballmanager.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateHelper {

	public LocalDateTime htmlStringToDate(String s) throws DateTimeParseException{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		return LocalDateTime.parse(s, formatter);
	}
	
	public String dateToStringJavascript(LocalDateTime date) {
		String s;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		s = date.format(formatter);
		return s; 
	}
}
