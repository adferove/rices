package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Pedido;

@ManagedBean
@ViewScoped
public class AdministrarPedido extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 39571229398004080L;
	
	private List<Pedido> listadoPedido;
	
	@PostConstruct
	public void init(){
		try{
			this.listadoPedido = IConsultaRices.getPedidoPorEstado("R");
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public List<Pedido> getListadoPedido() {
		return listadoPedido;
	}

}
