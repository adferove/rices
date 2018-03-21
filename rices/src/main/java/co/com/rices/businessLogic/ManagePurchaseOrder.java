package co.com.rices.businessLogic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;

@ManagedBean
@ViewScoped
public class ManagePurchaseOrder extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 911365794720003052L;
	
	@PostConstruct
	public void init(){
		
	}

}
