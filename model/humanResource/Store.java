package com.shopperStore.model.humanResource;

import com.shopperStore.model.user.Manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This represents a store in the system
 * @author muhammedadeyemi
 *
 * This class should be able to return items sorted by category
 */
public class Store extends StoreDAO{
	 private int id;
	 private String name, description, location, openingHours, closingHours;
	 private List<Manager> storeManagers;
	 private List<Item> inventory, shoppingList, itemsOnSale; // list of available items contained in inventory

	/**
	 * constructor that takes only strings
	 */
	public Store(String name, String description, String location, String openingHours, String closingHours) {
		this.name = name;
		this.description = description;
		this.location = location;
		this.openingHours = openingHours;
		this.closingHours = closingHours;
	}

	public Store(String name, String description, String location, List<Manager> managers, List<Item> inventory,
				 List<Item> itemsOnSale, String openingHour, String closingHour) {
		this(name, description, location, openingHour, closingHour);
		this.storeManagers.addAll(managers);
		this.inventory.addAll(inventory);
		this.itemsOnSale.addAll(itemsOnSale);
	}


	public boolean addToInventory(Item item) {
		if(this.inventory.contains(item) || item == null)
			return false;
		return inventory.add(item);
	}

	public boolean deleteFromInventory(String name) {
		for(int i = 0; i < inventory.size(); i++) {
			Item curr = inventory.get(i);
			if(curr.getName().equalsIgnoreCase(name)) {
				inventory.remove(curr);
				itemsOnSale.remove(curr);
				return true;
			}
		}
		return false;
	}

	public boolean modifyItemsOnSale(Item item) {
		if(itemsOnSale.contains(item) || item == null)
			return false;
		return itemsOnSale.add(item);
	}

	/**
	 * Item name has to be the same
	 * @param item - searches for item in the store and updates it
	 * @return true
	 */
	public boolean modifyItem(Item item) {
		// update this store with the updated item, then call super to update store
		for(int i = 0; i < inventory.size(); i++) {
			if(item.getName().equalsIgnoreCase(inventory.get(i).getName()))
				inventory.set(i, item); // update item in inventory
		}

		// update item onSale
		if(itemsOnSale.contains(item)) {
			for(int i = 0; i < itemsOnSale.size(); i++) {
				if(itemsOnSale.get(i).getName().equalsIgnoreCase(item.getName())) {
					itemsOnSale.set(i, item);
				}
			}
		}

		return super.update(this) != null;
	}

	public List<Item> getByCategory(String category) {

		List<Item> out = new ArrayList<>();

		for(int i = 0; i < itemsOnSale.size(); i++) {
			Item curr = itemsOnSale.get(i);
			if(category.equalsIgnoreCase(curr.getCategory()))
				out.add(curr);
		}

		return out.size() > 0 ? out : null;
	}

	public Item getItem(String name) {
		for (Item curr : itemsOnSale) {
			if (curr.getName().equalsIgnoreCase(name))
				return curr;
		}
		return null;
	}

	/**
	 * @return sorted version of the list
	 */
	public List<Item> getShoppingList() {
		Comparator<Item> sortByName = (o1, o2) -> o1.getName().compareTo(o2.getName());
		this.shoppingList.sort(sortByName);
		return this.shoppingList;
	}

	public boolean setShoppingList(List<Item> list) {
		// validate all items
		for (Item curr : list) {
			if(!this.itemsOnSale.contains(curr))
				return false;
		}
		return shoppingList.addAll(list);
	}


	// getters and setters

	public int getId() {
		return this.id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}



	public String getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(String openingHours) {
		this.openingHours = openingHours;
	}

	public String getClosingHours() {
		return closingHours;
	}

	public void setClosingHours(String closingHours) {
		this.closingHours = closingHours;
	}

	public void setStoreManagers(List<Manager> managers) {
		storeManagers.addAll(managers);
	}

	public List<Manager> getStoreManagers() {
		return this.storeManagers;
	}

	public void setInventory(List<Item> inventory) {
		this.inventory.addAll(inventory);
	}

	public List<Item> getInventory() {
		return this.inventory;
	}

	public void setItemsOnSale(List<Item> itemsOnSale) {
		this.itemsOnSale.addAll(itemsOnSale);
	}

	public List<Item> getItemsOnSale() {
		return this.itemsOnSale;
	}
}
