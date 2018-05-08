package co.com.rices.businessLogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.City;
import co.com.rices.objects.Complement;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ManageRegisteredOrder extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -5176873363876048439L;
	
	private static final String REGISTRADAS = "/pages/pedidos/registeredOrder.jsf";
	private static final String CONFIRMADAS = "/pages/pedidos/confirmedOrder.jsf";
	
	private String parametroConsulta;
	
	private boolean showConsulta;
	private boolean showVerDetalle;
	
	private String estado;
	private Date   fechaDesde;
	private Date   fechaHasta;
	
	private Pedido pedidoSeleccionado;

	private List<Pedido> listadoPedido;
	
	private Map<Integer, String> mapCities;
	private Map<Integer, Product> mapProducts;
	private Map<Integer, List<ProductStep>> mapProductStep;
	
	private String generarFactura(Pedido pPedido){
		StringBuilder builder = new StringBuilder();
		builder.append(" <div style='font-family: Courier new; font-size: 10px;'> ");
		builder.append(" 	<div class='logo' style='margin-left: 40px;'>                       ");
		builder.append("        <img src='http://ricestogo.com/rices/images/logo.png' alt='' /> ");
		builder.append(" 	</div>                                   ");
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
		builder.append(pPedido.getNombreCliente());
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
		builder.append(pPedido.getDireccionCliente()+", "+pPedido.getCityName());
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
		builder.append(pPedido.getCelularCliente());
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
		builder.append(" 					<span style='font-size: 12px;'>VLR/UND</span> ");
		builder.append(" 				</div></th> ");
		builder.append(" 		</tr> ");
		builder.append(" 	</thead> ");
		builder.append(" 	<tbody> ");
		for(DetallePedido d: pPedido.getListadoDetalle()){
			builder.append(" 		<tr> ");
			builder.append(" 			<td><div align='left'>");
			builder.append(" 					<span style='font-size: 11px; font-weight: bold'>");
			builder.append(d.getMainProduct().getName());
			builder.append("                    </span> ");
			builder.append(" 				</div></td> ");
			builder.append(" 			<td><div align='center'> ");
			builder.append(" 					<span style='font-size: 11px;'>");
			builder.append(d.getCantidad());
			builder.append("                    </span> ");
			builder.append(" 				</div></td> ");
			builder.append(" 			<td><div align='right'> ");
			builder.append(" 					<span style='font-size: 11px;'>$");
			builder.append(d.getProductPrice());
			builder.append("                    </span> ");
			builder.append(" 				</div></td> ");
			builder.append(" 		</tr> ");
			for(ProductStep ps: d.getMainProduct().getListProductStep()){
				if(ps.getListStepDetail()!=null && ps.getListStepDetail().size()>0){
					builder.append(" 		<tr> ");
					builder.append(" 			<td><div align='left'>");
					builder.append(" 					<span style='font-size: 11px;'>");
					builder.append(ps.getDescription());
					builder.append("                    </span> ");
					builder.append(" 				</div></td> ");
					builder.append(" 		</tr> ");
					for(StepDetail sd: ps.getListStepDetail()){
						builder.append(" 		<tr> ");
						builder.append(" 			<td><div align='left'>");
						builder.append(" 					<span style='font-size: 11px;'>");
						builder.append(sd.getTransientProduct().getName());
						builder.append("                    </span> ");
						builder.append(" 				</div></td> ");
						builder.append(" 			<td><div align='center'> ");
						builder.append(" 					<span style='font-size: 11px;'>");
						builder.append(d.getCantidad());
						builder.append("                    </span> ");
						builder.append(" 				</div></td> ");
						builder.append(" 			<td><div align='right'> ");
						builder.append(" 					<span style='font-size: 11px;'>$");
						builder.append(sd.getPrice());
						builder.append("                    </span> ");
						builder.append(" 				</div></td> ");
						builder.append(" 		</tr> ");
					}
				}
			}
			builder.append(" 		<tr> ");
			builder.append(" 			<td><div align='left'>");
			builder.append(" 					<span style='font-size: 12px; font-weight: bold'>");
			builder.append("Subtotal");
			builder.append("                    </span> ");
			builder.append(" 				</div></td> ");
			builder.append(" 			<td><div align='center'> ");
			builder.append(" 					<span style='font-size: 12px;'>");
			builder.append("");
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
		builder.append(" 			<td role='gridcell'><label id='form:j_idt42'><span style='font-size: 12px;'>GRAN TOTAL:</span></label></td> ");
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

			HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			if (req != null && req.getRequestURI()!= null){

				this.parametroConsulta = "";

				this.mapCities = new HashMap<Integer,String>();
				List<City> cities = IQueryRices.getCities();
				for(City city: cities){
					this.mapCities.put(city.getId(), city.getName());
				}

				this.mapProducts = new HashMap<Integer,Product>();
				List<Product> products = IQueryRices.getProductsByParams(null);
				for(Product product: products){
					this.mapProducts.put(product.getId(), product);
				}
				this.mapProductStep = new HashMap<Integer, List<ProductStep>>();
				
				if(req.getRequestURI().contains(REGISTRADAS)){
					this.parametroConsulta = "R";
				}else if(req.getRequestURI().contains(CONFIRMADAS)){
					this.parametroConsulta = "A";
				}

				if(this.parametroConsulta.equals("A") || this.parametroConsulta.equals("R")){

					this.listadoPedido = IConsultaRices.getPedidoPorEstado(this.parametroConsulta);

					for(Pedido p: this.listadoPedido){
						p.setCityName(this.mapCities.get(p.getCodigoCiudad()));
						p.setListadoDetalle(IConsultaRices.getDetallePedidoPorId(p.getId()));
						for(DetallePedido d: p.getListadoDetalle()){
							d.setMainProduct(this.mapProducts.get(d.getIdproducto()));
							if(this.mapProductStep.get(d.getMainProduct().getId().intValue())==null){
								this.mapProductStep.put(d.getMainProduct().getId().intValue(), IQueryRices.getProductStepsByProductId(d.getMainProduct().getId(), false));
							}
							d.getMainProduct().setListProductStep(this.mapProductStep.get(d.getMainProduct().getId().intValue()));
							for(ProductStep ps: d.getMainProduct().getListProductStep()){
								Complement pComplement = new Complement();
								pComplement.setProductStepId(ps.getId());
								pComplement.setDetailId(d.getId());
								List<Complement> complements = IQueryRices.getComplementsByDetail(pComplement);
								ps.setListStepDetail(null);
								if(complements.size()>0){
									ps.setListStepDetail(new ArrayList<StepDetail>());
									for(Complement complement: complements){
										StepDetail stepDetail = new StepDetail();
										stepDetail.setPrice(complement.getPrice());
										stepDetail.setTransientProduct(this.mapProducts.get(complement.getSelectedProductId()));
										ps.getListStepDetail().add(stepDetail);
									}
								}
							}
						}
					}
				}else{
					this.listadoPedido = IConsultaRices.getPedidoPorParametros(this.estado, this.fechaDesde, this.fechaHasta);
					this.showConsulta = true;
				}
			}

		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	
	public void actualizarEstado(Pedido pPedido){
		try{
			if(this.parametroConsulta.equals("R")){
				if(!pPedido.getEstado().equals("R")){
					if(IActualizaRices.actualizarEstadoPedido(pPedido)){
						this.listadoPedido.remove(pPedido);
						this.mostrarMensajeGlobal("listaPedidoActualizada", "exito");
					}
				}
			}else if(this.parametroConsulta.equals("A")){
				if(!pPedido.getEstado().equals("A")){
					if(IActualizaRices.actualizarEstadoPedido(pPedido)){
						this.listadoPedido.remove(pPedido);
						this.mostrarMensajeGlobal("listaPedidoActualizada", "exito");
						if(pPedido.getEstado().equals("D")){
//							RequestContext.getCurrentInstance().execute(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(pPedido) + "\"");
//							RequestContext.getCurrentInstance().execute(" printPage('block1');");
							PrimeFaces.current().executeScript("document.getElementById('block1').innerHTML=\"" + this.generarFactura(pPedido) + "\"");
							PrimeFaces.current().executeScript(" printPage('block1');");
						}
					}
				}
			}
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
			this.pedidoSeleccionado.setCityName(this.mapCities.get(this.pedidoSeleccionado.getCodigoCiudad()));
			//ASOCIA EL DETALLE
			this.pedidoSeleccionado.setListadoDetalle(IConsultaRices.getDetallePedidoPorId(this.pedidoSeleccionado.getId()));
			for(DetallePedido d: this.pedidoSeleccionado.getListadoDetalle()){
				d.setMainProduct(this.mapProducts.get(d.getIdproducto()));
				if(this.mapProductStep.get(d.getMainProduct().getId().intValue())==null){
					this.mapProductStep.put(d.getMainProduct().getId().intValue(), IQueryRices.getProductStepsByProductId(d.getMainProduct().getId(), false));
				}
				d.getMainProduct().setListProductStep(this.mapProductStep.get(d.getMainProduct().getId().intValue()));
				for(ProductStep ps: d.getMainProduct().getListProductStep()){
					Complement pComplement = new Complement();
					pComplement.setProductStepId(ps.getId());
					pComplement.setDetailId(d.getId());
					ps.setListStepDetail(null);
					List<Complement> complements = IQueryRices.getComplementsByDetail(pComplement);
					if(complements.size()>0){
						ps.setListStepDetail(new ArrayList<StepDetail>());
						for(Complement complement: complements){
							StepDetail stepDetail = new StepDetail();
							stepDetail.setPrice(complement.getPrice());
							stepDetail.setTransientProduct(this.mapProducts.get(complement.getSelectedProductId()));
							ps.getListStepDetail().add(stepDetail);
						}
					}
				}
			}
			this.showConsulta = false;
			this.showVerDetalle = true;
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void verFactura(){
//		RequestContext.getCurrentInstance().execute(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(this.pedidoSeleccionado) + "\"");
//		RequestContext.getCurrentInstance().execute(" printPage('block1');");
		PrimeFaces.current().executeScript("document.getElementById('block1').innerHTML=\"" + this.generarFactura(this.pedidoSeleccionado) + "\"");
		PrimeFaces.current().executeScript(" printPage('block1');");
	}
	
	public void regresar(){
		this.showVerDetalle = false;
		this.showConsulta = true;
	}


	public List<Pedido> getListadoPedido() {
		return listadoPedido;
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

	public Pedido getPedidoSeleccionado() {
		return pedidoSeleccionado;
	}

	public void setPedidoSeleccionado(Pedido pedidoSeleccionado) {
		this.pedidoSeleccionado = pedidoSeleccionado;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public boolean isShowVerDetalle() {
		return showVerDetalle;
	}
	
	
}
