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
import co.com.rices.objects.Product;

@ManagedBean
@ViewScoped
public class MenuNoonMoom implements Serializable{

	private static final long serialVersionUID = -6455045348536360298L;
	private static final String URI_TODO  = "/rices/menuRices.jsf";
	private static final String URI_NOON  = "/rices/noon.jsf";
	private static final String URI_MOON  = "/rices/moon.jsf";
	
	private List<Product> productList;
	
	@PostConstruct
	public void init(){
		try{
			this.productList = null;
			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (req != null){
				if(req.getRequestURI()!=null){
					if(req.getRequestURI().trim().contains(URI_TODO)){
						this.productList = IQueryRices.getProductsNoonMoon(null);
					}else if(req.getRequestURI().trim().contains(URI_NOON)){
						this.productList = IQueryRices.getProductsNoonMoon("N");
					}else if(req.getRequestURI().trim().contains(URI_MOON)){
						this.productList = IQueryRices.getProductsNoonMoon("M");
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

}
