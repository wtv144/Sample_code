package client;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.json.simple.*;
import org.json.simple.parser.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.scene.Scene;
public class Controller {
	static Gson g = new Gson();
	static JsonParser p = new JsonParser();
	public static void init() {
		UI.loginb.setOnAction(e->{
			trylogin(false);
		});
		UI.guestButton.setOnAction(e->{
			trylogin(true);
		});
		UI.newacctb.setOnAction(e->{
			eHillClient.stage.setScene(UI.makeaccnt);
		});
		UI.mkaccntb.setOnAction(e->{
			createaccount();
		});
		UI.buyoutb.setOnAction(e->{
			makebid(true);
		});
		UI.bidb.setOnAction(e->{
			makebid(false);
		});
		UI.logoutb.setOnAction(e->{
			logout();
		});
		UI.quitb.setOnAction(e->{
			quit();
		});
		UI.backb.setOnAction(e->{
			eHillClient.stage.setScene(UI.shop);
			UI.v_items.refresh();
		});
		UI.ilogoutb.setOnAction(e->{
			logout();
		});
		UI.iquitb.setOnAction(e->{
			quit();
		});
		UI.squitb.setOnAction(e->{
			quit();
		});
		UI.na_backb.setOnAction(e->{
			eHillClient.stage.setScene(UI.start);
			UI.countdown.getKeyFrames().clear();
			UI.countdown.stop();
			
		});
	}
	public static void handle(String json) {
		try {
			JsonObject j = p.parseString(json).getAsJsonObject();
		String t = j.get("type").getAsString();	
		if(t.equals("loginrh")) {
			loginreturn( j.get("status").getAsString());
		}
		else if (t.equals("new_item")){
		//	System.out.println("new item" + j.toString());
			
			add_item(j.get("info"));
		}
		else if (t.equals("item_update")) {
			//System.out.println(j.toString());
			if(eHillClient.loggedin) {
			update_item(j.get("id").getAsString(), j.get("isSold").getAsBoolean(), j.get("price").getAsDouble(), j.get("bidinfo").getAsString());
			}
		}
		else if (t.equals("make_accnt_ret")) {
			
				//show the choose other username
				createaccountreturn(j.get("status").getAsString());
			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void trylogin(boolean isGuest) {
		JsonObject j = new JsonObject();
		j.addProperty("type", "login");

		if(!isGuest) {
		String usern = UI.usertxt.getText();
		String pass = UI.passfield.getText();

		//System.out.println("Tried to log in");
		/*
		JSONObject j = new JSONObject();
		j.put("type", "login");
		j.put("username", usern);
		j.put("password", pass);*/
		j.addProperty("username",usern);
		j.addProperty("isguest", false);
		eHillClient.user = usern;
		j.addProperty("password", (pass+eHillClient.salt2).hashCode());
		try {
			//System.out.println(eHillClient.toServer.toString());
			eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
			
			/*String res  = eHillClient.fromServer.readUTF();
			if(res.contentEquals("true")) {
				System.out.println("logged in!");
			}
			else {return;}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		else {
			j.addProperty("username","filler");
			j.addProperty("password", 0);
			j.addProperty("isguest", true);
			eHillClient.user = "Guest";
			try {
			eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
			eHillClient.toServer.flush();}
			catch(Exception e) {}
			}
			System.out.println(j.toString());
		
		
		
			
			//System.out.println("passed to server");
		
			// TODO Auto-generated catch block
		
		
	}
	private static void createaccount() {
		String usern = UI.n_user.getText();
		String pass = UI.n_pass.getText();
		UI.na_acc.setVisible(false);
		UI.na_rej.setVisible(false);
		Client c = new Client(usern, (pass+eHillClient.salt2).hashCode());
		if(!usern.isEmpty() && !pass.isEmpty()) {
			//make the account
			JsonObject j = new JsonObject();
			j.addProperty("type", "newaccnt");		
			j.add("info", g.toJsonTree(c));
			try {
			eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
			eHillClient.toServer.flush();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	private static void createaccountreturn(String s) {
	//	System.out.println("create account return");
		if(s.equals("accepted")) {
			//show account created
			UI.na_acc.setVisible(true);
		}
		else {
			//show pick another username 
			UI.na_rej.setVisible(true);
		}
		
	}
	private static void logout() {
		eHillClient.loggedin = false;
		eHillClient.user = null;
		eHillClient.s_items.clear();
		eHillClient.items.clear();
		eHillClient.stage.setScene(UI.start);
		UI.usertxt.clear();
		UI.passfield.clear();
		UI.inv_login.setVisible(false);
	}
	private static void quit() {
		JsonObject j = new JsonObject();
		j.addProperty("type", "quit");
		UI.backmus.stop();
		try {
			eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
			eHillClient.fromServer.close();
			eHillClient.toServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.exit(0);
	}
	private static void loginreturn(String s) {
		if(s.equals("accepted")) {
			eHillClient.stage.setScene(UI.shop);
			eHillClient.loggedin =  true;
			
		}
		else {
			eHillClient.user = null;
			UI.inv_login.setVisible(true);
		}
		
	}
	private static void add_item(JsonElement j) {
		Item newitem = g.fromJson(j,Item.class);
		eHillClient.items.add(newitem);
		eHillClient.s_items.add(newitem);
	}
	private static void update_item(String id, boolean isSold, double price, String bidin) {
		int ind=-1;
		for(int i =0; i < eHillClient.s_items.size();i++) {
			if(eHillClient.s_items.get(i).getName().equals(id)){
				ind =i;
			}
		}
		String[] bidinfo = g.fromJson(bidin, String[].class);
	//	System.out.println(Arrays.toString(bidinfo));
		System.out.println(Arrays.toString(bidinfo));
		System.out.println(eHillClient.s_items.get(ind).getlastt());
		if(!Arrays.equals(bidinfo, eHillClient.s_items.get(ind).getlastt())) {
		eHillClient.items.get(ind).addtransaction(bidinfo[0], bidinfo[1]);}
		else {
			eHillClient.items.get(ind).addtransaction("", "");		}
		if(isSold) {
			eHillClient.s_items.get(ind).setSold();
			//eHillClient.s_items.get(ind).setPrice(eHillClient.s_items.get(ind).getbo());
			eHillClient.s_items.get(ind).setPrice(price);
		}
		else {
			eHillClient.s_items.get(ind).setPrice(price);
		
			
		}
		
		//refresh page and shit
		UI.updateView(ind);
		
	}
	private static void makebid(boolean isbuyout) {
		JsonObject j = new JsonObject();
		j.addProperty("type", "bid");
		j.addProperty("id", eHillClient.items.get(UI.itm_ind).getName());
		j.addProperty("bidder", eHillClient.user);
		//add user
		try {
		if(!eHillClient.items.get(UI.itm_ind).isSold() && System.currentTimeMillis() < eHillClient.items.get(UI.itm_ind).getendtime()) {

		if(isbuyout ) {
			j.addProperty("bidtype", "buyout");
			j.addProperty("price", eHillClient.items.get(UI.itm_ind).getbo());
			eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
			eHillClient.toServer.flush();		
			
		}
		else {			

			try {
				double bidamt = Double.parseDouble(UI.bidamt.getText());
				DecimalFormat df = new DecimalFormat("#.##");      
				bidamt = Double.valueOf(df.format(bidamt));
				if(bidamt >= eHillClient.items.get(UI.itm_ind).getbo()) {
					
					j.addProperty("bidtype", "buyout");
					j.addProperty("price", eHillClient.items.get(UI.itm_ind).getbo());
					eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
					eHillClient.toServer.flush();

				}
				else if(bidamt > eHillClient.items.get(UI.itm_ind).getPrice()) {
					
					j.addProperty("bidtype", "newprice");

					j.addProperty("price", bidamt);
					eHillClient.toServer.writeUTF(eHillClient.encrypt(j.toString()));
					eHillClient.toServer.flush();
					
				}
				else {
					//make enter valid bid
					UI.inv_bida.setVisible(true);
				}
			}catch(Exception e) {
				//make enter valid bid
				UI.inv_bida.setVisible(true);

			}
			
		}}
		else {
			//do something for is sold
			UI.inv_bids.setVisible(true);
		}
		}
		catch(Exception e) {
			
		}
		UI.bidamt.clear();
	}
	
}
