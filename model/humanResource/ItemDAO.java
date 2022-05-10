package com.shopperStore.model.humanResource;

import com.shopperStore._core.AppDAO;
import com.shopperStore._core.AppModel;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDAO implements AppDAO<Item>, AppModel<Item>{
	
	// DATABASE config information
	private static final String db = "smart_shoppers_system";
	private static final String jdbcURL = "jdbc:mysql://localhost:3306/" + db;
	private static final String dbUsername = "root"; // this is where to put database username
	private static final String dbPassword = "SuperFly7"; // this is where to put database password
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


	private String createdAt;
	private String updatedAt;

	/**
	 * Return the id for the specified payload
	 * @param payload - data
	 * @return payload's id
	 */
	public int getId(Item payload) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT id from item WHERE name=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, payload.getName()); // email is the identifier - unique
			ResultSet result = statement.executeQuery(sql);

			if(result.next())
				return result.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Get created at for a particular item/object
	 * @param id - for the object
	 * @return the day the item/object was created
	 */
	public String getCreatedAt(int id) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT createdAt FROM item WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery(sql);
			return result.next() ? result.getString(9) : null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setUpdatedAt(int id) {
		try {
			// get current date and time & format it
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String currentDate = formatter.format(date); // for createdAt and updatedAt

			// establish connection to the database
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "UPDATE item SET updatedAt=? WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, currentDate);
			statement.setInt(2, id);

			int executed = statement.executeUpdate();

			if(!(executed>0))
				System.out.println("DEV error: unable to set updated at in the DB for User");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getUpdatedAt(int id) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT updatedAt FROM item WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			ResultSet result = statement.executeQuery(sql);
			return result.next() ? result.getString(10) : null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Item create(Item payload) {
		// TODO Auto-generated method stub
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql;
		    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date date = new Date();  
		    String currentDate = formatter.format(date);
			this.createdAt = currentDate;
		    // creates User based on the role of the user that is passed
			sql = "INSERT INTO item (name, size, price, description, availability, category, createdAt, updatedAt)"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connect.prepareStatement(sql);
			// id is in position 1
			statement.setString(2, payload.getName());
			statement.setDouble(3, payload.getSize());
			statement.setDouble(4, payload.getPrice());
			statement.setString(5, payload.getDescription());
			int availability = payload.getAvailability() ? 1 : 0;
			statement.setInt(6,  availability);
			statement.setString(7,  payload.getCategory());
			statement.setString(8, currentDate); //created_at - 9
			statement.setString(9, currentDate); //updated_at - 10

			return statement.executeUpdate() > 0 ? payload : null;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Dev: Unable to create Item in SQL\nDB message: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get by @param - id
	 * @return Item
	 */
	public Item get(int id) {
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT id FROM item" + " WHERE id = \"" + id + "\" ";
			PreparedStatement statement = connect.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			connect.close();

			if(result.next()) {
				return new Item(result.getInt("id"), result.getString("name"),
						result.getString("Category"), result.getDouble("price"),
						result.getDouble("size"), result.getString("description"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param name - get by name
	 * @return Item
	 */
	public Item get(String name) {
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT name FROM item" + " WHERE name = \"" + name + "\" ";
			PreparedStatement statement = connect.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			connect.close();

			if(result.next()) {
				return new Item(result.getInt("id"), result.getString("name"),
						result.getString("Category"), result.getDouble("price"),
						result.getDouble("size"), result.getString("description"));
			}

			return null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param payload - data containing updated information
	 * @return updated item
	 */
	public Item update(Item payload) {
		try {
			Date date = new Date();
			String currentDate = formatter.format(date);
			this.updatedAt = currentDate;
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "UPDATE item SET name=?, size=?, price=?, description=?, availability=?, category=?," +
					"updatedAt=?";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, payload.getName());
			statement.setDouble(2, payload.getSize());
			statement.setDouble(3, payload.getPrice());
			statement.setString(4, payload.getDescription());
			statement.setInt(5, payload.getAvailability() ? 1 : 0);
			statement.setString(6, payload.getCategory());
			statement.setString(7, currentDate); // controller must pass in the date the data was updated

			if(statement.executeUpdate() > 0)
				return payload;

		} catch (SQLException e) {
			System.out.println("Message: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public boolean delete(int id) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "DELETE from item WHERE id=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, id); // email cannot be duplicated
			int rows = statement.executeUpdate();
			return rows > 0; // returns true of operation was successful
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public String getUpdatedAt() {
		return formatter.format(this.updatedAt);
	}

	public String getCreatedAt() {
		return formatter.format(this.createdAt);
	}
}
