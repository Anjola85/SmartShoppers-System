package com.shopperStore._core;

/**
 * This class communicates with the database to get required data
 * @author muhammedadeyemi
 *
 */
public interface AppModel<T>{

	/**
	 * @param <T> - generic type
	 * @param payload - data
	 * @return id
	 */
	public int getId(T payload);
	
	/**
	 *
	 * createdAt cannot be set because it is set automatically in the DB
	 * @param id
	 * @return
	 */

	/**
	 * Be able to return the date particular object was created/passed in DB
	 * @param id - for the object
	 * @return createdAt in string
	 */
	public String getCreatedAt();

	
	/**
	 * Gets the last time the object was updated 
	 * @param id
	 * @return String
	 */
	public String getUpdatedAt();
}
