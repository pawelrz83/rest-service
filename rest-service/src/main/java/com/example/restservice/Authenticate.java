package com.example.restservice;

public class Authenticate {

	//private final long id;
	private final String validPin = "1234";
	private final String pin;
	private final String user_id;
	private Token token;
	
	private String series = "167c051c4f52d2f613155e21f04bd513d816a2389dce3842a8ea5c6a7b943f19";
	
	public Authenticate(String user_id, String pin) {
		this.pin = pin;
		this.user_id = user_id;
		System.out.println("Constructor initialising");
		System.out.println("validPin is: " + this.validPin);
		System.out.println("pin is: " + this.pin);
		System.out.println("user_id is: " + this.user_id);
		//condition to check authentication by pin
		if(this.pin.equals(this.validPin))
		{
			System.out.println("Iniside TOken constructor");
			this.token = new Token(user_id);
			// Initialisation for Token object, constructor creates there token.
			System.out.println("Token generation");
		}
		else
		{
			this.token = null;
		}
	}

	public Token getToken(){
		return token;
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	
	private String salt = "234resdfsdfzsdf";
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
