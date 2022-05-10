package com.shopperStore.model.humanResource;

public class Item extends ItemDAO{
	private int id;
	private String name, category, description;
	private double price, size;
	private boolean availability;


	/**
	 * Required Parameters for an Item
	 * @param name - name of item
	 * @param category - category item belongs in
	 * @param price - price of item
	 * @param size - size of item in kg
	 */
	public Item(int id, String name, String category, double price, double size, String description) {
		this.id = id;
		this.name=name;
		this.category = category;
		this.price = price;
		this.size = size;
		this.description = description;
	}

	public int getId() { return this.id;}

	public void setId(int id) {this.id = id;}

	public void setName(String name) {
		this.name=name;
	}

	public String getName() {
		return name;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getDescription() {
		return description;
	}

	public void setSize(double size) {
		this.size = size;
	}
	
	public double getSize() {
		return size;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}
	
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public boolean getAvailability() {
		// TODO Auto-generated method stub
		return this.availability;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		// TODO Auto-generated method stub
		return this.category;
	}

}
