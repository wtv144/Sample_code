package client;

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
	private ArrayList<String[]> bidhist;

	public Item() {
		
	}
	public Item(String name, int p) {
		this.item_name=name;
		this.price=p;
	}
	public Item(String name,  double price, double bp, boolean is, long et, String des,String im) {
		this.item_name=name;
		this.price=price;
		buyoutprice = bp;
		issold = is;
		endtime = et;
		description =des;
		imglink = im;
		bidhist = new ArrayList<String[]>();
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
	public void addtransaction(String buyer, String price) {
		String[] a = new String[2];
		a[0] = buyer;
		a[1]=price;
		bidhist.add(0,a);
	}
	public String[] getlastt(){
		if(bidhist == null || bidhist.size()==0 ) {
		return null; }
		return bidhist.get(0);
	}
	public ArrayList<String[]> getth(){
		return this.bidhist;
	}
	public boolean isa_end() {
		return this.endtime < System.currentTimeMillis();
	}
	public String getbhString() {
		String ret = "";
		Gson g = new Gson();
		for(String[] sarr: bidhist) {
			ret+= g.toJson(sarr)+"\n";
		}
		return ret;
		
	}
	public boolean bhempty() {
		String s = "";
		for(String[] sarr:bidhist) {
			s+=sarr[0]+sarr[1];
		}
		System.out.println("bhstring" + s.trim().isEmpty());
		return s.trim().isEmpty();
	}

}

