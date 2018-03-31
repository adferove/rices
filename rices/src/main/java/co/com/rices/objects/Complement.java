package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Complement implements Serializable{

	private static final long serialVersionUID = -5053707129740351391L;
	
	private Integer detailId;
	private Integer selectedProductId;
	private Integer productStepId;
	private BigDecimal price;
	
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public Integer getSelectedProductId() {
		return selectedProductId;
	}
	public void setSelectedProductId(Integer selectedProductId) {
		this.selectedProductId = selectedProductId;
	}
	public Integer getProductStepId() {
		return productStepId;
	}
	public void setProductStepId(Integer productStepId) {
		this.productStepId = productStepId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	

}
