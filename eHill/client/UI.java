package client;
import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.EventHandler;
import java.util.Date.*;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.*;
public class UI {
	static Insets standard = new Insets(4,8,4,8);
	static Scene start;
	static GridPane lw;
	static Scene makeaccnt;
	static Button loginb;
	static Button guestButton;
	static Button newacctb;
	
	static Scene newa;
	static TextField n_user;
	static PasswordField n_pass;
	static Button mkaccntb;
	
	static TextField usertxt;
	static PasswordField passfield;
	static BorderPane storepage;
	static ScrollPane store_listings;
	static Button quitb;
	static Button logoutb;
	static Button squitb;
	static MediaPlayer backmus;
	public static void init() {
		makestart();
		mmakeaccnt();
		makestorepage();
		start = new Scene(lw,1200,1000);
		makeitempage();
		
		try {
		Media fh = new Media(new File("C:\\Users\\warre\\EE422C\\fall-2020-final-project-wtv144\\Client\\src\\client\\far_horizons.mp3").toURI().toString());
		Media sv = new Media(new File("C:\\Users\\warre\\EE422C\\fall-2020-final-project-wtv144\\Client\\src\\client\\sv.mp3").toURI().toString());
		//System.out.println(System.getProperty("user.dir")+ "\\client\\sv.mp3");
		//Media sv = new Media(System.getProperty("user.dir")+ "sv.mp3");
		backmus = new MediaPlayer(sv);
		backmus.play();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	static Text inv_login;
	private static void makestart()
	{
		
		lw = new GridPane();
		lw.setAlignment(Pos.CENTER);
		lw.setPadding(new Insets(25, 25, 25, 25));
		lw.setHgap(10);
		lw.setVgap(10);
		Text welcometext = new Text("Welcome to eHill");
		welcometext.setFont(new Font(20));
		lw.add(welcometext, 4, 3);
		Label userl = new Label("Username");
		userl.setPadding(standard);
		Label passl = new Label("Password");
		passl.setPadding(standard);;
		 usertxt = new TextField();
		usertxt.setPadding(new Insets(8,16,8,16) );
		 passfield = new PasswordField();
		passfield.setPadding(new Insets(8,16,8,16));
		lw.add(userl, 4,4);
		lw.add(usertxt,5,4);
		lw.add(passl, 4, 5);
		lw.add(passfield,5,5);
		loginb = new Button("Log in");
		lw.add(loginb, 5, 6);
		 newacctb = new Button("Create Account");
		lw.add(newacctb, 5, 7);
		guestButton = new Button("Log on as guest");
		lw.add(guestButton, 5, 8);
		inv_login = new Text("Invalid login");
		inv_login.setFill(Color.RED);
		inv_login.setVisible(false);
		lw.add(inv_login, 4, 9);
		squitb = new Button("quit");
		lw.add(squitb, 6, 10);
	}
	static Text na_acc;
	static Text na_rej;
	static Button na_backb;
	private static void mmakeaccnt() {
		Text na_text = new Text("Create an account");
		GridPane na_grid = new GridPane();
		na_grid.setAlignment(Pos.CENTER);
		na_grid.setPadding(new Insets(25, 25, 25, 25));
		na_grid.setHgap(10);
		na_grid.setVgap(10);
		Label n_userl = new Label ("Username");
		Label n_passl = new Label("Password");
		n_user = new TextField();
		n_user.setPadding(new Insets(8,16,8,16) );
		n_pass = new PasswordField();
		n_pass.setPadding(new Insets(8,16,8,16) );
		na_grid.add(na_text,4,3);
		na_grid.add(n_userl, 4, 4);
		na_grid.add(n_user, 5, 4);
		na_grid.add(n_passl, 4, 5);
		na_grid.add(n_pass, 5, 5);
		mkaccntb = new Button("Make account");
		na_grid.add(mkaccntb, 5, 6);
		na_backb = new Button("Back");
		na_acc = new Text("account accepted");
		na_acc.setFill(Color.GREEN);
		na_rej = new Text("account rejected. Username taken");
		na_rej.setFill(Color.RED);
		na_rej.setVisible(false);
		na_acc.setVisible(false);
		na_grid.add(na_backb, 5, 7);
		na_grid.add(na_acc, 5, 8);
		na_grid.add(na_rej, 5, 9);
		makeaccnt = new Scene(na_grid,1000,1000);
	}
	static Scene shop;
	static ComboBox<String> a_items;
	static VBox store_left;
	 static ListView<Item> v_items;
	static HBox topbar;
	private static void makestorepage() {
		storepage = new BorderPane();
		 v_items = new ListView<Item>();
		store_left = new VBox();
		Label header = new Label("Items for sale:");
		header.setStyle("-fx-font:24 arial;");
		header.setPadding(new Insets(4,8,4,8));
		store_left.getChildren().add(header);
		storepage.setLeft(store_left);
		v_items.setItems(eHillClient.s_items);
		storepage.setCenter(v_items);
		v_items.setMinWidth(250);
		
		v_items.setCellFactory(param -> new ListCell<Item>() {
			@Override
			public void updateItem(Item item, boolean empty) {
				super.updateItem(item,empty);
				setPrefHeight(80);
				setPrefWidth(400);
				if(!empty) {
				String s = item.getName() + "\t\t Price: " + item.getPrice()+"\n\t\t";		
				if(item.isSold()) {
					s+="AUCTION ENDED";
				}
				else {
					s+= "EXPIRING SOON";
				}
				setText(s);
				ImageView im = ImageViewBuilder.create().image(new Image(item.getimglink())).build();
				im.setFitHeight(40);
				im.setFitWidth(40);
				setGraphic(im);
				}
			}
		});
		v_items.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int ind =v_items.getSelectionModel().getSelectedIndex();
			//	System.out.println(eHillClient.s_items.get(ind).getName());
				setItemPage(ind);
				v_items.getSelectionModel().clearSelection();
			}
			
		});
		
