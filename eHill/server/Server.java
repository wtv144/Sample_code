package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.*;


import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
/*
 * TODO: Make the createaccount/check login synchronized
 * Make the itemlist synchronized
*/
public class Server extends Observable{
	static Server server;
	private static String secretKey = "akd823/mbkfo283k";
	private static String salt = "ee422c&*(&#(*@$&@#$";
	private static HashMap<String,Client> clients = new HashMap<String,Client>();
	private static ArrayList<Item> items;
	private static Object itemlock = new Object();
	private static String salt2 = "l;jsd98u%^sdklfb";
	private static Database db;
	public static void main(String[] args) {
		
		Client c = new Client("warren",("vu"+salt2).hashCode());
		String s = "The FUJI Sports Double Weave Judo Gi is a fantastic gi for training and competition! FUJI Double Weave Gi has been altered in 2015 to comply with the new IJF size and fit requirements. Not an official IJF Approved Gi for international competition.";
		String imlink = "https://m.media-amazon.com/images/I/41Ig8VFjR2L._AC_.jpg";
		long t = 1607407200000L;
		//items = new ArrayList<Item>();

		Item i = new Item("double-weave gi", 1,42, 2000,false,t, "", imlink,1, null);
		//items.add(i);
		clients.put(c.getUsername(), c);
		clients.put("u", new Client("u",("t"+salt2).hashCode()));
	
		Item j = new Item("Bevo Plushie", 1, 25,100,false,1606845600000L, "Cuddle with Bevo! Your little fan will love playing with this Texas Longhorns icon. Our favorite mascot is now available as a stuffed toy! This plush animal is too adorable to resist. He's built with wire in his legs giving him the ability to stand and pose.\r\n" + 
				"","https://www.universitycoop.com/media/product-images/11040267-default-11.jpg?resizeid=5&resizeh=1200&resizew=1200",25,null);
		
		 EmbeddedDataSource dataSource = new EmbeddedConnectionPoolDataSource();
		
		dataSource.setDatabaseName("eHill_database");
		dataSource.setCreateDatabase("create");
		db = new Database(dataSource);
		/*
		try {
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		//run the following try catch onse
		/*
		try {
			db.clearDatabase();
			System.exit(0);
		}
		catch(SQLException e) {
			
		} */
		/*
		try {
			db.initialize();
			db.insertItem(i);
			db.insertItem(j);
			db.insertItem(new Item("Fire stick", 3,400,6000,false,System.currentTimeMillis()+60000L/2,"An amazon product","https://images-na.ssl-images-amazon.com/images/I/61nHYoCoFwL._AC_SY300_.jpg",400,null));

			db.insertItem(new Item("PS5",2,499, 5000,false, 1607580000000L,"Explore uncharted virtual territories and slay dragons with this sleek Sony PlayStation 5 gaming console. The 825GB SSD allows ultra-fast load times, while 3-D audio output produces crisp acoustics. This Sony PlayStation 5 gaming console supports haptic feedback for effortless communication in multiplayer setups, and adaptive triggers allow immersive gameplay.","https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6426/6426149_sd.jpg;maxHeight=640;maxWidth=550",499,null));
			db.insertItem(new Item(" N95 masks (20 pack)",3,20,1000,false,1608052000000L,"Whether you're insulating the garage or sanding sheetrock, reach for the 3M 8210Plus Performance Sanding and Fiberglass Respirator. Breathe easier this N95 respirator features Advanced Electrostatic Media which enhances particulate capture while reducing breathing resistance. Designed with braided straps and soft nose foam for more comfort and adjustable nose clip to help ensure a secure custom fit. A job well done starts with safety, so give yourself and your crew the 3M 8210Plus Performance Sanding and Fiberglass Respirator.","https://images.homedepot-static.com/productImages/2a22f62f-ecef-4c3d-8771-e7a7b96ca566/svn/white-3m-face-masks-8210ph20-dc-4f_145.jpg",20,null));
			db.insertItem(new Item("Intro to Java Programming",4,60,1000,false,1608444000000L," A java textbook used for EE422C. Surprisingly useful for code examples","https://images-na.ssl-images-amazon.com/images/I/51hOnFPzUfL._SX398_BO1,204,203,200_.jpg", 60,null ));
			db.insertClient(c);
			db.insertClient(new Client("u", ("t"+salt2).hashCode()));
			db.insertClient(new Client("greg", ("fenves"+salt2).hashCode()));
			db.insertClient(new Client("admin", ("admin" +salt2).hashCode()));
			db.insertClient(new Client ("P",("F" + salt2).hashCode()));
		} 
		catch(SQLException e) {System.out.println("Error adding stuff in db"); e.printStackTrace();}	
		*/
		try {
			items = (ArrayList<Item>)db.getAllItems();
			List<Client> users = db.getAllClients();
			for(Client c1: users) {
				clients.put(c1.getUsername(), c1);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		/*
		try {
			InetAddress ext_ip = InetAddress.getByName("73.32.123.226");
			System.out.println("First part" +ext_ip+ " ");
			InetAddress int_ip = InetAddress.getByName("192.168.0.17");
			int port = 5000;
			System.out.println(InetAddress.getByName("192.168.0.17"));
			Socket socket = new Socket(ext_ip, 20, InetAddress.getByName("192.168.0.17"),20);
			System.out.println("Successfully made server");
		}
		catch(SecurityException e) {
			System.out.println("security exception");
		}
		catch(UnknownHostException e) {
			System.out.println("unknown host exception");
		}
		catch(Exception e) {
			System.out.println("exception making socket");
		}
		
		*/
	
		try{
			server = new Server();
			server.start();
		}
		catch(Exception e) {}
	}
	 class ClientHandler implements Runnable,Observer{
			Socket s;
			DataOutputStream out;
			DataInputStream in;
			boolean quit = false;
			public ClientHandler(Socket clientSocket) {
				this.s = clientSocket;
				try {
					 out = new DataOutputStream(s.getOutputStream()); //output to client
					in = new DataInputStream(s.getInputStream());	//input from client
					//System.out.println("i/o made \n");
					}catch(IOException e) {
						System.out.println("Error making i/o \n");
					}
			}
			public void run() {
			

				
				
				Gson g = new Gson();
				JsonParser p = new JsonParser();
				while(!quit) {
				try {

					
					//String inpt = in.readUTF();
					
					String in1 = in.readUTF();
					String d_in = decrypt(in1);
					JsonObject json = p.parseString(d_in).getAsJsonObject();
					//System.out.println("request received: " + json.get("type").getAsString());
					String type = json.get("type").getAsString();
					
					if(type.equals("login")) {
						//System.out.println((String)json.get("username") + " " +(String)json.get("password") );
					//	System.out.println(json.get("isguest").getAsBoolean());
						handleloginrequest(json.get("isguest").getAsBoolean(),json.get("username").getAsString(),json.get("password").getAsInt());
					}
					else if(type.equals("newaccnt")) {
						handlenewaccnt(json.get("info"));
					}
					else if(type.equals("bid")) {
						//System.out.println("received bid "+ json.toString() );
						
						updatebid(json.get("bidtype").getAsString(), json.get("price").getAsDouble(),json.get("id").getAsString(), json.get("bidder").getAsString());
						
					}
					else if(type.equals("quit")) {
						quit = true;

						try {
							in.close();
							out.close();
						}
						catch(Exception e) {}
						server.deleteObserver(this);
						s.close();
					}
				} catch (Exception  e) {
					// TODO Auto-generated catch block
				//Will this automatically read or does it need to be in a while loop?
					//System.out.println("exception");
				}
				}
			}
			public void handleloginrequest(boolean isguest, String usern, int pass) {
			//	System.out.println("\n In handle login");
				JsonObject j = new JsonObject();
				j.addProperty("type", "loginrh");
				
				if(isguest) {
					j.addProperty("status", "accepted");
					try {
						//System.out.println(j.toString());
						out.writeUTF(encrypt(j.toString()));
						out.flush();
						sendItems();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
				try {
				if(clients.containsKey(usern)) {
					if(clients.get(usern).getPassword()== pass) {
						j.addProperty("status", "accepted");
						out.writeUTF(encrypt(j.toString()));
						out.flush();
						sendItems();
						return;
				}
					else {
						j.addProperty("status", "rejected");
						out.writeUTF(encrypt(j.toString()));
						out.flush();

					}
				
				}
				else {
				j.addProperty("status", "rejected");
				out.writeUTF(encrypt(j.toString()));
				out.flush();
}
				}catch(Exception e) {}
				//System.out.println("\nlogin incorrect");

				
			}
			}
			
		
			@Override
			public void update(Observable o, Object arg) {
				// TODO Auto-generated method stub
				//System.out.println("observer notified");
				Gson g = new Gson();
				try {
					JsonObject j = new JsonObject();
					j.addProperty("type", "item_update");
					j.addProperty("id", items.get((int)arg).getName());
					j.addProperty("price", items.get((int)arg).getPrice());
					j.addProperty("isSold",  items.get((int)arg).isSold());
					
					j.addProperty("bidinfo",	g.toJson(items.get((int)arg).getlastt()));
					out.writeUTF(encrypt(j.toString()));
					out.flush();
				//	System.out.println(j.toString());


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			public void handlenewaccnt(JsonElement s) {
				Gson g = new Gson();
				JsonObject j = new JsonObject();
				j.addProperty("type", "make_accnt_ret");
				Client newclient = g.fromJson(s, Client.class);
				if(!clients.containsKey(newclient.getUsername())) {
				clients.put(newclient.getUsername(),newclient);
				j.addProperty("status", "accepted");
				try {
					db.insertClient(newclient);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else {
					j.addProperty("status","rejected");
					
				}
				try {
					out.writeUTF(encrypt(j.toString()));
					out.flush();
				}
				catch(Exception e) {}
			}
			public void sendItems() {
				Gson g = new Gson();
				synchronized(itemlock) {
					try {
					for(Item i:items) {
						JsonObject j = new JsonObject();
						j.addProperty("type", "new_item");
						j.add("info", g.toJsonTree(i));
					//	System.out.println(j.toString());
						out.writeUTF(encrypt(j.toString()));
						out.flush();
					}
					}
					catch(Exception e) {}
				}
				
			}
			
			
				
			}
	public void checkend() {
		Timer itemexp = new Timer();
		TimerTask t = new TimerTask() {
			@Override
			public void run() {
				synchronized(itemlock) {
					for(int i =0;i< items.size();i++) {
						Item item = items.get(i);
						if(!item.isSold() &&System.currentTimeMillis() > item.getendtime()) {
							//sell to the highest bidder
							item.setSold();
							server.setChanged();
							server.notifyObservers(i);
							
						}
					}
					//System.out.println("checked end");
				}
			}
		};
		itemexp.scheduleAtFixedRate(t, 0, 1000);
		
	}
	public void start() {
	//	System.out.println("Hello, Welcome to the Multithread Server\n");
		
		int port = 5000;
		//Load the data
		 
		 //continuously check
		 checkend();
	 try {
		 
			ServerSocket s = new ServerSocket(port);
		//	System.out.println(s.getInetAddress());
			//System.out.println(s.getLocalSocketAddress());
			//System.out.println("Multithread server started\n");
			//System.out.println("Multithread server started");
			while(true) {
				//System.out.println("startloop\n");
				Socket clientSocket = s.accept();
				//System.out.println(clientSocket.toString() +"\n");
				ClientHandler handler = new ClientHandler(clientSocket);
				this.addObserver(handler);
				Thread t = new Thread(handler);
				t.start();
				//System.out.println("Connection made\n");
			}
	 }
	 catch(Exception e) {
		 System.out.println("observer not added");
	 }
	
	

	}
	public void updatebid(String bidtype, double amount, String id, String buyer) {
		int item_ind=-1;

		synchronized(itemlock) {
			//find it

			for(int i =0; i < items.size();i++) {
				if (items.get(i).getName().equals(id)) {
					item_ind = i;
					break;
				}
			}
			if(bidtype.equals("buyout")) {
				items.get(item_ind).setSold();
			//	System.out.println("buyout occured");
				items.get(item_ind).setPrice(items.get(item_ind).getbo());
				items.get(item_ind).addtransaction(buyer,String.valueOf(items.get(item_ind).getPrice()));
				this.setChanged();
				this.notifyObservers(item_ind);
	
			}
			else if(bidtype.equals("newprice")) {
				if (amount >  items.get(item_ind).getPrice()) {
					items.get(item_ind).setPrice(amount);
					//System.out.println("Price changed " + items.get(item_ind).getPrice());
					items.get(item_ind).addtransaction(buyer,String.valueOf(amount) );
					this.setChanged();
					this.notifyObservers(item_ind);
					
				}
				else {
					
				}
			}
			
			try {
			db.updateItem(items.get(item_ind));}
			catch(Exception e) {
				
			}
			//System.out.println(items.get(item_ind).getbhString());
		}

	
}
	public static String encrypt(String str) {
		try
	    {
	        byte[] ivps = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivpspec = new IvParameterSpec(ivps);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivpspec);
	        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
	    } 
	    catch (Exception e) 
	    {
	        System.out.println("Error while encrypting: " + e.toString());
	    }
	    return null;
		
	}
	public static String decrypt(String str) {
		try {
			byte[] ivps = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(ivps);
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(),salt.getBytes(),65536,256);
			SecretKey temp = f.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(temp.getEncoded(),"AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey,ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
			
		}catch(Exception e) {
			
		}
		
		return null;
	}
	}
		

