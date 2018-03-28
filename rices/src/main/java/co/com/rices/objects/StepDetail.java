package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

public class StepDetail implements Serializable{

	private static final long serialVersionUID = 4456437400187192607L;

	private Integer    id;
	private Integer    productStepId;
	private Integer    selectedProductId;
	private String     state;
	private BigDecimal price;
	
	private boolean    checked;

	private Product    transientProduct;
	
	private ProductStep transientProducStep;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductStepId() {
		return productStepId;
	}

	public void setProductStepId(Integer productStepId) {
		this.productStepId = productStepId;
	}

	public Integer getSelectedProductId() {
		return selectedProductId;
	}

	public void setSelectedProductId(Integer selectedProductId) {
		this.selectedProductId = selectedProductId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Product getTransientProduct() {
		return transientProduct;
	}

	public void setTransientProduct(Product transientProduct) {
		this.transientProduct = transientProduct;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public ProductStep getTransientProducStep() {
		return transientProducStep;
	}

	public void setTransientProducStep(ProductStep transientProducStep) {
		this.transientProducStep = transientProducStep;
	}

	public StepDetail clone(){
		StepDetail stepDetail = new StepDetail();
		stepDetail.setId(new Integer(this.id));
		if(this.productStepId!=null){
			stepDetail.setProductStepId(new Integer(this.productStepId));
		}
		if(this.selectedProductId!=null){
			stepDetail.setSelectedProductId(new Integer(this.selectedProductId));
		}
		if(StringUtils.trimToNull(this.state)!=null){
			stepDetail.setState(new String(this.state));
		}
		stepDetail.setPrice(new BigDecimal(0));
		if(this.price!=null){
			stepDetail.setPrice(stepDetail.getPrice().add(this.price));
		}
		if(this.transientProduct!=null){
			stepDetail.setTransientProduct(this.transientProduct.clone());
		}
		return stepDetail;
	}
}
