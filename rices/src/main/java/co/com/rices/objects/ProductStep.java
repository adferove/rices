package co.com.rices.objects;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ProductStep implements Serializable{

	private static final long serialVersionUID = -6796250662297410741L;

	private Integer id;
	private Integer productId;
	private String  selectType;
	private String  state;
	private String  description;
	private Integer stepOrder;

	private List<StepDetail> listStepDetail;
	
	private Product transientMainPoduct;

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

	public List<StepDetail> getListStepDetail() {
		return listStepDetail;
	}
	public void setListStepDetail(List<StepDetail> listStepDetail) {
		this.listStepDetail = listStepDetail;
	}
	public Product getTransientMainPoduct() {
		return transientMainPoduct;
	}
	public void setTransientMainPoduct(Product transientMainPoduct) {
		this.transientMainPoduct = transientMainPoduct;
	}
	public ProductStep clone(){
		ProductStep productStep = new ProductStep();
		
		productStep.setId(new Integer(this.id));
		
		if(StringUtils.trimToNull(this.description)!=null){
			productStep.setDescription(new String(this.description));
		}
		if(this.getProductId()!=null){
			productStep.setProductId(new Integer(this.productId));
		}
		if(StringUtils.trimToNull(this.selectType)!=null){
			productStep.setSelectType(new String(this.selectType));
		}
		if(StringUtils.trimToNull(this.state)!=null){
			productStep.setState(new String(this.state));
		}
		if(this.stepOrder!=null){
			productStep.setStepOrder(new Integer(this.stepOrder));
		}
		return productStep;
	}

}
