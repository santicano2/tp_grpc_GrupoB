package com.example.api.dto;

public class UserDTO {
	private int id;
	private String username;
	private String name;
	private String lastname;
	private String phone;
	private String email;
	private String role;
	private boolean active;

	public int getId() { return id; }
    public void setId(int id) { this.id = id; }  

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getLastname() { return lastname; }
	public void setLastname(String lastname) { this.lastname = lastname; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }

	public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

}
