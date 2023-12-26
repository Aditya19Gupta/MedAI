package com.medai.entities;

import jakarta.persistence.*;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private  String name;
	private String age;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	private String role;
	private String breastCancerResult;
	private String diabetesResult;
	private String imageUrl;
	
	
	
	
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDiabetesResult() {
		return diabetesResult;
	}

	public void setDiabetesResult(String diabetesResult) {
		this.diabetesResult = diabetesResult;
	}

	public String getBreastCancerResult() {
		return breastCancerResult;
	}

	public void setBreastCancerResult(String breastCancerResult) {
		this.breastCancerResult = breastCancerResult;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	

	public User(int id, String name, String age, String email) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.email = email;
		
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", age=" + age + ", email=" + email + ", data=" + "]";
	}

	
	

}
