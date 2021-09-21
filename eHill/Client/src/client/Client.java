package client;

public class Client {
private String username;
private int password;
private String[] ccinfo;
public Client(String u, int p) {
	username = u;
	password = p;
}

public boolean matches(String u, int p) {
	return this.username.equals(u) && password == p;
}

public void changePassword(int p) {
	this.password = p;
}
@Override
public String toString() {
	return this.username + " " + this.password;
}
public String getUsername() {
	return username;
}
}
