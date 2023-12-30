package Server;

public class Personal {
	private String iduser;
	private String name;
	private String sdt;
	private String email;
	private String username;
	private String password;

	public Personal(String id, String name, String sdt, String email, String username, String password) {
		super();
		this.iduser = id;
		this.name = name;
		this.sdt = sdt;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	

	public String getId() {
		return iduser;
	}


	public void setId(String id) {
		this.iduser = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSdt() {
		return sdt;
	}


	public void setSdt(String sdt) {
		this.sdt = sdt;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
//		return  name + "," + sdt + "," + email + "," + username + "," + password;
		return iduser + "," + username + "," + password + "," + name + "," + sdt + "," + email + ",";
	}

}
