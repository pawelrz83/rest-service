package com.example.restservice;

public class Authenticate {

	private final long id;
	private final String validPin = "1234";
	private final String pin;
	private final String user_id;
	private Token token;
	
	public Authenticate(long id, String user_id, String pin) {
		this.id = id;
		this.pin = pin;
		this.user_id = user_id;
		System.out.println("Constructor initialising");
		System.out.println("validPin is: " + this.validPin);
		System.out.println("pin is: " + this.pin);
		System.out.println("user_id is: " + this.user_id);
		//condition to check authentication by pin
		if(this.pin.equals(this.validPin))
		{
			this.token = new Token(user_id);
			// Initialisation for Token object, constructor creates there token.
			System.out.println("Token generation");
		}
		else
		{
			this.token = null;
		}
	}

	public long getId() {
		return id;
	}
	
	
	public Token getToken(){
		return token;
	}
}
