package com.example.restservice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {

	private final AtomicLong counter = new AtomicLong();

	//First function uses just serialization and returns JSON with all fields made by get functions
	@GetMapping("/authenticate1")
	@ResponseBody
	public Authenticate authenticate1(@RequestParam(value = "pin") String name) {
		return new Authenticate(counter.incrementAndGet(), name);
	}

	//Second one just throws json created manually by hash map and adding key pair "token" : "value" 
	//where value is taken from getToken method on object authenticator which was initialized with pin before.
	@GetMapping("/authenticate2")
	@ResponseBody
	public Map<String, Object> authenticate2(@RequestParam(value = "pin") String name) 
	{
		Map<String, Object> json_result = new LinkedHashMap<>();
		Authenticate authenticator = new Authenticate(counter.incrementAndGet(), name);
	    json_result.put("token", authenticator.getToken());

	    return json_result;
		
	}
	
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
	
	@RequestMapping(value = "/authenticate3", method = RequestMethod.POST)
	 // public ResponseEntity<String> authenticate3(@RequestBody Authenticate authenticator) 
	  public ResponseEntity<String> authenticate3(@RequestBody Map<String, Object> json_result) 
	{
		System.out.println(json_result);
		
		
		Authenticate authenticator = new Authenticate(
				Long.parseLong( json_result.get("id").toString() ), 
				json_result.get("pin").toString() 
				);
		
		System.out.println("token is: " + authenticator.getToken() );
		if( authenticator.getToken().equals("error") )
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}else {
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}
	}
	
	
	
	//Write another requestBody /authenticate3 where you check if pin is ok and you return json with id, token but 
	//if pin is not valid, return json {'status':'401', 'msg':'Unauthorized'}  not less and no more than this information
	
	//If we have multiple GetMapping("/functionXYZ") and each time we construct this return json object, we may need to create 
	//another class like Result.java which would have constructor :
	//public Result(long statusCode) inside where there would be condition to check what;s the status code is
	// if statusCode==401 then return {'status':401,'msg':'Unauthorized'}
	// if statusCode==200 then return {'status':200}
	// if statusCode==404 then return {'status':404, 'msg':'Not found'}
	// if statusCode==500 then return {'status':500, 'msg':'Internal Error'}
	//All these errors are common HTTP status codes well known in internet. It could be used as a builder for json_result 
	//to not mess with the same steps each time
	
	
}
