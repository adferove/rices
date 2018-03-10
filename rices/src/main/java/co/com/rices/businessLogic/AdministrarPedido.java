package co.com.rices.businessLogic;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;

@ManagedBean
@ViewScoped
public class AdministrarPedido extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 39571229398004080L;
	
	private List<Pedido> listadoPedido;
	private Map<Integer, Producto> productoActivo;
	
	@PostConstruct
	public void init(){
		try{
			this.productoActivo = IConsultaRices.getMapProductoActivo();
			this.listadoPedido = IConsultaRices.getPedidoPorEstado("R");
			for(Pedido p: this.listadoPedido){
				//ASOCIA AL CLIENTE
				p.setCliente(IConsultaRices.getClientesPorParametro(p.getIdcliente(), null).get(0));
				//ASOCIA EL DETALLE
				p.setListadoDetalle(IConsultaRices.getDetallePedidoPorId(p.getId()));
				for(DetallePedido d: p.getListadoDetalle()){
					d.setProducto(this.productoActivo.get(d.getIdproducto()));
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void actualizarEstado(Pedido pPedido){
		try{
			if(!pPedido.getEstado().equals("R")){
				if(IActualizaRices.actualizarEstadoPedido(pPedido)){
					this.listadoPedido.remove(pPedido);
					this.mostrarMensajeGlobal("listaPedidoActualizada", "exito");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public List<Pedido> getListadoPedido() {
		return listadoPedido;
	}

}
