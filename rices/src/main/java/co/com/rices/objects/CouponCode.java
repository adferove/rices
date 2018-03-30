package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class CouponCode implements Serializable{

	private static final long serialVersionUID = 8653768297323293607L;
	
	private Integer    clientId;
	private String     coupon;
	private String     used;
	private BigDecimal percentage;
	
	
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	
}
