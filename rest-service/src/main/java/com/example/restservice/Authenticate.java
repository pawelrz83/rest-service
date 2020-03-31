package com.example.restservice;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Authenticate {

	//private final long id;
	private final String validPin = "1234";
	private final String pin;
	private final String user_id;
	private Token token;
	private String salt = "234resdfsdfzsdf";
	private String hash = "Sz5nUC9XFlI2yHh1fH0218oO2Kii0IfUjDAk3zgL8N8=";
	private String series = "167c051c4f52d2f613155e21f04bd513d816a2389dce3842a8ea5c6a7b943f19";
	
/*//Stara wersja konstruktora Authenticate, poniżej napiszę nową wersję z wykorzystaniem salta
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
*/

	public Authenticate(String user_id, String pin) {
		this.pin = pin;
		this.user_id = user_id;	
		MessageDigest md = null;
		
		try {
			md = MessageDigest.getInstance("SHA-256");
		}catch (NoSuchAlgorithmException e) {
		    throw new IllegalStateException("System doesn't support SHA-256 algorithm.");
		}
		
		String userpinAndSalt = pin + this.salt;
    	md.update( userpinAndSalt.getBytes() ); 
    
    	byte[] bytes = md.digest();   // chyba to jest zakodowanie hasha sha256 do zmiennej bytes. 
    	System.out.println(bytes);  // wartość tego powinna być docelową wartością hasła zapamiętanego w bazie albo w definicji klasy.
    	
    	System.out.println("salt: " + this.salt);
    	System.out.println("pin: " + this.pin);
    	System.out.println("Encoding salt+valid pin: " + this.salt + this.validPin);
    	System.out.println("Honestly I don't need to do that... only once at the begining to save the result of this operation for future comparisons.");
    	System.out.println("result of it 'bytes' var: " + bytes);
    	System.out.println("Encoding salt+user pin: " + this.salt + this.pin);
    	System.out.println("my variable series, WTF is that ??? based on my fucking calculations above I have to remember hash as 'bytes' and not this shit... : " + this.series);
    	// I added static value bytes in the class definintion on top
    	// but... if you will run this program few times yo will see that this bytes variable is changing
    	// so I googled why and it sounds like String representation of bytes will be always different
    	// we have to cast all into base64 encoded String and this one will be saved finally into class hash definition
    	System.out.println("String encoded :"+Base64.getEncoder().encodeToString(bytes));
    	System.out.println("does 'bytes' var equals to bytes.toString() ?? : " + bytes.toString());
    	System.out.println("sounds like not... we have to compare base64 representation of hashes");
    	
    	if(Base64.getEncoder().encodeToString(bytes).equals(this.hash))
		{
			System.out.println("I valdiated salt and ping hash with saved final hash in class definition");
			this.token = new Token(user_id);
			// Initialisation for Token object, constructor creates there token.
			System.out.println("Token generation");
		}
		else
		{
			System.out.println("Condition false.. wrong pin or hashing messed.");
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
	
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
