package co.com.rices.beans;

import java.io.Serializable;

public class RolUsuario implements Serializable{

	private static final long serialVersionUID = -1729000228266868797L;
	
	private Integer id;
	private String  login;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

}
