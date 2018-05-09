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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.DAO.IUpdateRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.general.EmailSender;
import co.com.rices.objects.City;
import co.com.rices.objects.Complement;
import co.com.rices.objects.CouponCode;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.RiceMenu;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ArmarPedido extends ConsultarFuncionesAPI{

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
		
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		try{
			this.mostraMensajeError      = "display:none;";
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			this.showSeleccionarProducto = true;
			this.showRiceMenu            = true;
			this.tabActual               = 0;
			this.pedidoPersiste          = new Pedido();
			this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
			this.pedidoPersiste.setSubtotal(new BigDecimal(0));
			this.pedidoPersiste.setIva(new BigDecimal(0));
			this.pedidoPersiste.setTotal(new BigDecimal(0));
			this.pedidoPersiste.setEstado("R");
			this.pedidoPersiste.setTotalDescuento(new BigDecimal(0));
			//PRODUCTOS OFRECIDOS POR RICES
			if(sesion.getAttribute("RicesListProducts")==null){
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
				sesion.setAttribute("RicesListProducts", this.listaRiceMenu);
			}else{
				this.listaRiceMenu = (List<RiceMenu>) sesion.getAttribute("RicesListProducts");
			}
			
			this.detallePedido = new DetallePedido();


			//PRODUCTOS EN EL CARRITO
			if(sesion.getAttribute("RiceProductInCart")==null){
				this.listadoDetallePedido = new ArrayList<DetallePedido>();
				sesion.setAttribute("RiceProductInCart", this.listadoDetallePedido);
			}else{
				this.listadoDetallePedido = (List<DetallePedido>) sesion.getAttribute("RiceProductInCart");
				for(DetallePedido dp: this.listadoDetallePedido){
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().add(dp.getPrecio()));
					this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(dp.getPrecio()));
				}
			}
			
			if(sesion.getAttribute("RiceCities")==null){
				sesion.setAttribute("RiceCities", IQueryRices.getCities());
			}
			
			this.listCities = (List<City>) sesion.getAttribute("RiceCities");
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

	public void aplicarCupon(){
		try{
			this.mensajeError = "";
			boolean error = false;
			if(this.codigoCupon==null || this.codigoCupon.trim().equals("")){
				error = true;
				this.mensajeError = this.getMensaje("ingresaCupon");
				this.mostraMensajeError="display:;";
			}
			if(!error){
				CouponCode pCouponCode = new CouponCode();
				pCouponCode.setCoupon(this.codigoCupon);
				CouponCode cuponCliente = IQueryRices.getCouponCode(pCouponCode);
				if(cuponCliente==null){
					this.mensajeError = this.getMensaje("cuponNoValido");
					this.mostraMensajeError="display:;";
					error = true;
				}else if(cuponCliente.getUsed().equals("S")){
					this.mensajeError = this.getMensaje("cuponUtilizado");
					this.mostraMensajeError="display:;";
					error = true;
				}else{
					this.mensajeError = "";
					this.mostraMensajeError="display:none;";
					this.pedidoPersiste.setDescuento(cuponCliente.getPercentage());
					BigDecimal valorSobreCien = cuponCliente.getPercentage().divide(new BigDecimal(100));
					BigDecimal valorMultiplica = (new BigDecimal(1)).subtract(valorSobreCien);
					this.pedidoPersiste.setMultiplicador(valorMultiplica);
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(valorMultiplica));
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
					this.pedidoPersiste.setTransientCouponCode(cuponCliente);
				}
			}
			if(error){
				this.pedidoPersiste.setDescuento(null);
				this.pedidoPersiste.setMultiplicador(null);
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
				this.pedidoPersiste.setTransientCouponCode(null);
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
				}
				//REGISTRA EL PEDIDO
				Integer idPedido =IInsertRices.savePurchaseOrder(this.pedidoPersiste);
				if(idPedido!=null){
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
					//SI UTILIZÓ CUPON LO INHABILITA
					if(this.pedidoPersiste.getTransientCouponCode()!=null){
						this.pedidoPersiste.getTransientCouponCode().setUsed("S");
						IUpdateRices.updateCouponState(this.pedidoPersiste.getTransientCouponCode());
					}
				}else{
					exito = false;
					this.mostrarMensajeGlobal("noRegistraPedido", "error");
				}

				if(exito){
					this.pedidoPersiste.setCityName(this.mapCity.get(this.pedidoPersiste.getCodigoCiudad()));
					String[] val = {this.pedidoPersiste.getNombreCliente(), this.pedidoPersiste.getCelularCliente(), this.pedidoPersiste.getDireccionCliente(), this.pedidoPersiste.getCityName()};
					String[] sDestinatario = {"ricestogo@gmail.com","DIEGOVARGASLOPEZ@hotmail.com","faugust@hotmail.com"};
					EmailSender.sendEmail(this.getMensaje("asuntoPedido"), this.getMensajeParametros("cuerpoPedido", val), this.getMensaje("footer"), sDestinatario);
					FacesContext context = FacesContext.getCurrentInstance();
					HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
					sesion.removeAttribute("RiceProductInCart");
					this.showSeleccionarProducto = false;
					this.showPedidoRegistrado    = true;
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
