package com.example.api.dto;

public class CreateUserResponseDTO {
	private String username;
	private String email;
	private String phone;
	private String plainPassword;

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getPlainPassword() { return plainPassword; }
	public void setPlainPassword(String plainPassword) { this.plainPassword = plainPassword; }
}
