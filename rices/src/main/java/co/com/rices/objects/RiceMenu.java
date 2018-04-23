package co.com.rices.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class RiceMenu implements Serializable{

	private static final long serialVersionUID = 899338187394996134L;
	
	private Integer id;
	private String  description;
	private Integer orden;
	private String  estado;
	private Date    open;
	private Date    closed;
	
	private List<Product> listProducts;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<Product> getListProducts() {
		return listProducts;
	}
	public void setListProducts(List<Product> listProducts) {
		this.listProducts = listProducts;
	}
	public Date getOpen() {
		return open;
	}
	public void setOpen(Date open) {
		this.open = open;
	}
	public Date getClosed() {
		return closed;
	}
	public void setClosed(Date closed) {
		this.closed = closed;
	}
	
	public RiceMenu clone(){
		RiceMenu clone = new RiceMenu();
		if(this.id!=null){
			clone.setId(new Integer(this.id));
		}
		if(StringUtils.trimToNull(this.description)!=null){
			clone.setDescription(new String(this.description));
		}
		if(this.orden!=null){
			clone.setOrden(new Integer(this.orden));
		}
		if(StringUtils.trimToNull(this.estado)!=null){
			clone.setEstado(new String(this.estado));
		}
		if(this.open!=null){
			clone.setOpen((Date) this.open.clone());
		}
		if(this.closed!=null){
			clone.setClosed((Date) this.closed.clone());
		}
		return clone;
	}

}
