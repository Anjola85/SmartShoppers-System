package com.shopperStore.model.user;

import com.shopperStore.model.humanResource.Item;
import com.shopperStore.model.humanResource.Store;
import com.shopperStore.model.humanResource.StoreDAO;
import com.shopperStore.model.humanResource.StorePermissions;


/**
 * @author muhammedadeyemi
 * Managers cannot be created manually.
 * Admin/system are the only ones able to add/remove managers
 * Managers have have crud permission for items regarding stores
 */
public class Manager extends UserDAO implements StorePermissions {
	private Store assignedStore; // store assigned to manger
	private String email, password;

	public Manager(String email, String password, Store assignedStore) {
		this.email = email;
		this.password = password;
		this.assignedStore = assignedStore;
	}


	public void setEmail(String email) { this.email = email; }
	public String getEmail() {return this.email;}

	public void setPassword(String password) {this.password = password;}
	public String getPassword() {return this.password;}

	public Store getAssignedStore() {
		return assignedStore;
	}

	public void setAssignedStore(Store assignedStore) {
		this.assignedStore = assignedStore;
	}


	// store permissions
	/**
	 * @param store
	 * @param item
	 * @return
	 */
	public boolean addItemToStore(Store store, Item item) {
		store = assignedStore;
		store.addToInventory(item);
		StoreDAO storeDB = new StoreDAO();
		return storeDB.update(store) != null; // overriding store in DB
	}

	// remove
	public boolean removeItemFromStore(Store store, Item item) {
		store = assignedStore;
		store.addToInventory(item);
		StoreDAO storeDB = new StoreDAO();
		return storeDB.update(store) != null; // overriding store in DB
	}

	/**
	 * If item in inventory is modified -> must reflect in on sale
	 * @param store - store that contains item
	 * @param item - updated item
	 * @return
	 */
	public boolean modifyStoreItem(Store store, Item item) {
		store = assignedStore;
		store.modifyItem(item);
		StoreDAO storeDB = new StoreDAO();
		return storeDB.update(store) != null; // overriding store in DBl;
	}

	/**
	 * modify items for a particular store because it might not apply for all the stores
	 * @param store
	 * @param item
	 * @return
	 */
	public boolean modifyOnSaleStoreItem(Store store, Item item) {
		store = assignedStore;
		store.modifyItemsOnSale(item);
		StoreDAO storeDB = new StoreDAO();
		return storeDB.update(store) != null; // overriding store in DBl;
	}

	/**
	 * @param payload - user object
	 * @return
	 */
	@Override
	public User update(User payload) {
		return super.update(payload);
	}
}
