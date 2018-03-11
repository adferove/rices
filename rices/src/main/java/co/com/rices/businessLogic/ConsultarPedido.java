package co.com.rices.businessLogic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;

@ManagedBean
@ViewScoped
public class ConsultarPedido extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 4754756531431040743L;
	
	private boolean showConsulta;
	private boolean showVerDetalle;
	
	private String estado;
	private Date   fechaDesde;
	private Date   fechaHasta;
	
	private Pedido pedidoSeleccionado;
	
	private Map<Integer, Producto> productoActivo;
	
	private List<Pedido> listadoPedido;
	
	
	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.productoActivo = IConsultaRices.getMapProductoTodos();
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void consultarPedido(){
		try{
			
			this.listadoPedido = null;
			boolean error = false;
			if(this.fechaDesde!=null && this.fechaHasta!=null){
				if(this.fechaDesde.compareTo(this.fechaHasta)>0){
					error = true;
					this.mostrarMensajeGlobal("desdeNoMayorHasta", "error");
				}
			}
			if(!error){
				this.listadoPedido = IConsultaRices.getPedidoPorParametros(this.estado, this.fechaDesde, this.fechaHasta);
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void verDetallePedido(Pedido pPedido){
		try{
			this.pedidoSeleccionado = pPedido;

			//ASOCIA AL CLIENTE
			this.pedidoSeleccionado.setCliente(IConsultaRices.getClientesPorParametro(this.pedidoSeleccionado.getIdcliente(), null).get(0));
			//ASOCIA EL DETALLE
			this.pedidoSeleccionado.setListadoDetalle(IConsultaRices.getDetallePedidoPorId(this.pedidoSeleccionado.getId()));
			for(DetallePedido d: this.pedidoSeleccionado.getListadoDetalle()){
				d.setProducto(this.productoActivo.get(d.getIdproducto()));
			}
			this.showConsulta = false;
			this.showVerDetalle = true;
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void regresar(){
		this.showVerDetalle = false;
		this.showConsulta = true;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public List<Pedido> getListadoPedido() {
		return listadoPedido;
	}

	public boolean isShowVerDetalle() {
		return showVerDetalle;
	}

	public Pedido getPedidoSeleccionado() {
		return pedidoSeleccionado;
	}

	
}
