package co.com.rices.businessLogic;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import co.com.rices.IConstants;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.objects.Parametro;
import co.com.rices.objects.Product;

@ManagedBean
@ViewScoped
public class MenuNoonMoom implements Serializable{

	private static final long serialVersionUID = -6455045348536360298L;
	private static final String URI_TODO    = "/rices/menuRices.jsf";
	private static final String URI_NOON    = "/rices/noon.jsf";
	private static final String URI_MOON    = "/rices/moon.jsf";
	private static final String URI_POLICY  = "/rices/privacyPolicy.jsf";
	private static final String URI_DATA    = "/rices/privacyData.jsf";
	
	private String textoMoon;
	private String textoNoon;
	private String texto;
	private List<Product> productList;
	
	@PostConstruct
	public void init(){
		try{
			List<Parametro> parametros = null;
			this.productList = null;
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (req != null){
				if(req.getRequestURI()!=null){
					if(req.getRequestURI().trim().contains(URI_TODO)){
						this.productList = IQueryRices.getProductsNoonMoon(null);
						if(this.textoMoon==null){
							parametros = IQueryRices.getParametrosById(IConstants.TX_MOON);
							if(parametros.size()>0){
								this.textoMoon = parametros.get(0).getTextLargo();
							}
						}
						if(this.textoNoon==null){
							parametros = IQueryRices.getParametrosById(IConstants.TX_NOON);
							if(parametros.size()>0){
								this.textoNoon = parametros.get(0).getTextLargo();;
							}
						}
					}else if(req.getRequestURI().trim().contains(URI_NOON)){
						this.productList = IQueryRices.getProductsNoonMoon("N");
						if(this.textoNoon==null){
							parametros = IQueryRices.getParametrosById(IConstants.TX_NOON);
							if(parametros.size()>0){
								this.textoNoon = parametros.get(0).getTextLargo();;
							}
						}
					}else if(req.getRequestURI().trim().contains(URI_MOON)){
						this.productList = IQueryRices.getProductsNoonMoon("M");
						if(this.textoMoon==null){
							parametros = IQueryRices.getParametrosById(IConstants.TX_MOON);
							if(parametros.size()>0){
								this.textoMoon = parametros.get(0).getTextLargo();
							}
						}
					}else if(req.getRequestURI().trim().contains(URI_POLICY)){
						parametros = IQueryRices.getParametrosById(IConstants.TX_PR_PO);
						if(parametros.size()>0){
							this.texto = parametros.get(0).getTextLargo();
						}
					}else if(req.getRequestURI().trim().contains(URI_DATA)){
						parametros = IQueryRices.getParametrosById(IConstants.TX_PR_DA);
						if(parametros.size()>0){
							this.texto = parametros.get(0).getTextLargo();
						}
					}
				}
			}	
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public List<Product> getProductList() {
		return productList;
	}

	public String getTextoMoon() {
		return textoMoon;
	}

	public String getTextoNoon() {
		return textoNoon;
	}

	public String getTexto() {
		return texto;
	}


}
