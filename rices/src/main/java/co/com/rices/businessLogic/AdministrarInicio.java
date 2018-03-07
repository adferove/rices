package co.com.rices.businessLogic;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;

@ManagedBean
@ViewScoped
public class AdministrarInicio extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -247498233455725789L;
	
	@PostConstruct
	public void init(){
		
	}

	public void mostrarModalDescuento(){
		this.abrirModal("mdlDescuento");
	}
}
