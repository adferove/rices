package co.com.rices.businessLogic;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

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
		builder.append(" 			<span style='font-size: 12px;'>Cabecera,</span> ");
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
		builder.append(" 					<span style='font-size: 12px; font-weight: bold;'>$");
		if(pPedido.getTotalDescuento()!=null){
			builder.append(pPedido.getTotalDescuento());
			builder.append("                    </span> ");
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
			this.productoActivo = IConsultaRices.getMapProductoTodos();
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
					if(pPedido.getEstado().equals("D")){
//						RequestContext.getCurrentInstance().execute(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(pPedido) + "\"");
//						RequestContext.getCurrentInstance().execute(" printPage('block1');");
						PrimeFaces.current().executeScript(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(pPedido) + "\"");
						PrimeFaces.current().executeScript(" printPage('block1');");
						
					}
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
