package com.example.bibliostock.controller;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.bibliostock.model.Customer;
import com.example.bibliostock.model.CustomerRepository;
import com.example.bibliostock.model.Manager;
import com.example.bibliostock.model.ManagerRepository;
import com.example.bibliostock.model.User;
import com.example.bibliostock.model.UserRepository;
import com.example.bibliostock.request.LoginRequest;
import com.example.bibliostock.request.ManagerSignUpRequest;
import com.example.bibliostock.request.SignUpRequest;
import com.example.bibliostock.response.MessageResponse;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class LoginController {
	@Autowired
	ManagerRepository managerRepo;
	@Autowired
	CustomerRepository customerRepo;
	@Autowired
	UserRepository userRepo;
	
	//List all users
	@GetMapping("/users")
	public ResponseEntity<Set<User>> getUsers(){
		try {
			Set<User> users = new HashSet<>();
			users.addAll(userRepo.findAll());
			return new ResponseEntity<>(users,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Log in
	@PostMapping("/login/{isManager}")
	public ResponseEntity<?> login(@PathVariable("isManager") int isManager, @RequestBody LoginRequest loginRequest) {
		try {
			
			switch (isManager) {
			case 1: {
				List<Manager> managerAccount = managerRepo.findByUsername(loginRequest.getUserName());
				if(managerAccount.isEmpty()) {
					return new ResponseEntity<>(new MessageResponse("Username not found. Please make sure you have an account."),HttpStatus.UNAUTHORIZED);
				}
				else {
					Manager manager = managerAccount.get(0);
					System.out.println(loginRequest);
					if(manager.getPassword().equals(loginRequest.getPassword()))
					{
						manager.setIsActive(true); //update session
						managerRepo.save(manager);
						
						return new ResponseEntity<>(manager,HttpStatus.OK);
					}
					return new ResponseEntity<>(new MessageResponse("Your password is incorrect."),HttpStatus.UNAUTHORIZED);
					
				}
			}
			case 0: {
				List<Customer> customerAccount = customerRepo.findByUsername(loginRequest.getUserName());
				if(customerAccount.isEmpty()) {
					return new ResponseEntity<>(new MessageResponse("Username not found. Please make sure you have an account."),HttpStatus.UNAUTHORIZED);
				}
				else {
					Customer customer= customerAccount.get(0);
					if(customer.getPassword().equals(loginRequest.getPassword()))
					{
						customer.setIsActive(true); //update session
						customerRepo.save(customer);
						
						return new ResponseEntity<>(customer,HttpStatus.OK);
					}
					return new ResponseEntity<>(new MessageResponse("Your password is incorrect."),HttpStatus.UNAUTHORIZED);
					
				}
				
			}
			default:
				return new ResponseEntity<>(new MessageResponse("Not valid user type."),HttpStatus.FORBIDDEN);

			}

			
		} catch (Exception e) {
			MessageResponse exception = new MessageResponse(e.toString());
			return new ResponseEntity<>(exception,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// end session / logs user out
	@PostMapping("/user/{ID}/logout")
	public ResponseEntity<?> logout(@PathVariable("ID") long ID){
		try {
			Optional<User> userAccount = userRepo.findById(ID);
			if(userAccount.isEmpty()) {
				return new ResponseEntity<>(new MessageResponse("User not found"),HttpStatus.NOT_FOUND);
			}
			else {
				User user = userAccount.get();
				if(user.getIsActive()) {
					user.setIsActive(false);
					userRepo.save(user);
					return new ResponseEntity<>(new MessageResponse("Session ended."),HttpStatus.OK);
				}
				else {
					return new ResponseEntity<>(new MessageResponse("User already logged out."),HttpStatus.UNAUTHORIZED);
				}
			}
		}catch(Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.toString()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//create customer account
	@PostMapping("/signup/customer")
	public ResponseEntity<?> createCustomerAccount(@RequestBody SignUpRequest signUpRequest){
		try {
			if(!customerRepo.findByUsername(signUpRequest.getUserName()).isEmpty())
			{
				return new ResponseEntity<>(new MessageResponse("Username already exists."),HttpStatus.CONFLICT);
			}
			if(!customerRepo.findByEmail(signUpRequest.getEmail()).isEmpty()) {
				return new ResponseEntity<>(new MessageResponse("Email already exists."),HttpStatus.CONFLICT);
			}
			customerRepo.save(new Customer(signUpRequest.getUserName(),
											signUpRequest.getPassword(),
											signUpRequest.getEmail(),
											signUpRequest.getDefaultAddress()
					));
			
			return new ResponseEntity<>(customerRepo.findByUsername(signUpRequest.getUserName()).get(0),
										HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Create manager account - has to be done by an admin
	@PostMapping("/signup/manager") 
	public ResponseEntity<?> createManagerAccount(@RequestBody ManagerSignUpRequest request,
												RedirectAttributes redirectAttributes
			){
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(URI.create("/login/1"));
			LoginRequest loginRequest = request.getLoginRequest();
			SignUpRequest signUpRequest = request.getSignUpRequest();
			List<Manager> managerAccount = managerRepo.findByUsername(loginRequest.getUserName());
			
			if(managerAccount.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			else {
				Boolean activeSession = managerAccount.get(0).getIsActive();
				Boolean isAdmin = managerAccount.get(0).getIsAdmin();
				
				if(activeSession) { 
					if(isAdmin) {
						
						if(!managerRepo.findByUsername(signUpRequest.getUserName()).isEmpty())
						{
							return new ResponseEntity<>(new MessageResponse("Username already exists."),HttpStatus.CONFLICT);
						}
						if(!managerRepo.findByEmail(signUpRequest.getEmail()).isEmpty()) {
							return new ResponseEntity<>(new MessageResponse("Email already exists."),HttpStatus.CONFLICT);
						}
						managerRepo.save(new Manager(signUpRequest.getUserName(),
								signUpRequest.getPassword(),
								signUpRequest.getEmail(),
								request.getIsAdmin()
								));
						return new ResponseEntity<>(managerRepo.findByUsername(signUpRequest.getUserName()).get(0),
								HttpStatus.CREATED);
					} 
					else { // No admin permission
						return new ResponseEntity<>(new MessageResponse("No admin access"),HttpStatus.FORBIDDEN);
					}
					
				}else { //inactive session -> redirect to login
					redirectAttributes.addFlashAttribute("isManager", 1);
				    redirectAttributes.addFlashAttribute("loginRequest", loginRequest);
				    return new ResponseEntity<>(headers,HttpStatus.UNAUTHORIZED);
				    
				}
			}
			
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
