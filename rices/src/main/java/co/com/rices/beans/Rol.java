package co.com.rices.beans;

import java.io.Serializable;

public class Rol implements Serializable{

	private static final long serialVersionUID = -7460921994273537477L;
	
	private Integer id;
	private String  descripcion;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
