package co.com.rices.objects;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

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

	public Parametro clone(){
		Parametro parametro = new Parametro();
		if(StringUtils.trimToNull(this.id)!=null){
			parametro.setId(new String(this.id));
		}
		if(StringUtils.trimToNull(this.textCorto)!=null){
			parametro.setTextCorto(new String(this.textCorto));
		}
		if(StringUtils.trimToNull(this.textLargo)!=null){
			parametro.setTextLargo(new String(this.textLargo));
		}
		if(this.valorNumerico!=null){
			parametro.setValorNumerico(this.valorNumerico);
		}
		return parametro;
	}
}
