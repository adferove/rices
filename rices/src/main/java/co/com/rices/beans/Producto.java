package co.com.rices.beans;

import java.io.Serializable;
import java.util.Date;

public class Producto implements Serializable{

	private static final long serialVersionUID = -6918780067810367768L;

	private Integer id;
	private String  nombre;
	private String  descripcion;
	private Date    fechacreacion;
	private String  estado;
	private String  loginusuario;
	private Integer rating;
	private String  rutaImagen;
	private ProductoPrecio productoPrecio;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getLoginusuario() {
		return loginusuario;
	}
	public void setLoginusuario(String loginusuario) {
		this.loginusuario = loginusuario;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public ProductoPrecio getProductoPrecio() {
		return productoPrecio;
	}
	public void setProductoPrecio(ProductoPrecio productoPrecio) {
		this.productoPrecio = productoPrecio;
	}
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	
	public Producto clon(){
		Producto clon = new Producto();
		clon.setId(new Integer(this.id));
		clon.setNombre(new String(this.nombre));
		clon.setDescripcion(new String(this.descripcion));
		clon.setFechacreacion((Date) this.fechacreacion.clone());
		clon.setEstado(new String(this.estado));
		clon.setLoginusuario(new String(this.loginusuario));
		clon.setRating(new Integer(this.rating));
		clon.setRutaImagen(new String(this.rutaImagen));
		clon.setProductoPrecio(this.productoPrecio.clon());
		return clon;
	}
}
