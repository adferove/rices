package co.com.rices.businessLogic;

import java.io.Serializable;

public class ProductStep implements Serializable{

	private static final long serialVersionUID = -6796250662297410741L;

	private Integer id;
	private Integer productId;
	private String  selectType;
	private String  state;
	private String  description;
	private Integer stepOrder;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getSelectType() {
		return selectType;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStepOrder() {
		return stepOrder;
	}
	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}

	public ProductStep clone(){
		ProductStep productStep = new ProductStep();
		productStep.setDescription(new String(this.description));
		productStep.setId(new Integer(this.id));
		productStep.setProductId(new Integer(this.productId));
		productStep.setSelectType(new String(this.selectType));
		productStep.setState(new String(this.state));
		if(this.stepOrder!=null){
			productStep.setStepOrder(new Integer(this.stepOrder));
		}
		return productStep;
	}

}
