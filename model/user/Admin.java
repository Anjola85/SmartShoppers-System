package com.shopperStore.model.user;

import com.shopperStore.model.humanResource.Item;
import com.shopperStore.model.humanResource.Store;
import com.shopperStore.model.humanResource.StoreDAO;
import com.shopperStore.model.humanResource.StorePermissions;

import java.security.SecureRandom;
import java.util.Random;

public class Admin extends UserDAO implements StorePermissions {

    public Admin(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    public Manager addManager() {
        String email = getSaltString() + "@gmail.com";
        String password = generateRandomPassword(6);
        Manager newManager = new Manager(email, password, null);
        return (Manager) super.create(newManager);
    }

    public Manager assignManager(Store s, Manager m) {
        m.setAssignedStore(s);
        return (Manager) super.update(m); // update manager in DB
    }

    private String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }

    // Method to generate a random alphanumeric password of a specific length
    private String generateRandomPassword(int len)
    {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }


    public boolean removeManager(Manager manager) {
        return super.deleteAccount(manager);
    }

    // store permissions
    /**
     * @param store
     * @param item
     * @return
     */
    public boolean addItemToStore(Store store, Item item) {
        store.addToInventory(item);
        StoreDAO storeDB = new StoreDAO();
        return storeDB.update(store) != null; // overriding store in DB
    }

    // remove
    public boolean removeItemFromStore(Store store, Item item) {
        store.addToInventory(item);
        StoreDAO storeDB = new StoreDAO();
        return storeDB.update(store) != null; // overriding store in DB
    }

    /**
     * If item in inventory is modified -> must reflect in on sale
     * @param store - store that contains item
     * @param item - updated item
     * @return true
     */
    public boolean modifyStoreItem(Store store, Item item) {
        store.modifyItem(item);
        StoreDAO storeDB = new StoreDAO();
        return storeDB.update(store) != null; // overriding store in DBl;
    }

    /**
     * modify items for a particular store because it might not apply for all the stores
     * @param store
     * @param item
     * @return - true
     */
    public boolean modifyOnSaleStoreItem(Store store, Item item) {
        store.modifyItemsOnSale(item);
        StoreDAO storeDB = new StoreDAO();
        return storeDB.update(store) != null; // overriding store in DBl;
    }
}
