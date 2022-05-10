package com.shopperStore.model.user;

import com.shopperStore.model.humanResource.Store;

import java.util.List;

/**
 * This is User's Abstract Class
 * @author muhammedadeyemi
 * This class is to be inherited by user concrete classes
 * The methods defined in these class applies to all types of Users
 */
public abstract class User {
	private int id;
	private String email, password, authority;
	
	 // all users must have identifier and password
	public void setId(int id) { this.id=id; }
	public int getId() {
		return this.id;
	}

	public  String getEmail() {
		return this.email;
	}

	public  void setEmail(String email) {
		this.email = email;
	}

	public  String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority.toLowerCase();
	}

	/**
	 * User should have ability to change the account identifier which is email and password
	 * @param prevEmail - for validation
	 * @param prevPassword - for validation
	 * @param email - new email
	 * @param password - new password
	 */
	public void changeAccountIdentifier(String prevEmail, String prevPassword, String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Ability for user to get all personal information
	 * @return string representation of all contained information of user
	 */
	public abstract String getInformation();
	
	
	/**
	 * 4.4.1 - Req 3: Customers must be able to change the store’s location at any time during their visit to the online service of the system.
	 * store's location being the current location that the number of stores belong to.
	 * This method should get a particular location and return stores belonging to it
	 * @param preferredLocation - DEFAULT location of cutomer/user but should be a target location where stores are
	 * @return a list of Stores
	 */
	public abstract List<Store> changeStoreLocation(String preferredLocation);
	
	/**
	 * Users should be able to request account deletion
	 * @return true if account has been successfully deleted
	 */
	public abstract boolean deleteAccount(User user);

}
