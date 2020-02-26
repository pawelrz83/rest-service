package com.example.restservice;

import java.net.Authenticator;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import com.example.restservice.HibernateMongoSessionUtils;
//import com.example.hibernate.model.Token;


@RestController
public class AuthenticateController {
	@Autowired
	public TokenRepository repository;
	/*
	 * private final AtomicLong counter = new AtomicLong();
	 * 
	 * //First function uses just serialisation and returns JSON with all fields
	 * made by get functions
	 * 
	 * @GetMapping("/authenticate1")
	 * 
	 * @ResponseBody public Authenticate authenticate1(@RequestParam(value = "pin")
	 * String name) { return new Authenticate(counter.incrementAndGet(), name); }
	 * 
	 * //Second one just throws json created manually by hash map and adding key
	 * pair "token" : "value" //where value is taken from getToken method on object
	 * authenticator which was initialized with pin before.
	 * 
	 * @GetMapping("/authenticate2")
	 * 
	 * @ResponseBody public Map<String, Object> authenticate2(@RequestParam(value =
	 * "pin") String name) { Map<String, Object> json_result = new
	 * LinkedHashMap<>(); Authenticate authenticator = new
	 * Authenticate(counter.incrementAndGet(), name); json_result.put("token",
	 * authenticator.getToken());
	 * 
	 * return json_result;
	 * 
	 * }
	 */
	
	/*
	 * @GetMapping("/authenticate3")
	 * 
	 * @ResponseBody public Map<String, Object> authenticate3(@RequestParam(value =
	 * "pin") String name) { Map<String, Object> json_result = new
	 * LinkedHashMap<>(); Authenticate authenticator = new
	 * Authenticate(counter.incrementAndGet(), name); json_result.put("token",
	 * authenticator.getToken());
	 * 
	 * return json_result;
	 * 
	 * }
	 */
	//------------------------------- Excerise 1 ------------------------------------------------
	//  client --->    {POST}  ---->  /authenticate    
	//  client <---    {RESPONSE} {token:"asdsadsadsa"}   <----  Server
	//      Client knows the token it can communicate and ask for everything...
	
	//  client --->    {POST}{token:"asdsadsadsa"} ----> /getPicture  (validation if token belongs to user and is valid)
	//  client <---    {RESPONSE}{data:"01010101010110"}  <----  Server 
	//---------------------------------------------------------------------------------------------
	
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	 // public ResponseEntity<String> authenticate(@RequestBody Authenticate authenticator) 
	
	  public ResponseEntity<Object> authenticate(@RequestBody Map<String, Object> json_result) {
		System.out.println(json_result);
		 boolean authenticated = false;
		 String token = json_result.get("token").toString();
		
		Authenticate authenticator = new Authenticate(
				json_result.get("user_id").toString(),
				json_result.get("pin").toString() 
				);
		System.out.println("I am after authenticator initialisation");	
		
		
		if( authenticator.getToken() != null )
		{
			System.out.println("token is: " + authenticator.getToken().getTokenString() );  // can be changed to serialisation str(token) = "aaaccc"
			// let's open the connection to database trying to save the token to MongoDB
			//Session session = HibernateMongoSessionUtils.getInstance().openSession();
			//Transaction tx = session.beginTransaction();
			//session.save(authenticator.getToken());
			authenticated = true;
			
			
			repository.save( authenticator.getToken() );
			System.out.println(authenticator.getToken().getId());
			
			if(authenticated) {
				HashMap<String, Object> entity = new HashMap<>();
			    entity.put(token, json_result);

			    return new ResponseEntity<Object>(entity, HttpStatus.OK); // TODO 1. How to add token into body of response! 
			
		}
			else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		}
		return null;
	}
	
	
	
	// curl --request POST http://localhost:8080/authenticate --header "Content-Type: application/json" -d "{\"id\":1, \"user_id\":\"rzeczkop\", \"pin\":\"1234\"}" -v
	
	@RequestMapping(value = "/getPicture", method = RequestMethod.POST)  // TODO 2. Write getPicture function
	 // public ResponseEntity<String> authenticate(@RequestBody Authenticate authenticator) 
	public ResponseEntity<Object> getPicture(@RequestBody Map<String, Object> json_result) 
	{
		System.out.println(json_result);
		String	user = json_result.get("user_id").toString();
		String token = json_result.get("token").toString();
		System.out.println(user);
		System.out.println(token);
		System.out.println(user.toString());
		System.out.println(token.toString());
		long validityEpoch = 60*60*24;
		
		List<Token> tokenList =  repository.findByUserid(json_result.get("user_id").toString());
//		
	    boolean authenticated = false;
		long currentEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);	
		for(Token tokenElem:tokenList) {
		//tokenList.forEach((tokenElem) -> {
            System.out.println(tokenElem);
            long tokenCreateEpoch = tokenElem.getCreated_date().toEpochSecond(ZoneOffset.UTC);            
            if(token.contentEquals(tokenElem.getTokenString())) {
            	
            	//System.out.println("Token usera" + user + "jest znaleziony w bazie");
            	System.out.printf("Token usera %s jest znaleziony w bazie", token);
            	if(currentEpoch - tokenCreateEpoch <= validityEpoch) {
                 	System.out.println("Token jest ważny" + tokenElem.getTokenString());
                 	authenticated = true;
                 }
            	else {
            		System.out.println("Token jest nieważny");
            	}
            }
           
            
           
        }
		
		if(authenticated) {
			String picture="/var/www/picture.jpg";
			HashMap<String, Object> entity = new HashMap<>();
		    entity.put("picture", picture);

		    return new ResponseEntity<Object>(entity, HttpStatus.OK);
//            return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO 1. How to add Picture into body of response! 
			
		}else 
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
			
//			System.out.println(tokenList);
//			
//			extracted(tokenList);
			
//			String picture="/var/www/picture.jpg";
			
		//curl --request POST http://localhost:8080/getPicture --header "Content-Type: application/json" -d "{\"user_id\":\"rzeczkop\", \"token\":\"fD4r_E1hvkkchgHeCBjHJCZ299CUOjXUm6F-6u68pCWDhNRbQsoBRev811Z_L51KoW80IB4jJmCh-KKDV6S9eVGEbY16UfzGi_6CR9ey530=\"}" -v
			
//			  Object valid;
//			for(token valid){
//				  System.out.println(picture);
//			  }
		// Simplification for beginnig, PICTURE String picture="/var/www/picture.jpg"
		// (token belongs to user_id ) && (token is valid)
		// 
		// 
		//  INPUT token:"asdsadsadsa"  -> Lista =  findByUserid("rzeczkop");
		
		//  for each token check:
		//     if token == lista.token
		//		  if it is still valid:
		//			we have it, we can return PICTURE, so string in body
		//     	  else  
		//		    return error token invalid    
		//     else
		//		  if it is invalid:
		//         invalidate token into database -> isValid - false
		// }
		
//		System.out.println(json_result);
//		
//		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
