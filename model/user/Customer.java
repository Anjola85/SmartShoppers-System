package com.shopperStore.model.user;

import com.shopperStore.model.humanResource.Item;
import com.shopperStore.model.humanResource.Store;

import java.util.ArrayList;
import java.util.List;


public class Customer extends UserDAO {
	private String name, address;
	private ArrayList<String> savedLocations; // bookmark location
	private ArrayList<Item> shoppingList; // you can only shop under one store at a time, unless after checking out

	public Customer() {
		super.setAuthority("customer");
	}

	/**
	 * @param email - customers email
	 * @param name - customers name
	 * @param password - customers password
	 * @param address - customers location
	 */
	public Customer(String email, String name, String password, String address) {
		this.setEmail(email);
		this.setName(name);
		this.setPassword(password);
		this.setAddress(address);
		setAuthority("customer");
	}

	public Customer(String email, String name, String password, String address, List<String> locations) {
		this(email, name, password, address);

		if(locations != null)
			this.savedLocations.addAll(locations);
	}




	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * return all info concerning customer
	 */
	public String toString() {
		return this.name + " " + super.getEmail() + " " + this.address;
	}

	public String getInformation() {
		return toString();
	}

	@Override
	public boolean deleteAccount(User user) {
		return super.deleteAccount(user);
	}

	// actions relating to store

	/**
	 * Get a lost of bookmarked stores
	 * @return savedLocations
	 */
	public ArrayList<String> getSavedLocation() {
		return savedLocations;
	}

	/**
	 * Ability to bookmark location
	 * @param location - list of locations
	 */
	public void setSavedLocation(ArrayList<String> location) {
		this.savedLocations.addAll(location);
	}

	/**
	 * Ability to be able to add item to shopping list
	 * @param store - store tht is being shopped from
	 * @param item - item that exits in this specific store
	 */
	public void addItemToList(Store store, Item item) {
		// check if store contains the item. contains method check if item is in list of onSale items
		shoppingList.add(item);
	}

	public void removeItemFromList(Item item) {
		this.shoppingList.remove(item);
	}

	/**
	 * Ability to view sorted items from shopping list
	 */
	public List<Item> getShoppingList() {
		// customer might want to sort by specific fields
		// ability to view recommended items list - based on their current shopping
		return this.shoppingList;
	}

	/**
	 * this method should be triggered when customer checks out;
	 * since the list is needed for checkout.
	 * shopping list has to be greater than one for you to check out
	 */
	public void checkout() {
		this.shoppingList = new ArrayList<Item>(); // clear the shopping list
	}

}
