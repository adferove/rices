package co.com.rices.beans;

import java.io.Serializable;

public class Cliente implements Serializable{

	private static final long serialVersionUID = -2224093693089615661L;
	private Integer id;
	private String  email;
	private String  nombre;
	private String  apellido;
	private String  celular;
	private String  direccion;
	private String  barrio;
	
	private boolean guardaDatos;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getBarrio() {
		return barrio;
	}
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}
	public boolean isGuardaDatos() {
		return guardaDatos;
	}
	public void setGuardaDatos(boolean guardaDatos) {
		this.guardaDatos = guardaDatos;
	}
	public Cliente clon(){
		Cliente clon = new Cliente();
		
		clon.setId(new Integer(this.id));
		clon.setNombre(new String(this.nombre));
		if(this.apellido!=null){
			clon.setApellido(new String(this.apellido));
		}
		clon.setEmail(new String(this.email));
		clon.setDireccion(new String(this.direccion));
		clon.setCelular(new String(this.celular));
		clon.setBarrio(new String(this.barrio));
		clon.setGuardaDatos(this.guardaDatos);
		return clon;
	}
}
