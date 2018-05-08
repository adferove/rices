package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.City;
import co.com.rices.objects.Complement;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.RiceMenu;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ArmarPedidoAdmin extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 126945293261024517L;

	private boolean        showSeleccionarProducto;
	private boolean        showRiceMenu;
	private boolean        showCheckout;
	private boolean        showPedidoRegistrado;

	private int            tabActual;
	private String         codigoCupon;
	private String         mostraMensajeError;
	private String         mensajeError;

	private Pedido         pedidoPersiste;

	private Product        mainProductSelected;

	private DetallePedido  detallePedido;
	
	private Map<Integer, String> mapCity;

	private List<RiceMenu> listaRiceMenu;
			
	private List<DetallePedido> listadoDetallePedido;
	
	private List<City> listCities;
	
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
			builder.append(d.getMainProduct().getPrice());
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
			this.mostraMensajeError      = "display:none;";
			this.showSeleccionarProducto = true;
			this.showRiceMenu            = true;
			this.tabActual               = 0;
			this.pedidoPersiste          = new Pedido();
			this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
			this.pedidoPersiste.setSubtotal(new BigDecimal(0));
			this.pedidoPersiste.setIva(new BigDecimal(0));
			this.pedidoPersiste.setTotal(new BigDecimal(0));
			this.pedidoPersiste.setEstado("D");
			this.pedidoPersiste.setDescuento(new BigDecimal(0));
			this.pedidoPersiste.setTotalDescuento(new BigDecimal(0));
			//PRODUCTOS OFRECIDOS POR RICES
			RiceMenu pRiceMenu = new RiceMenu();
			pRiceMenu.setEstado("A");
			pRiceMenu.setOpen(Calendar.getInstance().getTime());
			pRiceMenu.setClosed(Calendar.getInstance().getTime());
			this.listaRiceMenu = IQueryRices.getRiceMenus(pRiceMenu);
			for(RiceMenu rm: this.listaRiceMenu){
				rm.setListProducts(IQueryRices.getProductsByMenu(rm.getId()));
				for(Product p: rm.getListProducts()){
					p.setListProductStep(IQueryRices.getProductStepsByProductId(p.getId(), true));
					for(ProductStep s: p.getListProductStep()){
						s.setTransientMainPoduct(p);
						s.setListStepDetail(IQueryRices.getDetailsByProductStepId(s.getId(), true));
						for(StepDetail d: s.getListStepDetail()){
							d.setTransientProducStep(s);
							d.setTransientProduct(new Product());
							d.getTransientProduct().setId(d.getSelectedProductId());
							d.getTransientProduct().setName(IQueryRices.getProductNameById(d.getSelectedProductId()));
						}
					}
				}	
			}
			this.detallePedido = new DetallePedido();
			//PRODUCTOS EN EL CARRITO
			this.listadoDetallePedido = new ArrayList<DetallePedido>();
			this.listCities = IQueryRices.getCities();
			this.mapCity = new HashMap<Integer,String>();
			for(City c: this.listCities){
				this.mapCity.put(c.getId(), c.getName());
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	/**
	 * PASO 1
	 */
	public void typeYourName(){
		if(StringUtils.trimToNull(this.pedidoPersiste.getNombreCliente())!=null){
			this.tabActual = 1;
			this.showRiceMenu = true;
		}else{
			this.mostrarMensajeGlobal("paraContinuarNombre", "advertencia");
		}
	}

	public void seleccionarComplemento(Product pProducto){
		this.showRiceMenu = false;
		this.mainProductSelected = pProducto;
		for(ProductStep s: this.mainProductSelected.getListProductStep()){
			for(StepDetail d: s.getListStepDetail()){
				d.setChecked(false);
			}
		}
		this.detallePedido = new DetallePedido();
		this.detallePedido.setMainProduct(this.mainProductSelected);
		this.detallePedido.setCantidad(1);
		this.detallePedido.setPrecio(this.mainProductSelected.getPrice());
	}

	public void seleccionUnicaListener(StepDetail pDetail){
		if(pDetail.getTransientProducStep().getSelectType().equals("U") && pDetail.isChecked()){
			for(StepDetail s: pDetail.getTransientProducStep().getListStepDetail()){
				if(s.getId().intValue()!=pDetail.getId().intValue()){
					s.setChecked(false);
				}
			}
		}
		this.calculateSubtotalDetail();
	}

	private void calculateSubtotalDetail(){
		BigDecimal singlePrice = new BigDecimal(0);
		for(ProductStep s: this.mainProductSelected.getListProductStep()){
			for(StepDetail d: s.getListStepDetail()){
				if(d.isChecked()){
					singlePrice = singlePrice.add(d.getPrice());
				}
			}
		}
		BigDecimal subtotal    = this.mainProductSelected.getPrice().add(singlePrice);
		BigDecimal quantity    = new BigDecimal(this.detallePedido.getCantidad());
		this.detallePedido.setPrecio(subtotal.multiply(quantity));
	}

	public void addProductToOrder(){
		DetallePedido detalleAgrega = new DetallePedido();
		detalleAgrega.setMainProduct(this.mainProductSelected.clone());
		detalleAgrega.setCantidad(new Integer(this.detallePedido.getCantidad()));
		detalleAgrega.setPrecio(this.detallePedido.getPrecio());
		detalleAgrega.getMainProduct().setListProductStep(new ArrayList<ProductStep>());
		for(ProductStep ps: this.mainProductSelected.getListProductStep()){
			ProductStep clone = ps.clone();
			clone.setListStepDetail(new ArrayList<StepDetail>());
			for(StepDetail sd: ps.getListStepDetail()){
				if(sd.isChecked()){
					clone.getListStepDetail().add(sd.clone());
				}
			}
			if(!clone.getListStepDetail().isEmpty()){
				detalleAgrega.getMainProduct().getListProductStep().add(clone);
			}
		}
		this.listadoDetallePedido.add(detalleAgrega);
		this.pedidoPersiste.setSubtotal(new BigDecimal(0));
		this.pedidoPersiste.setTotal(new BigDecimal(0));
		for(DetallePedido dp: this.listadoDetallePedido){
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().add(dp.getPrecio()));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(dp.getPrecio()));
		}
		this.showRiceMenu = true;
	}

	public void restarSumarCantidad(int pValor){
		if(pValor==0){
			if(this.detallePedido.getCantidad() > 1){
				this.detallePedido.setCantidad(this.detallePedido.getCantidad()-1);
				this.calculateSubtotalDetail();
			}
		}else{
			if(this.detallePedido.getCantidad() < 99){
				this.detallePedido.setCantidad(this.detallePedido.getCantidad()+1);
				this.calculateSubtotalDetail();
			}
		}
	}

	public void restarCantidad(DetallePedido pDetalle){
		if(pDetalle.getCantidad() > 1){
			BigDecimal valorUnidad = pDetalle.getPrecio().divide(new BigDecimal(pDetalle.getCantidad()));
			pDetalle.setCantidad(pDetalle.getCantidad()-1);
			pDetalle.setPrecio(pDetalle.getPrecio().subtract(valorUnidad));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().subtract(valorUnidad));
			if(this.pedidoPersiste.getMultiplicador()!=null){
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(this.pedidoPersiste.getMultiplicador()));
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
			}else{
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
			}
		}
	}

	public void sumarCantidad(DetallePedido pDetalle){
		if(pDetalle.getCantidad() < 99){
			BigDecimal valorUnidad = pDetalle.getPrecio().divide(new BigDecimal(pDetalle.getCantidad()));
			pDetalle.setCantidad(pDetalle.getCantidad()+1);
			pDetalle.setPrecio(pDetalle.getPrecio().add(valorUnidad));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(valorUnidad));
			if(this.pedidoPersiste.getMultiplicador()!=null){
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(this.pedidoPersiste.getMultiplicador()));
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
			}else{
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
			}
		}
	}

	public void quitarDetalle(DetallePedido pDetalle){
		this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().subtract(pDetalle.getPrecio()));
		if(this.pedidoPersiste.getMultiplicador()!=null){
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(this.pedidoPersiste.getMultiplicador()));
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
		}else{
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
		}
		this.listadoDetallePedido.remove(pDetalle);
	}

	public void validarProductoSeleccionado(){
		if(this.listadoDetallePedido.size()>0){
			this.tabActual = 2;
		}else{
			this.mostrarMensajeGlobal("seleccioneUnProducto", "advertencia");
		}
	}

	public void regresarSeleccionProducto(){
		this.tabActual = 1;
	}

	public void actualizarTotal(){
		try{
			if(this.pedidoPersiste.getDescuento()!=null && this.pedidoPersiste.getDescuento().compareTo(new BigDecimal(0))>0){
				BigDecimal valorSobreCien = this.pedidoPersiste.getDescuento().divide(new BigDecimal(100));
				BigDecimal valorMultiplica = (new BigDecimal(1)).subtract(valorSobreCien);
				this.pedidoPersiste.setMultiplicador(valorMultiplica);
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(valorMultiplica));
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
				this.pedidoPersiste.setTotalDescuento(this.pedidoPersiste.getSubtotal().subtract(this.pedidoPersiste.getTotal()));
			}else if(this.pedidoPersiste.getTotalDescuento()!=null && this.pedidoPersiste.getTotalDescuento().compareTo(new BigDecimal(0))>0){
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().subtract(this.pedidoPersiste.getTotalDescuento()));
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
				this.pedidoPersiste.setDescuento(new BigDecimal(0));
			}else{
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
				this.pedidoPersiste.setDescuento(new BigDecimal(0));
				this.pedidoPersiste.setTotalDescuento(new BigDecimal(0));
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void confirmarPedido(){
		boolean error = false;
		try{
			if(StringUtils.trimToNull(this.pedidoPersiste.getNombreCliente())==null){
				error = true;
				this.mostrarMensajeGlobal("ingresaNombre", "error");
			}
			if(StringUtils.trimToNull(this.pedidoPersiste.getCelularCliente())==null){
				error = true;
				this.mostrarMensajeGlobal("ingresaTelefono", "error");
			}
			if(StringUtils.trimToNull(this.pedidoPersiste.getDireccionCliente())==null){
				error = true;
				this.mostrarMensajeGlobal("ingresaDireccion", "error");
			}

			if(!error){
				boolean exito = true;
				if(this.pedidoPersiste.getDescuento()!=null && this.pedidoPersiste.getDescuento().compareTo(new BigDecimal(0))>0){
					this.pedidoPersiste.setTotalDescuento(this.pedidoPersiste.getSubtotal().subtract(this.pedidoPersiste.getTotal()));
				}else if(this.pedidoPersiste.getTotalDescuento()!=null && this.pedidoPersiste.getTotalDescuento().compareTo(new BigDecimal(0))>0){
					this.pedidoPersiste.setDescuento(new BigDecimal(0));
				}else{
					this.pedidoPersiste.setDescuento(new BigDecimal(0));
					this.pedidoPersiste.setTotalDescuento(new BigDecimal(0));
				}
				//REGISTRA EL PEDIDO
				Integer idPedido =IInsertRices.savePurchaseOrder(this.pedidoPersiste);
				if(idPedido!=null){
					this.pedidoPersiste.setId(idPedido);
					this.pedidoPersiste.setFecha(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
					this.pedidoPersiste.setHora(new java.sql.Time(Calendar.getInstance().getTime().getTime()));
					//REGISTRA DETALLE
					for(DetallePedido dp: this.listadoDetallePedido){
						dp.setIdpedido(idPedido);
						Integer idDetalle = IInsertRices.saveOrderDetail(dp); 
						if(idDetalle!=null){
							for(ProductStep ps: dp.getMainProduct().getListProductStep()){
								for(StepDetail sd: ps.getListStepDetail()){
									Complement pComplement = new Complement();
									pComplement.setDetailId(idDetalle);
									pComplement.setSelectedProductId(sd.getSelectedProductId());
									pComplement.setProductStepId(ps.getId());
									pComplement.setPrice(sd.getPrice());
									IInsertRices.saveComplement(pComplement);
								}
							}
						}
					}

				}else{
					exito = false;
					this.mostrarMensajeGlobal("noRegistraPedido", "error");
				}
				if(exito){
					this.pedidoPersiste.setCityName(this.mapCity.get(this.pedidoPersiste.getCodigoCiudad()));
					this.showSeleccionarProducto = false;
					this.showPedidoRegistrado    = true;
					this.pedidoPersiste.setListadoDetalle(this.listadoDetallePedido);
//					RequestContext.getCurrentInstance().execute(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(this.pedidoPersiste) + "\"");
//					RequestContext.getCurrentInstance().execute(" printPage('block1');");
					PrimeFaces.current().executeScript(" document.getElementById('block1').innerHTML=\"" + this.generarFactura(this.pedidoPersiste) + "\"");
					PrimeFaces.current().executeScript(" printPage('block1');");
				}else{
					this.mostrarMensajeGlobal("noRegistraDetalle", "advertencia");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void cancelarProducto(){
		this.showRiceMenu = true;
	}

	public boolean isShowSeleccionarProducto() {
		return showSeleccionarProducto;
	}

	public int getTabActual() {
		return tabActual;
	}

	public void setTabActual(int tabActual) {
		this.tabActual = tabActual;
	}

	public Pedido getPedidoPersiste() {
		return pedidoPersiste;
	}

	public void setPedidoPersiste(Pedido pedidoPersiste) {
		this.pedidoPersiste = pedidoPersiste;
	}

	public List<RiceMenu> getListaRiceMenu() {
		return listaRiceMenu;
	}

	public boolean isShowRiceMenu() {
		return showRiceMenu;
	}

	public Product getMainProductSelected() {
		return mainProductSelected;
	}

	public void setMainProductSelected(Product mainProductSelected) {
		this.mainProductSelected = mainProductSelected;
	}

	public String getCodigoCupon() {
		return codigoCupon;
	}

	public void setCodigoCupon(String codigoCupon) {
		this.codigoCupon = codigoCupon;
	}

	public boolean isShowCheckout() {
		return showCheckout;
	}

	public boolean isShowPedidoRegistrado() {
		return showPedidoRegistrado;
	}

	public String getMostraMensajeError() {
		return mostraMensajeError;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public DetallePedido getDetallePedido() {
		return detallePedido;
	}

	public List<DetallePedido> getListadoDetallePedido() {
		return listadoDetallePedido;
	}

	public List<City> getListCities() {
		return listCities;
	}

}
