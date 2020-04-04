package com.example.restservice;

import java.net.Authenticator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
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
	public TokenRepository tokenRepository;
	@Autowired
	public UserRepository userRepository;
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
			
			
			tokenRepository.save( authenticator.getToken() );
			System.out.println(authenticator.getToken().getId());
			
			return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO 1. How to add token into body of response! 
			
		}else 
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
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
		
		List<Token> tokenList =  tokenRepository.findByUserid(json_result.get("user_id").toString());
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
	
	@RequestMapping(value = "/secureAuthenticate", method = RequestMethod.POST)
	public ResponseEntity<Object> secureAuthenticate(@RequestBody Map<String, Object> json_result) 
	{	         
	   	
	    	System.out.println(json_result);
			boolean authenticated = false;
			
			Authenticate authenticator = new Authenticate(
					json_result.get("user_id").toString(),
					json_result.get("pin").toString() 
					);
			System.out.println("I am after authenticator initialisation");
		
	
// te porównanie poniżej powinno być w konstruktorze klasy authenticate() kontroler nie ma za zadanie analizowania logiki
// on ma tutaj tylko strzelac z akcji, wykonywać funkcje wyświetlajace, wykonujące. Nie możesz tutaj przeprowadzać operacji
// na otwartym sercu, robie commenta poniżej i robie po swojemu, zobacz jak!
/*	    	
	    	if(userpinAndSalt.equals(authenticator.getSeries()))	
			{
				System.out.println("SHA-256 authenticate");
				return ResponseEntity.status(HttpStatus.OK).build();
			
			}			
	    	else 
			{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
*/			
			if( authenticator.getToken() != null )
			{
				System.out.println("token is: " + authenticator.getToken().getTokenString() );  // can be changed to serialisation str(token) = "aaaccc"
				// let's open the connection to database trying to save the token to MongoDB
				//Session session = HibernateMongoSessionUtils.getInstance().openSession();
				//Transaction tx = session.beginTransaction();
				//session.save(authenticator.getToken());
				authenticated = true;
				
				
				tokenRepository.save( authenticator.getToken() );
				System.out.println(authenticator.getToken().getId());
				
				return ResponseEntity.status(HttpStatus.CREATED).build(); // TODO 1. How to add token into body of response! 
				
			}else 
			{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
	}
	


//curl --request POST http://localhost:8080/secureAuthenticate --header "Content-Type: application/json" -d "{\"id\":1, \"user_id\":\"rzeczkop\", \"pin\":\"1234\"}" -v


				 // skopiowałem to ze zwykłej funkcji /authenticate ale to wymagałoby poprawy, getToken zwraca już token który jest ustawiany w konstruktorze
				 // ale te ustawienie dotyczy sterego sposobu gdzie if(this.pin.equals(this.validPin))... my teraz chcemy porównywać sha256(pin+salt) 
				 // musisz dopisać drugi konstruktor klasy authenticate ... który zrobi porównanie na sha256(pin+salt) 
				 // reszte poniżej też trzeba zweryfikować, ale chyba jest już ok.				
				
	
//				System.out.println("token is: " + authenticator.getToken().getTokenString() );  // can be changed to serialisation str(token) = "aaaccc"
				// let's open the connection to database trying to save the token to MongoDB
				//Session session = HibernateMongoSessionUtils.getInstance().openSession();
				//Transaction tx = session.beginTransaction();
				//session.save(authenticator.getToken());
	

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ResponseEntity<Object> changePassword(@RequestBody Map<String, Object> json_result) 
	{	         
	   	//Napisz tutaj funkcję ustawienia hasła, czyli przyjmujesz w ciele token, sprawdzasz czy jest ważny, jak tak to masz zmienić userowi hasło
		// funkcja przyjmie {'user_id':rzeczkop, 'token':'jakis valid token z bazy', 'newPin':'4321'}
		// od razu może być za trudne zrobić wszystko ponieważ, trzeba stworzyć nowy obiekt, klasę: User z polem passwordHash i salt
		// 1) każdy user pierwszy raz logujący się do systemu korzysta z pinu 1234 ale po wygenerowaniu tokenu może zmienić hasło na cokolwiek
		// 2) wszystko jest zapisywany do mongodb bazy - możesz podejrzeć jak Token zapisujemy
		// 3) zapis passwordHash ma być zrobiony jak w secureAuthenticate, czyli zapisujesz base64 zmiennej bytes z salt+haslo podane w funkcji
		// 4) salt ma byc generowany dla kazdego usera - może być przez funkcję /changePassword albo w secure authenticate.
		// Jak nie wiadomo jak ugryzc temat to zrob System.out.println("zapisuje usera do bazy") czy coś w tym stylu i pisz dalej, wrócimy do problemów
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<Object> getUser(@RequestBody Map<String, Object> json_result) 
	{	         
	    	System.out.println(json_result);
	    	System.out.println("before querying database");
			List<User> userList =  userRepository.findByUserid(json_result.get("user_id").toString());
			System.out.println("After mongo select and before loop over the result");
			for(User userElem:userList) {
				System.out.println(userElem.toString());
			}
	    	return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<Object> setUser(@RequestBody Map<String, Object> json_result) 
	{	         
	    	System.out.println(json_result);
	    	// TODO before passing these params to constructor, we have to check if they are empty and we have all mandatory fields inside json
			User newUser = new User(
					json_result.get("user_id").toString(), 
					json_result.get("passwd").toString()
					);
			System.out.println("I am after User constructor.");

			userRepository.save(newUser);
	
			
	    	return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//curl --request POST http://localhost:8080/user --header "Content-Type: application/json" -d "{\"user_id\":\"rzeczkop\", \"passwd\":\"1234\"}" -v

}
	



