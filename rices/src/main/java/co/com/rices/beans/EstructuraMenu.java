package co.com.rices.beans;

import java.io.Serializable;

public class EstructuraMenu implements Serializable{

	private static final long serialVersionUID = 1489892760691003822L;

	private Integer idMenu;
	private String  descripcion;
	private Integer orden;
	private String  url;
	private String  estado;
	private Integer idMenuSuperior;

	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Integer getIdMenuSuperior() {
		return idMenuSuperior;
	}
	public void setIdMenuSuperior(Integer idMenuSuperior) {
		this.idMenuSuperior = idMenuSuperior;
	}


}
