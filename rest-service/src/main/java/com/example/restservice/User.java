	package com.example.restservice;

	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	import java.security.SecureRandom;
	import java.time.LocalDateTime;
	import java.util.Base64;
	import java.util.Date;
	import java.util.List;

	import javax.persistence.ElementCollection;
	import javax.persistence.Entity;
	import javax.persistence.GeneratedValue;
	import javax.persistence.Id;
	//import org.springframework.data.annotation.Id;

	//import org.hibernate.annotations.GenericGenerator;
	//import org.hibernate.search.annotations.IndexedEmbedded;

	@Entity
	public class User {
		@Id
		private String id;
		private String salt;
		private String shadow;
		private String userid;
		
		public User() {}
		public User(String userid, String passwd) {
			System.out.println("Start of constructor");
			if(passwd.isEmpty())
			{
				passwd = "1234";  //to load from configuration because when I wanted to use private final variable it always tried to save that into Database
				// TODO find the way to eliminate fields saved to database.
			}
			
			this.userid = userid; 
			//generate salt
			System.out.println("Before salt generation");
			this.salt = generateSalt();
			//error handling +
			//encode salt+passwd = shadow
			System.out.println("Before passwd encoding");
			this.shadow = passwordEncode(passwd);
			//error handling +
		}
		
		public String passwordEncode(String passwd)
		{
			MessageDigest md = null;
			
			try {
				md = MessageDigest.getInstance("SHA-256");
			}catch (NoSuchAlgorithmException e) {
			    throw new IllegalStateException("System doesn't support SHA-256 algorithm.");
			}

			String saltedPassword = passwd + this.salt;
	    	md.update( saltedPassword.getBytes() ); 
	    
	    	byte[] bytes = md.digest();  
	    	
	    	return Base64.getEncoder().encodeToString(bytes);
		}
		
		public String generateSalt()
		{
			Base64.Encoder base64Encoder = Base64.getUrlEncoder(); 
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[10];
		    random.nextBytes(bytes);
			String randomString = base64Encoder.encodeToString(bytes);
			return randomString;
		}
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		private String getSalt() {
			return this.salt;
		}

		public String getUserid() {
			return this.userid;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("UserProfile [id=").append(id).append(", userid=")
					.append(userid).append(", shadow=").append(shadow).append("]");
			return builder.toString();
		}
			
	}



