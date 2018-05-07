package co.com.rices.businessLogic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

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
	
	private String generarFactura(Pedido pPedido){
		StringBuilder builder = new StringBuilder();

		builder.append(" <div style='font-family: Courier new; font-size: 10px;'> ");
		builder.append(" 	<div id='logo'> ");
		builder.append(" 		<h3> ");
		builder.append(" 			<span style='font-size: 12px;'> RICES TO GO</span> ");
		builder.append(" 		</h3> ");
		builder.append(" 	</div> ");
		builder.append(" 	<div id='company'> ");
		builder.append(" 		<div> ");
		builder.append(" 			<span style='font-size: 12px;'>Cll 14 Kra 27,</span> ");
		builder.append(" 			<br />  ");
		builder.append(" 			<span style='font-size: 12px;'>B/ga, Santander</span> ");
		builder.append(" 		</div> ");
		builder.append(" 		<div> ");
		builder.append(" 			<span style='font-size: 12px;'>3016912772</span> ");
		builder.append(" 		</div> ");
		builder.append(" 		<div> ");
		builder.append(" 			<span style='font-size: 12px;'>info@ricestogo.com</span> ");
		builder.append(" 		</div> ");
		builder.append(" 	</div> ");
		builder.append(" 	<h3> ");
		builder.append(" 		<span style='font-size: 12px;'>ORDEN # ");
		builder.append(pPedido.getId());
		builder.append("        </span> ");
		builder.append(" 	</h3> ");
		builder.append(" 	<table id='idCliente' role='grid'> ");
		builder.append(" 		<tbody> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>CLIENTE:</span></label></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'> ");
		builder.append(" 					<div align='left'> ");
		builder.append(" 						<span style='font-size: 12px; font-weight: bold;'>");
		builder.append(pPedido.getCliente().getNombre());
		builder.append("                        </span> ");
		builder.append(" 					</div> ");
		builder.append(" 				</td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>DIRECCION:</span></label></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><div align='left'> ");
		builder.append(" 						<span style='font-size: 12px; font-weight: bold;'>");
		builder.append(pPedido.getCliente().getDireccion()+", "+pPedido.getCliente().getBarrio());
		builder.append("                        </span> ");
		builder.append(" 					</div></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>FECHA:</span></label></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><div align='left'> ");
		builder.append(" 						<span style='font-size: 12px; font-weight: bold'>");
		builder.append(this.fechaCorta(pPedido.getFecha())+" "+this.horaCorta(pPedido.getHora()));
		builder.append("                        </span>");
		builder.append(" 					</div></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>Tel/celular:</span></label></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 			<tr role='row'> ");
		builder.append(" 				<td role='gridcell'><div align='left'> ");
		builder.append(" 						<span style='font-size: 12px; font-weight: bold'>");
		builder.append(pPedido.getCliente().getCelular());
		builder.append("                        </span> ");		
		builder.append(" 					</div></td> ");
		builder.append(" 			</tr> ");
		builder.append(" 		</tbody> ");
		builder.append(" 	</table> ");
		builder.append(" <div style='height: 10px;'></div> ");
		builder.append(" <table> ");
		builder.append(" 	<thead> ");
		builder.append(" 		<tr> ");
		builder.append(" 			<th><div align='left'> ");
		builder.append(" 					<span style='font-size: 12px;'>ITEM</span> ");
		builder.append(" 				</div></th> ");
		builder.append(" 			<th><div align='center'> ");
		builder.append(" 					<span style='font-size: 12px;'>CANTIDAD</span> ");
		builder.append(" 				</div></th> ");
		builder.append(" 			<th><div align='right'> ");
		builder.append(" 					<span style='font-size: 12px;'>VALOR</span> ");
		builder.append(" 				</div></th> ");
		builder.append(" 		</tr> ");
		builder.append(" 	</thead> ");
		builder.append(" 	<tbody> ");
		for(DetallePedido d: pPedido.getListadoDetalle()){
		builder.append(" 		<tr> ");
		builder.append(" 			<td><div align='right'>");
		builder.append(" 					<span style='font-size: 12px;'>");
		builder.append(d.getProducto().getNombre());
		builder.append("                    </span> ");
		builder.append(" 				</div></td> ");
		builder.append(" 			<td><div align='center'> ");
		builder.append(" 					<span style='font-size: 12px;'>");
		builder.append(d.getCantidad());
		builder.append("                    </span> ");
		builder.append(" 				</div></td> ");
		builder.append(" 			<td><div align='right'> ");
		builder.append(" 					<span style='font-size: 12px;'>$");
		builder.append(d.getPrecio());
		builder.append("                    </span> ");
		builder.append(" 				</div></td> ");
		builder.append(" 		</tr> ");
		}		
		builder.append(" 	</tbody> ");
		builder.append(" </table> ");
		builder.append(" <div style='height: 20px;'></div> ");
		builder.append(" <table id='idTotales' role='grid'> ");
		builder.append(" 	<tbody> ");
		builder.append(" 		<tr role='row'> ");
		builder.append(" 			<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>SUBTOTAL:</span></label></td> ");
		builder.append(" 			<td role='gridcell'> ");
		builder.append(" 				<div align='right'> ");
		builder.append(" 					<span style='font-size: 12px; font-weight: bold;'>$");
		builder.append(pPedido.getSubtotal());
		builder.append("                    </span> ");
		builder.append(" 				</div> ");
		builder.append(" 			</td> ");
		builder.append(" 		</tr> ");
		builder.append(" 		<tr role='row'> ");
		builder.append(" 			<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>DESCUENTO:</span></label></td> ");
		builder.append(" 			<td role='gridcell'><div align='right'> ");
		builder.append(" 					<span style='font-size: 12px; font-weight: bold;'>");
		if(pPedido.getDescuento()!=null){
			builder.append(pPedido.getDescuento());
			builder.append("                    %</span> ");
		}else{
			builder.append("                    </span> ");
		}
		builder.append(" 				</div></td> ");
		builder.append(" 		</tr> ");
		builder.append(" 		<tr role='row'> ");
		builder.append(" 			<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>TOTAL:</span></label></td> ");
		builder.append(" 			<td role='gridcell'><div align='right'> ");
		builder.append(" 					<span style='font-size: 12px; font-weight: bold'>$");
		builder.append(pPedido.getTotal());
		builder.append("                     </span> ");
		builder.append(" 				</div></td> ");
		builder.append(" 		</tr> ");
		builder.append(" 	</tbody> ");
		builder.append(" </table> ");
		builder.append(" <div style='height: 10px;'></div> ");
		builder.append(" <footer> ");
		builder.append(" 	<span style='font-size: 11px;'>Desarrollado por black vulture IT solutions. </span> ");
		builder.append(" </footer> ");
		builder.append(" </div> ");

		return builder.toString();
	}
	
	
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
	
	public void verFactura(){
		RequestContext.getCurrentInstance().execute(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(this.pedidoSeleccionado) + "\"");
		RequestContext.getCurrentInstance().execute(" printPage('block1');");
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
