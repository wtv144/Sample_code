package client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Observer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.*;
import org.json.simple.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
public class eHillClient extends Application{
	static boolean loggedin = false;
	static Stage stage;
	static GridPane lw;
	static Stage newaccntw;
	static Stage guestlgn;
	static ArrayList<Item> items = new ArrayList<Item>();
	static ObservableList<Item> s_items = FXCollections.observableArrayList(items);
	static String user;
	public static DataOutputStream toServer;
	public static DataInputStream fromServer;
	private static String secretKey = "akd823/mbkfo283k";
	private static String salt = "ee422c&*(&#(*@$&@#$";
	static String salt2 = "l;jsd98u%^sdklfb";
	Insets standard = new Insets(4,8,4,8);
	@Override
	public void start(Stage primarystage) {
		ArrayList<Integer> test = new ArrayList<Integer>();
		ObservableList<Integer> t1 = FXCollections.observableArrayList(test);
		
		stage = primarystage;
		stage.setHeight(1000);
		stage.setWidth(1200);
		UI.init();
		Controller.init();
		stage.setScene(UI.start);
		stage.show();
		
		try {
			@SuppressWarnings("resource")
			//InetAddress ip = InetAddress.getByName("localhost");
			// Socket socket = new Socket("localhost", 5000);
			
			 Socket socket = new Socket("localhost",5000);
		
			toServer = new DataOutputStream(socket.getOutputStream());
			fromServer = new DataInputStream(socket.getInputStream());
		}catch(IOException e) {
			//System.out.println("socket not connected");
		}
		new Thread(()->{
			try {
				while(true) {
					String s_in = fromServer.readUTF();
					String inp = decrypt(s_in);
					Platform.runLater(()->{
						Controller.handle(inp);

					});
				}
			}
			catch(Exception e) {}
		}).start();
		}
		
		
	
	public static String encrypt(String str) {
		try
	    {
	        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	        IvParameterSpec ivspec = new IvParameterSpec(iv);
	         
	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 256);
	        SecretKey tmp = factory.generateSecret(spec);
	        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	         
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
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
	
public static void main(String[] args) {
	launch(args);
}


}

	


