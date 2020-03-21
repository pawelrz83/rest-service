	package com.example.restservice;

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
	public class Token {

		@Id
		private String id;
		private String token;
		private String userid;
		private boolean isValid=true;  // default value - true. How to change it to false, when it will expire?
		private LocalDateTime created_date = LocalDateTime.now(); // TODO change for local PL time or time of the server.
		// not GMT becacuse there will be some glitches with comparing the time : 

		public Token() {}
		public Token(String userid) {
			// function should check if user has already created a token and it's stored somewhere in the database
			// if there is some valid we may get it and return to the user. He is ok because he provided good pin
			// so we authenticate him.
			// Or we invalidate his previous token and create a new one.
			this.userid=userid; //e.g. rzeczkop or pablo or castor ...
			generateToken();
		}
		
		public String generateToken()
		{
			Base64.Encoder base64Encoder = Base64.getUrlEncoder(); 
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[80];
		    random.nextBytes(bytes);
			this.token = base64Encoder.encodeToString(bytes);
			return this.token;
		}
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTokenString() {
			return token;
		}

		public String getUser_id() {
			return userid;
		}

		
		public LocalDateTime getCreated_date() {
			return created_date;
		}

		public void setCreated_date(LocalDateTime created_date) {
			this.created_date = created_date;
		}

		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("UserProfile [id=").append(id).append(", token=")
					.append(token).append(", created_date=")
					.append("]");
			return builder.toString();
		}
		
		private String series = "167c051c4f52d2f613155e21f04bd513d816a2389dce3842a8ea5c6a7b943f19";
		
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



