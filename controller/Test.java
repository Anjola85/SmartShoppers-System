package com.shopperStore.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.KeyPair;
import java.sql.*;


public class Test {
    //signup
    // login
    // find nearest store based on customers location
    // find stores based on inputted location(max: 50km)
    // START
    //specify JDBC URL
    static String jdbcURL = "jdbc:mysql://localhost:3306/smart_shoppers_system";
    static String dbUsername = "root";
    static String dbPassword = "SuperFly7";
    static Connection connection = null;
    
   //Generate the pair of keys
     static KeyPair pair; 



    /***
     * Test method
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws Exception {
    	
//    	String cipher = hashPassword("anjola");
//    	System.out.println(cipher);
//    	validatePassword("anjola", cipher);
        String str = "DB message: Duplicate entry 'anjola85@gmail.com' for key 'users.email_UNIQUE'\n";
        if(str.toLowerCase().contains("duplicate"))
            System.out.println("works");

    }
    
    private static String hashPassword(String plainTextPassword){
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}
    
    private static void validatePassword(String plainPassword, String hashedPassword) {
		if (BCrypt.checkpw(plainPassword, hashedPassword))
			System.out.println("The password matches.");
		else
			System.out.println("The password does not match.");
	}



    // DATABASE CALLS
    public static void addUser(String email, String name, String password, String address, String authority) {
        try {
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
//			if(connection != null)
//				System.out.println("Connected to Database");

            String sql = "INSERT INTO users (email, fullname, password, address, authority)"
                    + " VALUES (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, password);
            statement.setString(4, address);
            statement.setString(5, authority);


            int rows = statement.executeUpdate();

            if(rows > 0) {
                System.out.println("NEW USER ADDED SUCCESSFULLY");
            }


            connection.close();

        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    //only updates name, password and address
    public static void updateUserInfo(String email, String name, String password, String address) throws SQLException {
        connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        String sql = "UPDATE users SET name=?, password=?, address=?  WHERE email=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, password);
        statement.setString(3, address);
        statement.setString(4, email);

        int rows = statement.executeUpdate();

        if(rows > 0) {
            System.out.println("NEW USER ADDED SUCCESSFULLY");
        }


        connection.close();
    }

    public static void deleteUser(String email) throws SQLException {
        connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        String sql = "DELETE FROM users WHERE email=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);



        int rows = statement.executeUpdate();

        if(rows > 0) {
            System.out.println("User information deleted successfully\n");
        }

        connection.close();
    }

    public static void getUsers() throws SQLException {
        connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
        String sql = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        while(result.next()) {
            String email, name, password, address, authority;
            email = result.getString(1);
            name = result.getString(2);
            password = result.getString(3);
            address = result.getString(4);
            authority = result.getString(5);
            System.out.printf("%s, %s, %s, %s, %s\n", email, name, password, address, authority);
        }

        System.out.println("Finished fetching user information\n\n");

        connection.close();
    }

    public static int getUserID(String userEmail) {
        try {
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            String sql = "SELECT id FROM users" + " WHERE email = \"" + userEmail + "\" ";
//			Statement statement = connection.createStatement();
            PreparedStatement statement = connection.prepareStatement(sql);

//			statement.setString(1, userEmail);
            ResultSet result = statement.executeQuery(sql);
//
            if(result.next()) {
                return result.getInt(1);
            }

            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

}
