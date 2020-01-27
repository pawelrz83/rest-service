package com.example.restservice;


import java.security.SecureRandom;
import java.util.Base64;

public class Authenticate {


private final long id;
private final String validPin = "1234";
private final String pin;
private String token="error";

public Authenticate(long id, String pin) {
this.id = id;
this.pin = pin;
System.out.println("Constructor initialising");
System.out.println("validPin is: " + this.validPin);
System.out.println("pin is: " + this.pin);
//condition to check authentication by pin
if(this.pin.equals(this.validPin))
{
System.out.println("Token generation");
this.token = generateToken();
}
}

public long getId() {
return id;
}

public String generateToken()
{
Base64.Encoder base64Encoder = Base64.getUrlEncoder();
SecureRandom random = new SecureRandom();
byte bytes[] = new byte[80];
   random.nextBytes(bytes);
String token = base64Encoder.encodeToString(bytes);
return token;
}

public String getContent() {
System.out.println("Getting content method");
return token;
}
}