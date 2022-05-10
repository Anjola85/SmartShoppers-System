package com.shopperStore.model.humanResource;

import com.shopperStore.model.user.Manager;
import com.shopperStore.model.user.User;
import com.shopperStore.model.user.UserDAO;
import org.jetbrains.annotations.NotNull;

/**
 * This is the human resource class
 * @author muhammedadeyemi
 *
 * This class is concerned with the ability to add/manage users of the system
 * it also contains permissions regarding the shoppers system
 * 
 * 
 * 
 * 
 * The system along with users with the highest privilege (administrators) can add and remove managers within the system. 
 * Furthemore, the system can grant privileges to managers,
 * to access and update other resources within the system as long as they have granted permission.
 * This feature is considered High Priority.
 * 
 * 
 * If administrators add a manager into the system, the system would create an account,
 *  grant that account with the access privilege for item and store management. 
 *  Then the system would display the identifier and temporary password to the administrators.
 *  
 *  If administrators remove a manager from the system, 
 *  the system can either revoke the privilege from the manager’s account or delete the manager’s account from the system.
 */

public class HRSystem extends UserDAO {
	
	/** 
	Req 1: The system as well as the administrators must be able to add, remove and update managers for a specific store.
	 */
	public Manager addManager(Manager manager) {
		return (Manager) super.create(manager);
	}

	/**
	 * Return manager to be able to edit and initiate update to the object
	 * @param email
	 * @return
	 */
	public Manager getManager(String email) {
//		User user = super.get(id);
//		if(!(user instanceof Manager)) // if id passed in is not a manager
//			return null;
//		return (Manager) super.get(id);
		return null;
	}
	
	public boolean removeManager(@NotNull Manager manager) {
		return super.delete(manager.getId());
	}

	public Manager updateManager(Manager manager, int managerId) {
		return null;
	}

	/**
	 * Must have same ID to be able to update
	 * @param manager
	 * @param
	 * @return
	 */
	public Manager updateManager(@NotNull Manager manager) {
		User user = super.get(manager.getId());
		if(!(user instanceof Manager)) // if id passed in is not a manager
			return null;
		return (Manager) super.update(manager);
	}
	
	public boolean assignStore(@NotNull Manager manager, Store store) {
		manager.setAssignedStore(store);// assign new store to the manager
		return true;
	}

	/**
	 * return the id of the User
	 * @param payload - data
	 * @return
	 */
	public int getId(@NotNull User payload) {
		return payload.getId();
	}
}
