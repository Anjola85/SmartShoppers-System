package com.shopperStore.controller;

import com.shopperStore.model.humanResource.Store;
import com.shopperStore.model.humanResource.StoreDAO;

import java.util.List;

public class StoreController {

    public boolean addStore(Store s) {
        if(s == null)
            return false;

        StoreDAO db = new StoreDAO();
        return db.create(s) != null;
    }

    public boolean removeStore(Store s) {
        if(s == null)
            return false;

        StoreDAO db = new StoreDAO();
        return db.delete(s.getId());
    }

    public boolean updateStore(Store s) {
        if(s == null)
            return false;

        StoreDAO db = new StoreDAO();
        return db.update(s) != null;
    }

    public Store getStore(String name) {
        if(name == null || name.equals(""))
            return null;

        StoreDAO db = new StoreDAO();
        return db.get(name);
    }

    public List<Store> getStoreBasedOnLocation(String location) {
        if(location == null || location.equals(""))
            return null;

        StoreDAO db = new StoreDAO();
        return db.getStores(location);
    }
}