		logoutb = new Button("logout");
		
		quitb = new Button("Quit");
		quitb.setMinHeight(10);
		logoutb.setMinHeight(10);;
		topbar = new HBox();
		topbar.setAlignment(Pos.CENTER_RIGHT);
		topbar.getChildren().add(quitb);
		topbar.getChildren().add(logoutb);
		storepage.setTop(topbar);
		shop = new Scene(storepage,1000,1000);

	}
	
	static Scene itemPage;
	static BorderPane itm_page;
	static GridPane i_page;
	static Button bidb;
	static TextField bidamt;
	static Button buyoutb;
	static Label itmname;
	static Button backb;
	static HBox topbar2;
	static Button ilogoutb;
	static Button iquitb;
	static TextArea bh;
	static ImageView imageview;
	public static void makeitempage() {
		itm_page = new BorderPane();
		GridPane i_page = new GridPane();
		itm_page.setCenter(i_page);

		
		 itmname = new Label("Item name holder");
		 itmname.setStyle("-fx-font:24 arial;");
		 itmname.setPadding(new Insets(4,8,4,8));
		i_page.add(itmname, 0, 0);
		 cur_pricel = new Label("Price:");
		cur_pricel.setPadding(new Insets(4,8,4,8));
		cur_pricel.setStyle("-fx-font:15 arial;");
		i_page.add(cur_pricel, 0, 3);
		Label bidamtlabel = new Label("Bid amount");
		bidamtlabel.setStyle("-fx-font:15 arial;");
		bidamtlabel.setPadding(new Insets(4,8,4,8));
		i_page.add(bidamtlabel, 0, 4);
		bidamt = new TextField();
		bidamt.setPadding(new Insets(8,16,8,16) );
		i_page.add(bidamt, 2, 4);
		bidb = new Button("Bid");
		bidb.setPadding(new Insets(8,16,8,16));
		i_page.add(bidb, 2, 5);
		
		 bo_pricel = new Label("Buyout price:");
		 bo_pricel.setStyle("-fx-font:15 arial;");
		 bo_pricel.setPadding(new Insets (4,8,4,8));
		 i_page.add(bo_pricel, 0, 6);
		buyoutb = new Button("Buyout");
		i_page.add(buyoutb, 2, 6);
		Label descriptionl = new Label ("Item description:");
		descriptionl.setPadding(new Insets(4,8,4,8));
		descriptionl.setStyle("-fx-font:15 arial;");
		String imlink = "https://www.universitycoop.com/media/product-images/100271301-Burnt%20Orange-10.jpg?resizeid=2&resizeh=240&resizew=240";
		imageview = ImageViewBuilder.create().image(new Image(imlink)).build();
		imageview.setFitWidth(400);
		imageview.setFitHeight(400);
		itm_page.setLeft(imageview);
		item_descr = new TextArea(dummytext);
		item_descr.setEditable(false);
		item_descr.setWrapText(true);
		i_page.add(descriptionl, 0, 7);
		i_page.add(item_descr, 2, 8);
		inv_bids = new Text("Invalid bid. Item sold/auction closed");
		inv_bida = new Text("Invalid bid. Bid amount too low");
		inv_bids.setFill(Color.RED);
		inv_bida.setFill(Color.RED);
		inv_bids.setVisible(false);
		inv_bida.setVisible(false);
		i_page.add(inv_bida, 0, 9);
		i_page.add(inv_bids, 0, 10);
		cdtextl = new Label("Expires in:");
		cdtextl.setPadding(new Insets(4,8,4,8));
		cdtextl.setStyle("-fx-font:24 arial;");
		i_page.add(cdtextl, 0, 11);
		cdtext= new Text(":mm:dd:hh:mm:ss");
		cdtext.setStyle("-fx-font:24 arial;");
		cdtext.setFill(Color.RED);
		i_page.add(cdtext, 2, 11);
		topbar2 = new HBox();
		topbar2.setAlignment(Pos.CENTER_RIGHT);
		backb = new Button("Back");
		topbar2.getChildren().add(backb);
		ilogoutb = new Button("logout");
		iquitb = new Button("Quit");
		topbar2.getChildren().add(iquitb);
		topbar2.getChildren().add(ilogoutb);
		iquitb.setMinHeight(10);
		ilogoutb.setMinHeight(10);
		backb.setMinHeight(10);
		itm_page.setTop(topbar2);
		Label bhl = new Label("Bidding history:");
		bhl.setStyle("-fx-font:15 arial;");
		bhl.setPadding(new Insets(4,8,4,8));
		bhl.setAlignment(Pos.TOP_RIGHT);
		 bh = new TextArea();
		i_page.add(bhl, 0, 13);
		i_page.add(bh, 2, 14);
		itemPage = new Scene(itm_page, 1000,1000);
		
		
	}
	/*Layout plan:
	 * Item name on tom
	 * Image
	 * Price
	 * bid stuff
	 * Buyoutprice
	 * itemdescription
	 */
	static Label cur_pricel;
	static Label bo_pricel;
	static TextArea item_descr;
	static String dummytext = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. In cursus turpis massa tincidunt dui. Habitant morbi tristique senectus et netus. Vitae ultricies leo integer malesuada nunc vel risus. Fermentum et sollicitudin ac orci. Massa tincidunt nunc pulvinar sapien et ligula. Velit scelerisque in dictum non consectetur a. Magna sit amet purus gravida quis. Ac auctor augue mauris augue neque gravida in. Arcu cursus vitae congue mauris rhoncus. Scelerisque viverra mauris in aliquam sem. Tellus mauris a diam maecenas sed enim. Non tellus orci ac auctor augue. Sagittis aliquam malesuada bibendum arcu vitae elementum curabitur. Feugiat in fermentum posuere urna. Aliquam malesuada bibendum arcu vitae elementum curabitur vitae. Purus ut faucibus pulvinar elementum integer. Etiam erat velit scelerisque in dictum. Diam phasellus vestibulum lorem sed risus ultricies.";
	static int itm_ind;
	 static Timeline countdown;
	private static Label cdtextl;
	private static Text cdtext;
	private static boolean isfirst =true;
	public static void setItemPage(int ind) {
		inv_bids.setVisible(false);
		inv_bida.setVisible(false);
		bh.clear();
		bidamt.clear();
	
		itm_ind = ind;
		Item curitm = eHillClient.s_items.get(itm_ind);
		if(!isfirst) {
			isfirst=false;
			countdown.getKeyFrames().clear();
			countdown.playFromStart();
		}
		bo_pricel.setText("Buyout price: " + curitm.getbo());
		cur_pricel.setText("Price: " + curitm.getPrice());
		item_descr.setText(curitm.getDescription());
		itmname.setText(curitm.getName());
		String ilink = curitm.getimglink();
		 imageview = ImageViewBuilder.create().image(new Image(ilink)).build();
	
		 imageview.setFitWidth(400);
			imageview.setFitHeight(400);
		itm_page.setLeft(imageview);
		if(!curitm.isSold()) {
		countdown = new Timeline( new KeyFrame(Duration.seconds(1),e->{
			int itemin = ind;
			
			if(!eHillClient.items.isEmpty() &&itm_ind == ind && !eHillClient.items.get(ind).isSold() ) {
		
			long diff = curitm.getendtime()-System.currentTimeMillis();
			
			Period p = new Period(diff);
			if(diff < 0 || curitm.isSold()) {
				cdtext.setText("Auction Ended");
				cdtextl.setVisible(false);
				countdown.stop();
				
			}
			String res = PeriodFormat.getDefault().print(p).split("and")[0];
			cdtext.setText(res);
		}
			
			
		}));
	
		countdown.setCycleCount(Timeline.INDEFINITE);
		countdown.play();
		}
		else {
			cdtext.setText("Auction Ended");
			if(curitm.bhempty()) {
				bh.appendText("Auction ended without buyers");
			}
		}
		System.out.println(curitm.getbhString().trim());
		if(!curitm.bhempty() ) {
			if(curitm.isSold()) {
				bh.appendText("SOLD to the last bidder \n");

			}
		ArrayList<String[]> bids = curitm.getth();
		for(int i = 0; i < bids.size();i++) {
			String[] a = bids.get(i);
			if(!a[0].isEmpty() && !a[0].isEmpty()) {
			bh.appendText("Bidder: " + a[0] + "\t\t" + "Price: " + a[1] + "\n");}
		}}
		
		eHillClient.stage.setScene(itemPage);
		
		
	}
	
	static Text inv_bids;
	static Text inv_bida;
	public static void updateView(int ind) {
		inv_bids.setVisible(false);
		inv_bida.setVisible(false);
		if(itm_ind == ind) {	//Update only if it is on the same page. 
		Item itm = eHillClient.s_items.get(ind);
		String[] a = itm.getlastt();
		//System.out.println(a);
		String lastt ="";
		if(!a[0].isEmpty()) {
		 lastt = " Bidder: " + a[0] + "\t\t" + "Price: " + a[1] + "\n";
		 if(itm.isSold()) {
			 lastt="(SOLD)" +lastt;
		 }
		}
		else if(UI.bh.getText().trim().isEmpty()) {
			lastt = "Auction ended without a buyer";
		}
		else if( itm.isSold()){
			lastt = "(SOLD) to the last bidder\n";
		}
		bh.insertText(0, lastt);

		if(!itm.isSold()) {
			bo_pricel.setText("Buyout price: " + itm.getbo());
			cur_pricel.setText("Price: " + itm.getPrice());
			
		}
		else {
			cur_pricel.setText("Price: " + itm.getPrice());
			//countdown.stop();
			cdtext.setText("Auction ended");
			countdown.getKeyFrames().clear();
			cdtext.setText("Auction ended");
			countdown.stop();
			
		}
		}

		v_items.refresh();
		
	}
}
