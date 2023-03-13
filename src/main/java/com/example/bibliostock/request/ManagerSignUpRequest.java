package com.example.bibliostock.request;

public class ManagerSignUpRequest {
	private LoginRequest loginRequest;
	private SignUpRequest signUpRequest;
	private Boolean isAdmin;
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public LoginRequest getLoginRequest() {
		return loginRequest;
	}
	public void setLoginRequest(LoginRequest loginRequest) {
		this.loginRequest = loginRequest;
	}
	public SignUpRequest getSignUpRequest() {
		return signUpRequest;
	}
	public void setSignUpRequest(SignUpRequest signUpRequest) {
		this.signUpRequest = signUpRequest;
	}
	
	
}
