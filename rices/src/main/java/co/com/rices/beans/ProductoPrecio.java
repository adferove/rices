package co.com.rices.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProductoPrecio implements Serializable{

	private static final long serialVersionUID = -3753454468195755919L;
	
	private Integer    id;
	private Integer    idProducto;
	private BigDecimal precio;
	private String     estado;
	private String     usuarioCrea;
	private Date       fechaCrea;
	private String     usuarioActualiza;
	private Date       fechaActualiza;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
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
	public String getUsuarioActualiza() {
		return usuarioActualiza;
	}
	public void setUsuarioActualiza(String usuarioActualiza) {
		this.usuarioActualiza = usuarioActualiza;
	}
	public Date getFechaActualiza() {
		return fechaActualiza;
	}
	public void setFechaActualiza(Date fechaActualiza) {
		this.fechaActualiza = fechaActualiza;
	}
	public ProductoPrecio clon(){
		ProductoPrecio clon = new ProductoPrecio();
		clon.setId(new Integer(this.id));
		clon.setIdProducto(new Integer(this.idProducto));
		clon.setPrecio(this.precio);
		clon.setEstado(new String(this.estado));
		clon.setUsuarioCrea(new String(this.usuarioCrea));
		clon.setFechaCrea((Date) this.fechaCrea.clone());
		clon.setUsuarioActualiza(new String(this.usuarioActualiza));
		clon.setFechaActualiza((Date) this.fechaActualiza.clone());
		return clon;
	}
}
