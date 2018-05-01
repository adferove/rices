package co.com.rices.objects;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.IConstants;

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
	private Integer idMenu;
	private byte[]  image;
	private String  contentType;
	private String  mime;

	private List<ProductStep> listProductStep;

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
	public List<ProductStep> getListProductStep() {
		return listProductStep;
	}
	public void setListProductStep(List<ProductStep> listProductStep) {
		this.listProductStep = listProductStep;
	}
	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public byte[] getImage() {
		if(this.image==null){
			try {
				if(StringUtils.trimToNull(this.contentType)!=null){
					BufferedImage originalImage = ImageIO.read(new File(IConstants.PATH_DISK+this.getImageName()+"."+this.getMime()));
					// convert BufferedImage to byte array
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(originalImage, this.getMime(), baos);
					baos.flush();
					this.image = baos.toByteArray();
					baos.close();
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getMime() {
		this.mime = "";
		if(this.contentType.equals("image/png")){
			this.mime = "png";
		}else if(this.contentType.equals("image/gif")){
			this.mime = "gif";
		}else if(this.contentType.equals("image/jpeg")){
			this.mime = "jpeg";
		}else if(this.contentType.equals("image/jpg")){
			this.mime = "jpg";
		} 
		return mime;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	
	public Product clone(){
		Product product = new Product();
		product.setId(new Integer(this.id));
		if(this.closed!=null){
			product.setClosed((Date) this.closed.clone());
		}
		if(this.creationDate!=null){
			product.setCreationDate((Date) this.creationDate.clone());
		}
		if(StringUtils.trimToNull(this.description)!=null){
			product.setDescription(new String(this.description));
		}
		if(StringUtils.trimToNull(this.imageName)!=null){
			product.setImageName(new String(this.imageName));
		}
		if(StringUtils.trimToNull(this.loginUsuario)!=null){
			product.setLoginUsuario(new String(this.loginUsuario));
		}
		if(StringUtils.trimToNull(this.name)!=null){
			product.setName(new String(this.name));
		}
		if(this.open!=null){
			product.setOpen((Date) this.open.clone());
		}
		if(StringUtils.trimToNull(this.productType)!=null){
			product.setProductType(new String(this.productType));
		}
		if(this.ranking!=null){
			product.setRanking(new Integer(this.ranking));
		}
		if(StringUtils.trimToNull(this.state)!=null){
			product.setState(new String(this.state));
		}
		product.setPrice(new BigDecimal(0));
		if(this.price!=null){
			product.setPrice(product.getPrice().add(this.price));
		}
		if(this.idMenu!=null){
			product.setIdMenu(new Integer(this.idMenu));
		}
		return product;
	}


}
