package co.com.rices.beans;

import java.io.Serializable;

public class CuponCliente implements Serializable{

	private static final long serialVersionUID = -9164511314904834240L;
	
	private Integer idCliente;
	private String  cupon;
	private String  usado;
	
	private Cliente transientCliente;
	private Cupon   transientCupon;
	
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public String getCupon() {
		return cupon;
	}
	public void setCupon(String cupon) {
		this.cupon = cupon;
	}
	public String getUsado() {
		return usado;
	}
	public void setUsado(String usado) {
		this.usado = usado;
	}
	public Cliente getTransientCliente() {
		return transientCliente;
	}
	public void setTransientCliente(Cliente transientCliente) {
		this.transientCliente = transientCliente;
	}
	public Cupon getTransientCupon() {
		return transientCupon;
	}
	public void setTransientCupon(Cupon transientCupon) {
		this.transientCupon = transientCupon;
	}

	
}
