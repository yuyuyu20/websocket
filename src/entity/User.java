package entity;

public class User {
	private String acc;
	private String pwd;
	private String name;

	public String getAcc() {
		return acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User [acc=" + acc + ", pwd=" + pwd + ", name=" + name + "]";
	}

	public User(String acc, String pwd, String name) {
		super();
		this.acc = acc;
		this.pwd = pwd;
		this.name = name;
	}

	public User() {
		super();
	}

}
