package co.com.rices.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Cupon implements Serializable{

	private static final long serialVersionUID = -4521701167940019417L;
	
	private String     cupon;
	private String     descripcion;
	private BigDecimal porcentaje;
	private String     estado;
	private String     usuarioCrea;
	private Date       fechaCrea;
	private Date       horaCrea;
	
	public String getCupon() {
		return cupon;
	}
	public void setCupon(String cupon) {
		this.cupon = cupon;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUsuarioCrea() {
		return usuarioCrea;
	}
	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}
	public Date getFechaCrea() {
		return fechaCrea;
	}
	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}
	public Date getHoraCrea() {
		return horaCrea;
	}
	public void setHoraCrea(Date horaCrea) {
		this.horaCrea = horaCrea;
	}
	
	

}
