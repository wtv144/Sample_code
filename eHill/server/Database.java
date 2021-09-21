package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class Database {
	
	private final DataSource dataSource;
	
	Database(DataSource dataSource){
		this.dataSource =  dataSource;
	}
	
	void initialize() throws SQLException{

		String createItems = "CREATE TABLE items (" + 
					"id INTEGER NOT NULL " + 
						"PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " + 
					"name VARCHAR(255), " + 
					"price DOUBLE NOT NULL, " + 
					"buyoutprice DOUBLE NOT NULL, " + 
					"issold BOOLEAN, "+
					"endtime BIGINT NOT NULL,"+
					"description LONG VARCHAR, " +
					"minprice DOUBLE NOT NULL, " +
					"imglink LONG VARCHAR,"+
					"bidhist LONG VARCHAR" +
				")";
		
		String createClients = "CREATE TABLE Clients (" + 
					"username VARCHAR(255), " + 
					"password INT" +
					")";
		
		try (
				Connection connection = dataSource.getConnection();
				Statement createItemsStatement = connection.createStatement();
				Statement createClientsStatement = connection.createStatement();
		) {
			connection.setAutoCommit(false); //if two statements are created, both must be successfully created otherwise it will roll back
			//System.out.println("will make items");
			createItemsStatement.executeUpdate(createItems);
			createClientsStatement.executeUpdate(createClients);
			//System.out.println("client table made");
			connection.commit();
			
		}
	}
	
	int insertItem(Item item) throws SQLException{
		String insertItem = "INSERT INTO items (name, price, buyoutprice, issold, endtime, description, imglink, minprice, bidhist) " + 
				"VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";
		try(
			Connection connection = dataSource.getConnection();
			PreparedStatement insertItemStatement = connection.prepareStatement(insertItem, Statement.RETURN_GENERATED_KEYS);
		) {
			insertItemStatement.setString(1, item.getName());
			insertItemStatement.setDouble(2, item.getPrice());
			insertItemStatement.setDouble(3, item.getbo());
			insertItemStatement.setBoolean(4, item.isSold());
			insertItemStatement.setLong(5, item.getendtime());
			insertItemStatement.setString(6, item.getDescription());
			insertItemStatement.setString(7, item.getimglink());
			insertItemStatement.setDouble(8, item.getMinp());
			insertItemStatement.setString(9, item.getbhString());
			insertItemStatement.executeUpdate();
			System.out.println("inserted " + item.getName());
			ResultSet generatedKeys = insertItemStatement.getGeneratedKeys();
			generatedKeys.next();
			int id = generatedKeys.getInt(1);
			insertItemStatement.close();
			generatedKeys.close();
			return id;
			
		  }	
	}
	
	void updateItem(Item item) throws SQLException {
		String updateItem = "UPDATE items " + 
				"SET name = ?, price = ?, buyoutprice = ?, issold= ?, endtime = ?, description = ?, imglink = ?, minprice = ?, bidhist = ?" +
				"WHERE id = ?";
		//(name, price, buyouprice, issold, endtime, description, imglink, minprice, bidhist)
		try(
			Connection connection = dataSource.getConnection();
			PreparedStatement updateItemStatement = connection.prepareStatement(updateItem);
		) {
			updateItemStatement.setString(1, item.getName());
			updateItemStatement.setDouble(2, item.getPrice());
			updateItemStatement.setDouble(3, item.getbo());
			updateItemStatement.setBoolean(4, item.isSold());
			updateItemStatement.setLong(5, item.getendtime());
			updateItemStatement.setString(6, item.getDescription());
			updateItemStatement.setString(7, item.getimglink());
			updateItemStatement.setDouble(8, item.getMinp());
			updateItemStatement.setString(9, item.getbhString());
			updateItemStatement.setInt(10, item.getId());

			updateItemStatement.executeUpdate();
			
		}
	}
	
	
	Item getItem(int id) throws SQLException{
		String selectItem = "SELECT * FROM items " + 
				"WHERE id = ?";
		
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement selectItemStatement = connection.prepareStatement(selectItem);
			) {
				selectItemStatement.setInt(1, id);
				ResultSet results = selectItemStatement.executeQuery();
				
				return readItemResultSet(results).get(0); 
			}
	}
	
	private static ArrayList<Item> readItemResultSet(ResultSet results) throws SQLException{
		ArrayList<Item> items = new ArrayList<>(); //just in case we ever filter out multiple items
		//(name, price, buyoutprice, issold, endtime, description, imglink, minprice, bidhist)
		while(results.next()) {
			int newId = results.getInt("id");
			String name = results.getString("name");
			double price = results.getDouble("price");
			double buyoutprice = results.getDouble("buyoutprice");

			boolean isSold = results.getBoolean("issold");

			long endtime = results.getLong("endtime");

			String description = results.getString("description");
			String imlink = results.getString("imglink");
			double	minPrice = results.getDouble("minprice");
			String bh = results.getString("bidhist");
			System.out.println("bid hist is" +bh);
//			String highestBidder = results.getString("highestBidder");
			
			items.add(new Item(name, newId, price, buyoutprice, isSold, endtime, description, imlink, minPrice, bh));
		}
		
		return items;
				
	}
	
	ArrayList<Item> getAllItems() throws SQLException{
		String selectItems = "SELECT * FROM items";
		
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement selectItemStatement = connection.prepareStatement(selectItems);
			) {
				
				ResultSet results = selectItemStatement.executeQuery();
				
				return readItemResultSet(results); 
			}
		
	}
	
	
	

	void insertClient(Client Client) throws SQLException{
		String insertClient = "INSERT INTO Clients (username, password) " + 
				"VALUES (?, ?)";
		
			Connection connection = dataSource.getConnection();
			PreparedStatement insertClientStatement = connection.prepareStatement(insertClient);
		 
			insertClientStatement.setString(1, Client.getUsername());
			insertClientStatement.setInt(2, Client.getPassword());
		
			insertClientStatement.executeUpdate();
			System.out.println("inserted Client");
			insertClientStatement.close();
			
			
		  
	
	}
	
	boolean searchClient(String username, int password) throws SQLException{
		String selectClient = "SELECT * FROM Clients " + 
				"WHERE Clientname = ?";
		
		
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement selectClientStatement = connection.prepareStatement(selectClient);
			) {
				//byte[] salt = getSalt();
				selectClientStatement.setString(1, username);
				//selectClientStatement.setString(2, getHashPassword(password, salt));
				ResultSet results = selectClientStatement.executeQuery();
				while(results.next()) {
					int pass = results.getInt("password");

					if(pass == password ) {
						return true;
					}
				}
				return false;
				
				 
			}
		
	}
	
	ArrayList<Client> getAllClients() throws SQLException{
		String selectClients = "SELECT * FROM Clients";
		
		try(
				Connection connection = dataSource.getConnection();
				PreparedStatement selectClientStatement = connection.prepareStatement(selectClients);
			) {
				
				ResultSet results = selectClientStatement.executeQuery();
				
				return readClientResultSet(results); 
			}
	}
		
	
	
	
	private static ArrayList<Client> readClientResultSet(ResultSet results) throws SQLException{
		ArrayList<Client> Clients = new ArrayList<>(); //just in case we ever filter out multiple items
		
		while(results.next()) {
			
			String username = results.getString("username");
			int password = results.getInt("password");
//			System.out.println("dbClientname: " + Clientname);
//			System.out.println("dbPassword: " + password);
//			System.out.println("dbSalt: " + results.getBytes("usalt"));
		
//			boolean isSold = results.getBoolean("isSold");
//			double startPrice = results.getDouble("startPrice");
//			String highestBidder = results.getString("highestBidder");
			
			Clients.add(new Client(username, password));
		}
		
		return Clients;
				
	}
	void clearDatabase() throws SQLException {
		String clearGuests = "DELETE FROM items";
		String clearMessages = "DELETE FROM clients";
		try (
			Connection connection = dataSource.getConnection();
			PreparedStatement clearGuestsStatement = connection.prepareStatement(clearGuests);
			PreparedStatement clearMessagesStatement = connection.prepareStatement(clearMessages);
		) {
			connection.setAutoCommit(false);
			clearMessagesStatement.executeUpdate();
			clearGuestsStatement.executeUpdate();
			connection.commit();
		}
	}
	

}
