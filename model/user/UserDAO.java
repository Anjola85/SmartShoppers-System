package com.shopperStore.model.user;

import com.shopperStore._core.AppDAO;
import com.shopperStore._core.AppModel;
import com.shopperStore.model.humanResource.Store;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Communicates with the database for database operations
 * this class also has the default. Necessary methods all the user based classes must have
 * @author muhammedadeyemi
 */
public class UserDAO extends User implements AppDAO<User>, AppModel<User>{
	// DATABASE config information
	private static final String db = "smart_shoppers_system";
	private static final String jdbcURL = "jdbc:mysql://localhost:3306/" + db;
	private static final String dbUsername = "root"; // this is where to put database username
	private static final String dbPassword = "SuperFly7"; // this is where to put database password


	// instance variables
	private int id;
	private String createdAt, updatedAt;
	private String email, password;
	private String errMessage, successMessage;

	/**
	 * @param payload - data being transferred to DB
	 * @return User object
	 */
	public User create(User payload) {

		try {
			if(payload.getEmail() == null || payload.getEmail().length() < 1 || payload.getPassword() == null || payload.getPassword().length() < 1)
				return null;

			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql;
		    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    Date date = new Date();
		    String currentDate = formatter.format(date); // for createdAt and updatedAt
			this.createdAt = currentDate; // setting instance variables

			// check if email exists


		    // creates User based on the role of the user that is passed
			if(payload instanceof Customer) {
				Customer newCustomer = (Customer) payload;
				sql = "INSERT INTO users (email, name, password, address, authority, createdAt, updatedAt)"
						+ " VALUES (?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement statement = connect.prepareStatement(sql);

				statement.setString(1, newCustomer.getEmail()); // email
				statement.setString(2, newCustomer.getName()); // name
				statement.setString(3, hashPassword(newCustomer.getPassword())); // password hashed
				statement.setString(4, newCustomer.getAddress()); // 5 - address
				statement.setString(5, newCustomer.getAuthority()); // 6 - authority
				statement.setString(6, currentDate); //created_at - 8
				statement.setString(7, currentDate); //updated_at - 9

				if(statement.executeUpdate() > 0) {
					System.out.println("successfully logged in!!! check Database");
					return newCustomer;
				} else {
					return null;
				}
			}
			else if(payload instanceof Manager) {
				Manager newManager = (Manager) payload;

			}
			else if(payload instanceof Admin) {
				Admin newAdmin = (Admin) payload;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			this.errMessage = e.getMessage();
			System.out.println("Dev: Unable to create User\nDB message: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Hash Password failed for User");
			e.printStackTrace();
		}

		return null;
	}



	/**
	 * This gets and sets all the attributes of the specified user
	 * @param userID is the users id
	 * @return User object.  <=== TODO
	 */
	public User get(int userID) {
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT id FROM users" + " WHERE id = \"" + userID + "\" ";
			PreparedStatement statement = connect.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);

			if(result.next()) {
				String role = result.getString("authority");
				if(role.equalsIgnoreCase("customer")) {
					// password gotten back is still hashed
					List<String> savedLocations = (List<String>) deSerializeObjectFromDB("savedLocations", result.getInt("id"));
					System.out.println("reachable");

					return new Customer(result.getString("email"), result.getString("name"),
					result.getString("password"), result.getString("address"), savedLocations); // return customer object
				}
				else if(role.equalsIgnoreCase("manager")) {
					return null;
				} else if(role.equalsIgnoreCase("admin")) {
					return null;
				}
			}

			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public User get(String email) {
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT * FROM users" + " WHERE email = \"" + email + "\" ";
			PreparedStatement statement = connect.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			User user = null;
			
			if(result.next()) {
				String role = result.getString("authority");
				if(role.equalsIgnoreCase("customer")) {
					// password gotten back is still hashed
					List<String> savedLocations = (List<String>) deSerializeObjectFromDB("savedLocations", result.getInt("id"));

					System.out.println("returning customer in get User method");
					user = new Customer(result.getString("email"), result.getString("name"), result.getString("password"), result.getString("address"), savedLocations); // return customer object
					user.setAuthority("customer");
				}
				else if(role.equalsIgnoreCase("manager")) {
					Store assignedStore = (Store) deSerializeObjectFromDB("assignedStore", result.getInt("id"));
					user = new Manager(result.getString("email"), result.getString("password"), assignedStore);
					user.setAuthority("manager");
				} else if(role.equalsIgnoreCase("admin")) {
					user = new Admin(result.getString("email"), result.getString("password"));
					user.setAuthority("admin");
				}
				return user;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Provided the email of the user, it returns the ID for the specified user
	 * @param user - User Object
	 * @return id for user object
	 */
	public int getUserID(User user) {
		try {
			String userEmail = user.getEmail();
			Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT id FROM users WHERE email='" + user.getEmail() + "'";
			PreparedStatement statement = connect.prepareStatement(sql);
			statement.setString(1, userEmail);
			ResultSet result = statement.executeQuery(sql);

			if(result.next())
				return result.getInt(1);

			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * TODO: problem with returning User object
	 * Id is specifying what user you want to update
	 * payload is the new data that wants to override old
	 */
	public User update(User payload) { //payload contains new data, that wants to be updated, but how do i store it??? its an object
		User out = null;
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql;
			PreparedStatement statement = null;
			ResultSet rs = null;
			if (payload instanceof Customer) {
				Customer customer = (Customer) payload;
				sql = "UPDATE users SET name=?, password=?, address=?, savedLocation=?, shoppingList=? WHERE id=?";
				statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, customer.getName());
				statement.setString(2, customer.getPassword());
				statement.setString(3, customer.getAddress());
				statement.setObject(4, customer.getSavedLocation());
				statement.setObject(5, customer.getShoppingList());
				statement.setInt(6, payload.getId());
				statement.executeUpdate();
				out = customer;
			} else if (payload instanceof Manager) {
				Manager manager = (Manager) payload;
				sql = "UPDATE users SET password=?";
				statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, manager.getPassword());
				statement.executeUpdate();
				out = manager;
			} else if (payload instanceof Admin) {
				Admin admin = (Admin) payload;
				sql = "UPDATE users SET password=?";
				statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, admin.getPassword());
				statement.executeUpdate();
				out = admin;
			}

			assert statement != null;
			rs = statement.getGeneratedKeys();
			int userID = -1;
			assert rs != null;
			if (rs.next()) {
				userID = rs.getInt(1);
				if (id != -1) setUpdatedAt(id); // update time in DB
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return out;
	}

	/**
	 * Delete by ID
	 */
	public boolean delete(int id) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "DELETE from users WHERE id=?";
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

	@Override
	public int getId(User payload) {
		return payload.getId();
	}

	/**
	 * get created at for the passed in ID
	 * @return - date the object was created
	 */
	public String getCreatedAt() {
		return createdAt;
//		try {
//			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
//			String sql = "SELECT createdAt FROM users WHERE id=?";
//			PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setInt(1, id);
//			ResultSet result = statement.executeQuery(sql);
//			return result.next() ? result.getString(9) : null;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	public void setUpdatedAt(int id) {
		try {
			// get current date and time & format it
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String currentDate = formatter.format(date); // for createdAt and updatedAt

			// establish connection to the database
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "UPDATE users SET updated_at=? WHERE id=?";
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

	public String getUpdatedAt() {
		return updatedAt;
//		try {
//			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
//			String sql = "SELECT updatedAt FROM users WHERE id=?";
//			PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setInt(1, id);
//			ResultSet result = statement.executeQuery(sql);
//			return result.next() ? result.getString(10) : null;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	/**
	 * This method updates the accountIdentifier in the DB
	 * @param email - to get specific user information since it is unique
	 * @param password - to validate user
	 */
	@Override
	public void changeAccountIdentifier(String prevEmail, String prevPassword, String email, String password) {
		try {
			// get current date and time & format it
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			String currentDate = formatter.format(date);

			// establish connection to the database
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "UPDATE users SET email=?, password=? WHERE email=? AND password=?";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, email); // new email
			statement.setString(2, password); // new password
			statement.setString(3, prevEmail); // validates the user
			statement.setString(4, prevPassword); // before updating

			if(statement.executeUpdate() > 0) { // if update successful update class attributes
				setEmail(email);
				setPassword(password);

				ResultSet rs = statement.getGeneratedKeys(); // get id from DB
				int objectID = rs.next() ? rs.getInt(1) : -1;
				if(objectID != -1)
					setUpdatedAt(objectID); // update updatedAt
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getInformation() {
		return this.toString();
	}

	/**
	 * TODO
	 * 4.4.1 - Req 3: Customers must be able to change the store’s location at any time during their visit to the online service of the system.
	 * store's location being the current location that the number of stores belong to.
	 * This method should get a particular location and return stores belonging to it
	 * @param preferredLocation - DEFAULT should be users location/address
	 * @return all stores in that location/city
	 */
	@Override
	public List<Store> changeStoreLocation(String preferredLocation) { // TODO: HERE
		return null;
	}

	/**
	 * Uses the account identifier(unique) to delete the account
	 * @param user - Object
	 * @return - true if account was successfully deleted
	 */
	@Override
	public boolean deleteAccount(User user) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "DELETE from users WHERE email=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getEmail()); // email cannot be duplicated
			int rows = statement.executeUpdate();
			return rows > 0; // returns true of operation was successful
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getEmail() {
		return super.getEmail();
	}

	@Override
	public void setEmail(String email) {
		// TODO Auto-generated method stub
		super.setEmail(email);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}

	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		super.setPassword(password);
	}



	/**
	 * Returns the id because it extends the User class
	 * although i never set id manually so should i should get the ID from the DB???
	 */
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	/**
	 * Gets the id from the DB
	 * @param email - unique identifiers for users
	 * @return the user's id
	 */
	public int getId(String email) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT id from users WHERE email=?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email); // email is the identifier - unique
			ResultSet result = statement.executeQuery(sql);
			if(result.next()) {
				this.id = result.getInt(1); // 1 becuase the id is stored in the first column in the DB
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Validate user
	 * @param email - unique identifier
	 * @param password - to validate user
	 * @return true or false
	 */
	public boolean validateUser(String email, String password) {
		try {
			Connection connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String sql = "SELECT password FROM users WHERE email='" + email + "'";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			// get password and compare
			if(result.next()) {
				if(validatePassword(password, result.getString("password")))
					return true;
				System.out.println("Tried to validate but couldn't validate password");
			}
			System.out.println("Validation failed");
			result.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// handling instance variables
	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String message) {
		this.errMessage = message;
	}


	// HELPER METHODS


	private static String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	private boolean  validatePassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

	/**
	 * Serializes object and passes in into the database
	 * @param objectName - this is the name of the field in the DB
	 * @param objectToSerialize - this is the object that wants to be serialized
	 * @return the id for the serialized data
	 */
	private int serializeObjectToDB(String objectName, Object objectToSerialize) {
		String SQL_SERIALIZE_OBJECT = "INSERT INTO " + db + "(" + objectName + ") VALUES (?)";
		int id = -1; // default id is -1 if not found
		Connection connect;
		try {
			connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			PreparedStatement pstmt = connect.prepareStatement(SQL_SERIALIZE_OBJECT, Statement.RETURN_GENERATED_KEYS);
			pstmt.setObject(1, objectToSerialize); // pass the object into field of type blob in the DB that automatically serializes it
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next())
				id = rs.getInt(1); // gets the id of the serialized object in the database
			else
				System.out.println("Result set is null for serializeObjectToDB method");

			rs.close();
			pstmt.close();
//			System.out.println("Java object serialized to database. Object: " + objectToSerialize);
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * De-serializes and returns the specified object
	 * @param object_name - name of the field in the database
	 * @param id - for the serialized object that wants to be returned
	 * @return the de-serialized object
	 */
	private Object deSerializeObjectFromDB(String object_name, int id) {
		Connection connect;
		Object deSerializedObject = null;
		try {
			connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
			String SQL_DESERIALIZE_OBJECT = "SELECT " + object_name + " FROM " + "users WHERE id = ?";
			PreparedStatement pstmt = connect.prepareStatement(SQL_DESERIALIZE_OBJECT);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if(rs.next()) {
				byte[] buf = rs.getBytes(1); // retrieves data in bytes from DB
				ObjectInputStream objectIn = null;

				if(buf != null)
					objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
				else {
					System.out.println("Unable to retrieve serialized object from database. buf is empty -> returning null");
					return null;
				}

				deSerializedObject = objectIn.readObject(); // de-serialize received object

			}
			else
				System.out.println("Result set is empty for deserialization method");

			rs.close();
			pstmt.close();

		} catch (SQLException | IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("message: " + e.getMessage());
			e.printStackTrace();
		}
		return deSerializedObject;
	}

}
