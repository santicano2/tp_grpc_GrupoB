package com.example.api.dto;

public class LoginResponseDTO {
	private boolean ok;
	private String message;
	private String username;
	private String name;
	private String lastname;
	private String email;
	private String role;
	private String token;

	public boolean isOk() { return ok; }
	public void setOk(boolean ok) { this.ok = ok; }

	public String getMessage() { return message; }
	public void setMessage(String message) { this.message = message; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getLastname() { return lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }
}
