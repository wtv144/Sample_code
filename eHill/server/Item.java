package server;

import java.util.ArrayList;
import java.util.Observable;

import com.google.gson.Gson;

public class Item {
	private String item_name;
	private int item_id;
	private double price;
	private double buyoutprice;
	private boolean issold;
	private long endtime;
	private String description;
	private String imglink;
	private double minprice;
	private ArrayList<String[]> bidhist;
	public Item() {
		
	}
	public Item(String name, int p) {
		this.item_name=name;
		this.price=p;
		 bidhist = new ArrayList<String[]>();
	}
	public Item(String name, int id,double price, double bp, boolean is, long et, String des,String im, double minprice, String bh) {
		this.item_name=name;
		this.item_id=id;
		this.price=price;
		buyoutprice = bp;
		issold = is;
		endtime = et;
		description =des;
		imglink = im;
		 this.minprice= minprice;
		 if(bh!= null && !bh.isEmpty()) {
		 bidhist =parsebhs(bh);}
		 else {
			 bidhist = new ArrayList<String[]>();
		 }

	}
	
	public String getName() {
		return this.item_name;
	}
	public double getPrice() {
		return this.price;
	}
	public int getId() {
		return this.item_id;
	}
	public boolean isSold() {
		return this.issold;
	}
	public void setSold() {
		issold = true;
	}
	public void setPrice(double p) {
		this.price = p;
	}
	public double getbo() {
		return this.buyoutprice;
	}
	public long getendtime() {
		return this.endtime;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String s) {
		this.description = s;
	}
	public void setimglink(String s) {
		this.imglink=s;
	}
	public String getimglink() {
		return this.imglink;
	}
	public void setend(long t) {
		this.endtime = t;
	}
	public void addtransaction(String buyer, String price) {
		String[] a = new String[2];
		a[0] = buyer;
		a[1]=price;
		bidhist.add(0,a);
	}
	public String[] getlastt(){
		if(bidhist == null || bidhist.size() == 0) {
			return null;
		}
		return bidhist.get(0);
	}
	public double getMinp() {
		return this.minprice;
	}
	public boolean bhempty() {
		String s = "";
		for(String[] sarr:bidhist) {
			s+=sarr[0]+sarr[1];
		}
		System.out.println("bhstring" + s.trim().isEmpty());
		return s.trim().isEmpty();
	}
	public String getbhString() {
		String ret = "";
		Gson g = new Gson();
		for(String[] sarr: bidhist) {
			ret+= g.toJson(sarr)+"\n";
		}
		return ret;
		
	}
	public ArrayList<String[]> parsebhs(String s) {
		ArrayList<String[]> bh = new ArrayList<String[]>();
		String[] vals = s.split("\n");
		Gson g = new Gson();
	
		for(int i = 0; i <vals.length;i++) {
			String[] temp = g.fromJson(vals[i], String[].class);
			bh.add(temp);
			
		}
		
		return bh;
		
	}
}
