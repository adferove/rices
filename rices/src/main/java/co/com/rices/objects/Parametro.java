package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;

public class Parametro implements Serializable{

	private static final long serialVersionUID = 6574212824949484907L;
	
	private String id;
	private BigDecimal valorNumerico;
	private String textCorto;
	private String textLargo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getValorNumerico() {
		return valorNumerico;
	}
	public void setValorNumerico(BigDecimal valorNumerico) {
		this.valorNumerico = valorNumerico;
	}
	public String getTextCorto() {
		return textCorto;
	}
	public void setTextCorto(String textCorto) {
		this.textCorto = textCorto;
	}
	public String getTextLargo() {
		return textLargo;
	}
	public void setTextLargo(String textLargo) {
		this.textLargo = textLargo;
	}

	
}
