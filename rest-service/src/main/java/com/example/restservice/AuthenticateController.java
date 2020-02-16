package com.example.restservice;

import java.util.LinkedHashMap;
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
	  public ResponseEntity<String> authenticate(@RequestBody Map<String, Object> json_result) 
	{
		System.out.println(json_result);
		
		
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
			
			
			repository.save( authenticator.getToken() );
			System.out.println(authenticator.getToken().getId());
			
			return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO 1. How to add token into body of response! 
			
		}else 
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	// curl --request POST http://localhost:8080/authenticate3 --header "Content-Type: application/json" -d "{\"id\":1, \"user_id\":\"rzeczkop\", \"pin\":\"12342\"}" -v
	
	@RequestMapping(value = "/getPicture", method = RequestMethod.POST)  // TODO 2. Write getPicture function
	 // public ResponseEntity<String> authenticate(@RequestBody Authenticate authenticator) 
	public ResponseEntity<String> getPicture(@RequestBody Map<String, Object> json_result) 
	{
		
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
		
		System.out.println(json_result);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
