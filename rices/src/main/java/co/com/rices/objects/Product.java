package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Product implements Serializable{

	private static final long serialVersionUID = -3215594742117719705L;

	private Integer id;
	private String  name;
	private String  description;
	private Date    creationDate;
	private String  state;
	private String  loginUsuario;
	private Integer ranking;
	private String  imageName;
	private String  productType;
	private Date    open;
	private Date    closed;
	private BigDecimal price;


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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
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

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Product clone(){
		Product product = new Product();
		product.setId(new Integer(this.id));
		if(this.closed!=null){
			product.setClosed((Date) this.closed.clone());
		}
		product.setCreationDate((Date) this.creationDate.clone());
		product.setDescription(new String(this.description));
		product.setImageName(new String(this.imageName));
		product.setLoginUsuario(new String(this.loginUsuario));
		product.setName(new String(this.name));
		if(this.open!=null){
			product.setOpen((Date) this.open.clone());
		}
		product.setProductType(new String(this.productType));
		product.setRanking(new Integer(this.ranking));
		product.setState(new String(this.state));
		product.setPrice(new BigDecimal(0));
		if(this.price!=null){
			product.setPrice(product.getPrice().add(this.price));
		}
		return product;
	}

}
