package co.com.rices.objects;

import java.io.Serializable;

public class City implements Serializable{

	private static final long serialVersionUID = 7754639768781035707L;
	
	private Integer id;
	private String  name;
	private Integer idSuperior;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIdSuperior() {
		return idSuperior;
	}
	public void setIdSuperior(Integer idSuperior) {
		this.idSuperior = idSuperior;
	}

	
}
