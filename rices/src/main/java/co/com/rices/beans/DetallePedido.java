package co.com.rices.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import co.com.rices.objects.Product;

public class DetallePedido implements Serializable{

	private static final long serialVersionUID = -5393826053155618664L;
	
	private Integer    id;
	private Integer    idpedido;
	private Integer    idproducto;
	private Integer    cantidad;
	private String     observacion;
	private BigDecimal precio;
	private BigDecimal productPrice;
	
	private Producto   producto;
	
	private Product    mainProduct;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdpedido() {
		return idpedido;
	}
	public void setIdpedido(Integer idpedido) {
		this.idpedido = idpedido;
	}
	public Integer getIdproducto() {
		return idproducto;
	}
	public void setIdproducto(Integer idproducto) {
		this.idproducto = idproducto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Product getMainProduct() {
		return mainProduct;
	}
	public void setMainProduct(Product mainProduct) {
		this.mainProduct = mainProduct;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	
}
