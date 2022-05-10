package com.shopperStore.model.humanResource;

import com.shopperStore._core.AppDAO;
import com.shopperStore._core.AppModel;
import com.shopperStore.model.user.Manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * /** dealing with items in a store **
 * Ability to add/remove items from store
 *  Ability to modify items -> edited item is passed in as a new item which is overwritten in the DB and stored
 *  ability to modify sale item list - that is, add, delete, modify and update
 */
public class StoreDAO implements AppDAO<Store>, AppModel<Store> {

    // DATABASE CONFIG INFORMATION
    private static final String db = "smart_shoppers_system";
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/" + db;
    private static final String dbUsername = "root"; // this is where to put database username
    private static final String dbPassword = "SuperFly7"; // this is where to put database password

    // INSTANCE VARIABLES
    private int id;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String createdAt, updatedAt;


    /** CRUD OPERATIONS FOR A STORE **/

    private Connection establishDBConnection() {
        try {
            return DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        } catch (SQLException e) {
            System.out.println("Dev message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Store create(Store payload) {
        try {
            this.createdAt = formatter.format(new Date()); // setting instance variables
            this.updatedAt = createdAt;
            Connection connect = establishDBConnection();
            String sql = "INSERT INTO store (name, description, location, managers, inventory, itemsOnSale, openingHour, closingHour, createdAt, updatedAt)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            assert connect != null;
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, payload.getName());
            statement.setString(2, payload.getDescription());
            statement.setString(3, payload.getLocation());
            statement.setObject(4, payload.getStoreManagers());
            statement.setObject(5, payload.getInventory());
            statement.setObject(6, payload.getItemsOnSale());
            statement.setString(7, payload.getOpeningHours());
            statement.setString(8, payload.getClosingHours());
            statement.setString(9, createdAt);
            statement.setString(10, updatedAt);
            return statement.executeUpdate() > 0 ? payload : null;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Dev: Unable to create User\nDB message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Store get(int id) {
        try {
            Connection connect = establishDBConnection();
            String sql = "SELECT id FROM store WHERE id=?";
            assert connect != null;
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery(sql);
            connect.close();

            // return object
            if(rs.next()) {
                this.id = rs.getInt("id");
                // retrieve deserialized objects
                ArrayList<Manager> managers = (ArrayList) deSerializeObjectFromDB("managers", id);
                ArrayList<Item> inventory = (ArrayList) deSerializeObjectFromDB("inventory", id);
                ArrayList<Item> itemsOnSale = (ArrayList) deSerializeObjectFromDB("itemsOnSale", id);
                this.updatedAt = rs.getString("updatedAt");
                return new Store(rs.getString("name"), rs.getString("description"), rs.getString("location"), managers,
                inventory, itemsOnSale, rs.getString("openingHour"), rs.getString("closingHour"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Store get(String name) {
        try {
            Connection connect = establishDBConnection();
            String sql = "SELECT id FROM store WHERE name=?";
            assert connect != null;
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery(sql);
            // return object
            if(rs.next()) {
                this.id = rs.getInt("id");
                // retrieve deserialized objects
                ArrayList<Manager> managers = (ArrayList) deSerializeObjectFromDB("managers", id);
                ArrayList<Item> inventory = (ArrayList) deSerializeObjectFromDB("inventory", id);
                ArrayList<Item> itemsOnSale = (ArrayList) deSerializeObjectFromDB("itemsOnSale", id);
                this.updatedAt = rs.getString("updatedAt");
                return new Store(rs.getString("name"), rs.getString("description"), rs.getString("location"), managers,
                        inventory, itemsOnSale, rs.getString("openingHour"), rs.getString("closingHour"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param location - get based on this location
     * @return - a list of stores
     */
    public List<Store> getStores(String location) {
        Connection connect = establishDBConnection();
        List<Store> out = new ArrayList<>();
        try {
            assert connect != null;
            Statement stmt = connect.createStatement();
            String sql = "SELECT * FROM store WHERE location='" + location + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                // handle managers
                ArrayList<Manager> managers = (ArrayList<Manager>) deSerializeObjectFromDB("managers", rs.getInt("id"));
                // handle inventory
                ArrayList<Item> inventory = (ArrayList<Item>) deSerializeObjectFromDB("inventory", rs.getInt("id"));
                // handle itemsOnSale
                ArrayList<Item> itemsOnSale = (ArrayList<Item>) deSerializeObjectFromDB("itemsOnSale", rs.getInt("id"));

                // populate list output
                out.add(new Store(rs.getString("name"), rs.getString("description"), rs.getString("location"),
                        managers, inventory, itemsOnSale, rs.getString("openingHours"), rs.getString("closingHours")));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * Updating the current store
     * @param payload - store object
     * @return new store object
     */
    @Override
    public Store update(Store payload) {
        Store out = null;
        try {
            this.updatedAt = formatter.format(new Date());
            Connection connection = establishDBConnection();
            String sql = "UPDATE store SET name=?, description=?, location=?, managers=?, inventory=?, itemsOnSale=?, openingHour=?, closingHour=?, updatedAt=? WHERE id=?";
            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, payload.getName());
            statement.setString(2, payload.getDescription());
            statement.setString(3, payload.getLocation());
            statement.setObject(4, payload.getStoreManagers());
            statement.setObject(5, payload.getInventory());
            statement.setObject(6, payload.getItemsOnSale());
            statement.setString(7, payload.getOpeningHours());
            statement.setString(8, payload.getClosingHours());
            statement.setString(9, payload.getUpdatedAt());
            statement.setInt(10, payload.getId());
            out = statement.executeUpdate() > 0 ? payload : null; // set return
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Unable to update store, with dev message: " + e.getMessage());
            e.printStackTrace();
        }

        return out;
    }

    @Override
    public boolean delete(int id) {
        try {
            Connection connection = establishDBConnection();
            String sql = "DELETE from store WHERE id=?";
            assert connection != null;
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


    /** IMPORTANT METHOD FOR ALL OBJECTS IN DB **/
    @Override
    public int getId(Store payload) { return id; }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }





    // HELPER METHODS

    /**
     * De-serializes and returns the specified object
     * @param object_name - name of the field in the database
     * @param id - for the serialized object that wants to be returned
     * @return the de-serialized object
     */


    private Object deSerializeObjectFromDB(String object_name, int id) {
        Object deSerializedObject = null;
        try {
            Connection connect = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            String SQL_DESERIALIZE_OBJECT = "SELECT " + object_name + " FROM " + db + " WHERE id = ?";
            PreparedStatement pstmt = connect.prepareStatement(SQL_DESERIALIZE_OBJECT);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                byte[] buf = rs.getBytes(1); // retrieves data in bytes from DB
                ObjectInputStream objectIn = null;

                if(buf != null)
                    objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
                else
                    System.out.println("Unable to retrieve serialized object from database. buf is empty");

                assert objectIn != null; // object cannot be null
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
