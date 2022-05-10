package com.shopperStore.model.humanResource;

public interface StorePermissions {
    /**
     * @param store
     * @param item
     * @return
     */
    public boolean addItemToStore(Store store, Item item);

    /**
     * removed the specified item from the store
     * @param store
     * @param item
     * @return
     */
    public boolean removeItemFromStore(Store store, Item item);

    /**
     *
     * @param store - store that contains item
     * @param item - updated item
     * @return true if successfully updated
     */
    public boolean modifyStoreItem(Store store, Item item);

    /**
     * modify items for a particular store because it might not apply for all the stores
     * @param store
     * @param item
     * @return
     */
    public boolean modifyOnSaleStoreItem(Store store, Item item);
}
