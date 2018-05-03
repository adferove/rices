package co.com.rices.businessLogic;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;

@ManagedBean
@ViewScoped
public class ManageParametro extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -8267282785049423441L;
	
	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;

}
