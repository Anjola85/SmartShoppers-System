package com.shopperStore.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SerializeToDatabase {

	/**
	 * @variable serializeDB - name of the database
	 * @variable object_name - this is the name of the field in the DB - this is of type string specifying the name of the serialized object
	 * @variable serialized_object - this is also a field of type blob - A BLOB is a binary large object that can hold a variable amount of data
	 */
	private static final String SQL_SERIALIZE_OBJECT = "INSERT INTO serialized_java_objects(object_name, serialized_object) VALUES (?, ?)";
	private static final String SQL_DESERIALIZE_OBJECT = "SELECT serialized_object FROM serialized_java_objects WHERE serialized_id = ?";

	/**
	 * 
	 * @param connection - this is used to connect to the database
	 * @param objectToSerialize - this is the object to be serialized into the databse
	 * @return the serialized id
	 * @throws SQLException - if action is unsuccessful
	 */
	public static int serializeJavaObjectToDB(Connection connection, Object objectToSerialize) throws SQLException {

		PreparedStatement pstmt = connection.prepareStatement(SQL_SERIALIZE_OBJECT, Statement.RETURN_GENERATED_KEYS);
/**
 * int count = stmt.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = stmt.getGeneratedKeys();
 */
		pstmt.setString(1, objectToSerialize.getClass().getName());
		pstmt.setObject(2, objectToSerialize);
		pstmt.executeUpdate();
		ResultSet rs = pstmt.getGeneratedKeys();
		int serialized_id = -1; // default value is -1 meaning not found
		
		if (rs.next()) 
			serialized_id = rs.getInt(1); // gets the id of the serialized object in the database
		else
			System.out.println("rs is empty");
		
		rs.close();
		pstmt.close();
		System.out.println("Java object serialized to database. Object: " + objectToSerialize);
		
		if(serialized_id == -1)
			System.out.println("unable to get serialized id");
		
		return serialized_id;
	}

	/**
	 * 
	 * @param connection - to establish connection to the database
	 * @param serialized_id - id of the serialized object
	 * @return object - the de-serialized object 
	 * @throws SQLException 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerializeJavaObjectFromDB(Connection connection, int serialized_id) throws SQLException, IOException, ClassNotFoundException {
//		private static final String SQL_DESERIALIZE_OBJECT = "SELECT serialized_object FROM serialized_java_objects WHERE serialized_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(SQL_DESERIALIZE_OBJECT); 
		pstmt.setInt(1, serialized_id);
		ResultSet rs = pstmt.executeQuery();
		Object deSerializedObject = null;

		if(rs.next()) {
			// Object object = rs.getObject(1);
//			System.out.println("debugging here: " + rs.getInt("serialized_id"));
			byte[] buf = rs.getBytes(1); // retrieves the byte from the DB
			ObjectInputStream objectIn = null;
			
			if (buf != null)
				objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
			else
				System.out.println("buf is empty");

			deSerializedObject = objectIn.readObject(); // de-serialize retrieved object
			System.out.println("Java object de-serialized from database. Object: "
					+ deSerializedObject + " Classname: "
					+ deSerializedObject.getClass().getName());
		} else {
			System.out.println("result set is empty");
		}

		rs.close();
		pstmt.close();

		return deSerializedObject;
	}

	/**
	 * Serialization and de-serialization of java object from mysql
	 *
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String args[]) throws ClassNotFoundException,
			SQLException, IOException {
		Connection connection = null;

		String db = "javaserialization";
		String url = "jdbc:mysql://localhost:3306/" + db ;
		String username = "root";
		String password = "SuperFly7";
		connection = DriverManager.getConnection(url, username, password);

		// a sample java object to serialize
		Vector obj = new Vector();
		obj.add("java");
		obj.add("papers");

		// serializing java object to mysql database
		int serialized_id = serializeJavaObjectToDB(connection, obj);
		System.out.println("serialized id: " + serialized_id);


		// de-serializing java object from mysql database
		Vector objFromDatabase = (Vector) deSerializeJavaObjectFromDB(connection, serialized_id);
//		System.out.println(objFromDatabase.get(0));

		connection.close();
	}
}
