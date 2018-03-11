package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.CuponCliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;
import co.com.rices.general.RicesTools;

@ManagedBean
@ViewScoped
public class AdministrarOrdenCompra extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -8483194343442440237L;

	private boolean        showSeleccionarProducto;
	private boolean        showCheckout;
	private boolean        showPedidoRegistrado;
	private boolean        pedidoRegistrado;
	
	private String         codigoCupon;
	private String         mostraMensajeError;
	private String         mensajeError;
	

	private Pedido         pedidoPersiste;
	private DetallePedido  detallePedido;
	
	private Map<String, List<Cliente>> mapClienteByMail;
	private Map<String, CuponCliente> mapCuponByIdCliente;

	private List<Producto> listadoProducto;
	private List<DetallePedido> listadoDetallePedido;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		try{
			this.mapCuponByIdCliente     = new HashMap<String, CuponCliente>();
			this.mapClienteByMail        = new HashMap<String, List<Cliente>>();
			this.mostraMensajeError      = "display:none;";
			this.pedidoRegistrado        = false;
			this.showSeleccionarProducto = true;
			this.pedidoPersiste          = new Pedido();
			this.pedidoPersiste.setCliente(new Cliente());
			this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
			this.pedidoPersiste.setSubtotal(new BigDecimal(0));
			this.pedidoPersiste.setIva(new BigDecimal(0));
			this.pedidoPersiste.setTotal(new BigDecimal(0));
			this.pedidoPersiste.setEstado("R");
			this.detallePedido = new DetallePedido();
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			
			//PRODUCTOS OFRECIDOS POR RICES
			if(sesion.getAttribute("RicesProducts")==null){
				this.listadoProducto = IConsultaRices.getProductoActivo();	
				sesion.setAttribute("RicesProducts", this.listadoProducto);
			}else{
				this.listadoProducto = (List<Producto>) sesion.getAttribute("RicesProducts");
			}

			//PRODUCTOS EN EL CARRITO
			if(sesion.getAttribute("RicesProductsInCart")==null){
				this.listadoDetallePedido = new ArrayList<DetallePedido>();
				sesion.setAttribute("RicesProductsInCart", this.listadoDetallePedido);
			}else{
				this.listadoDetallePedido = (List<DetallePedido>) sesion.getAttribute("RicesProductsInCart");
				for(DetallePedido dp: this.listadoDetallePedido){
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().add(dp.getPrecio()));
					this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(dp.getPrecio()));
				}
			}

		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void agregarProductoOrden(){
		this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(this.detallePedido.getPrecio()));
		if(this.pedidoPersiste.getMultiplicador()!=null){
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(this.pedidoPersiste.getMultiplicador()));
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
		}else{
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
		}
		
		//SE VALIDA QUE EL PRODUCTO NO SE ENCUENTRE EN LA LISTA Y SI ESTÁ USA CANTIDAD Y PRECIO
		boolean esta = false;
		for(DetallePedido dp: this.listadoDetallePedido){
			if(dp.getProducto().equals(this.detallePedido.getProducto())){
				esta = true;
				dp.setCantidad(dp.getCantidad()+this.detallePedido.getCantidad());
				dp.setPrecio(dp.getPrecio().add(this.detallePedido.getPrecio()));
				break;
			}
		}
		if(!esta){
			this.listadoDetallePedido.add(this.detallePedido);
		}
		this.cerrarModal("productDialog");
	}

	public void seleccionarCantidad(Producto pProducto){
		this.detallePedido = new DetallePedido();
		this.detallePedido.setProducto(pProducto);
		this.detallePedido.setCantidad(1);
		this.detallePedido.setPrecio(pProducto.getProductoPrecio().getPrecio());
		this.abrirModal("productDialog");
	}

	/**
	 * Resta o Suma la cantidad del producto.
	 *  0 para Resta
	 *  1 para Suma
	 * @param pValor
	 */
	public void restarSumarCantidad(int pValor){
		if(pValor==0){
			if(this.detallePedido.getCantidad() > 1){
				this.detallePedido.setCantidad(this.detallePedido.getCantidad()-1);
				this.detallePedido.setPrecio(this.detallePedido.getPrecio().subtract(this.detallePedido.getProducto().getProductoPrecio().getPrecio()));
			}
		}else{
			if(this.detallePedido.getCantidad() < 99){
				this.detallePedido.setCantidad(this.detallePedido.getCantidad()+1);
				this.detallePedido.setPrecio(this.detallePedido.getPrecio().add(this.detallePedido.getProducto().getProductoPrecio().getPrecio()));
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

	public void restarCantidad(DetallePedido pDetalle){
		if(pDetalle.getCantidad() > 1){
			pDetalle.setCantidad(pDetalle.getCantidad()-1);
			pDetalle.setPrecio(pDetalle.getPrecio().subtract(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().subtract(pDetalle.getProducto().getProductoPrecio().getPrecio()));
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
			pDetalle.setCantidad(pDetalle.getCantidad()+1);
			pDetalle.setPrecio(pDetalle.getPrecio().add(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			if(this.pedidoPersiste.getMultiplicador()!=null){
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(this.pedidoPersiste.getMultiplicador()));
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
			}else{
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
			}
		}
	}

	public void validarProductoSeleccionado(){
		if(this.listadoDetallePedido.size()>0){
			this.showSeleccionarProducto = false;
			this.showCheckout            = true;
		}else{
			this.mostrarMensajeGlobal("seleccioneUnProducto", "advertencia");
		}
	}

	public void regresarSeleccionProducto(){
		this.showSeleccionarProducto = true;
		this.showCheckout            = false;
	}

	public void confirmarPedido(){
		boolean error = false;
		try{
			if(this.pedidoPersiste.getCliente().getEmail()==null     || this.pedidoPersiste.getCliente().getEmail().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaEmail", "error");
			}else{
				if(!RicesTools.validateEmail(this.pedidoPersiste.getCliente().getEmail().trim().toLowerCase())){
					error = true;
					this.mostrarMensajeGlobal("ingresaEmailValido", "error");
				}
			}
			if(this.pedidoPersiste.getCliente().getNombre()==null    || this.pedidoPersiste.getCliente().getNombre().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaNombre", "error");
			}
			if(this.pedidoPersiste.getCliente().getCelular()==null   || this.pedidoPersiste.getCliente().getCelular().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaTelefono", "error");
			}
			if(this.pedidoPersiste.getCliente().getDireccion()==null || this.pedidoPersiste.getCliente().getDireccion().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaDireccion", "error");
			}
			if(this.pedidoPersiste.getCliente().getBarrio()==null    || this.pedidoPersiste.getCliente().getBarrio().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaBarrio", "error");
			}

			if(!error){
				this.pedidoPersiste.getCliente().setEmail(this.pedidoPersiste.getCliente().getEmail().trim().toLowerCase());
				this.pedidoPersiste.getCliente().setNombre(this.pedidoPersiste.getCliente().getNombre().trim().toUpperCase());
				this.pedidoPersiste.getCliente().setCelular(this.pedidoPersiste.getCliente().getCelular().trim());
				this.pedidoPersiste.getCliente().setDireccion(this.pedidoPersiste.getCliente().getDireccion().trim().toUpperCase());
				this.pedidoPersiste.getCliente().setBarrio(this.pedidoPersiste.getCliente().getBarrio().trim().toUpperCase());


				boolean exito = false;
				//REGISTRA EL CLIENTE
				if(this.pedidoPersiste.getCliente().getId()==null){
					Integer idCliente = IActualizaRices.registrarCliente(this.pedidoPersiste.getCliente());
					if(idCliente!=null){
						this.pedidoPersiste.getCliente().setId(idCliente);
						exito = true;
					}else{
						this.mostrarMensajeGlobal("noRegistraCliente", "error");
					}
				}else{
					exito = IActualizaRices.actualizarCliente(this.pedidoPersiste.getCliente());
				}
				if(exito){
					//REGISTRA EL PEDIDO
					Integer idPedido = IActualizaRices.registrarPedido(this.pedidoPersiste);
					if(idPedido!=null){
						//REGISTRA DETALLE
						for(DetallePedido dp: this.listadoDetallePedido){
							dp.setIdpedido(idPedido);
							if(!IActualizaRices.registrarDetallePedido(dp)){
								exito = false;
								break;
							}
						}
						//SI UTILIZÓ CUPON LO INHABILITA
						if(this.pedidoPersiste.getTransientCuponCliente()!=null){
							this.pedidoPersiste.getTransientCuponCliente().setUsado("S");
							IActualizaRices.actualizarEstadoCupon(this.pedidoPersiste.getTransientCuponCliente());
						}
					}else{
						exito = false;
						this.mostrarMensajeGlobal("noRegistraPedido", "error");
					}
				}else{
					this.mostrarMensajeGlobal("noRegistraCliente", "advertencia");
				}
				if(exito){
					FacesContext context = FacesContext.getCurrentInstance();
					HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
					if(sesion!=null){
						sesion.invalidate();
						sesion=null;
					}
					this.pedidoRegistrado   = true;
					this.showCheckout         = false;
					this.showPedidoRegistrado = true;
				}else{
					this.mostrarMensajeGlobal("noRegistraDetalle", "advertencia");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void onChangeMail(){
		try{
			if(this.pedidoPersiste.getCliente().getEmail()!=null && !this.pedidoPersiste.getCliente().getEmail().trim().equals("")){
				String pEmail = this.pedidoPersiste.getCliente().getEmail().trim().toLowerCase();
				
				if(this.mapClienteByMail.get(pEmail)==null){
					this.mapClienteByMail.put(pEmail, IConsultaRices.getClientesPorParametro(null, pEmail));
				}
				List<Cliente> resultados = this.mapClienteByMail.get(pEmail);
				if(resultados.size()>0){
					Cliente pCliente = resultados.get(0);
					if(pCliente.isGuardaDatos()){
						this.pedidoPersiste.setCliente(pCliente);
					}else{
						this.pedidoPersiste.getCliente().setId(pCliente.getId());
					}
					if(this.mapCuponByIdCliente.get(this.pedidoPersiste.getCliente().getId().intValue())==null){
						this.pedidoPersiste.setDescuento(null);
						this.pedidoPersiste.setMultiplicador(null);
						this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
						this.pedidoPersiste.setTransientCuponCliente(null);
					}
				}else{
					this.pedidoPersiste.setCliente(new Cliente());
					this.pedidoPersiste.getCliente().setEmail(pEmail);
					this.pedidoPersiste.setDescuento(null);
					this.pedidoPersiste.setMultiplicador(null);
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
					this.pedidoPersiste.setTransientCuponCliente(null);
				}
			}else{
				this.pedidoPersiste.setCliente(new Cliente());
				this.pedidoPersiste.setDescuento(null);
				this.pedidoPersiste.setMultiplicador(null);
				this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal());
				this.pedidoPersiste.setTransientCuponCliente(null);
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void cerrarPedido(){
		try{
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("/rices/index.jsf");
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void validarSesion(){
		if(this.pedidoRegistrado){
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			if(sesion!=null){
				sesion.invalidate();
				sesion=null;
			}
		}
	}
	
	public void aplicarCupon(){
		try{
			this.mensajeError = "";
			boolean error = false;
			if(this.codigoCupon==null || this.codigoCupon.trim().equals("")){
				error = true;
				this.mensajeError = this.getMensaje("ingresaCupon");
				this.mostraMensajeError="display:;";
			}else if(this.pedidoPersiste.getCliente().getId()==null){
				error = true;
				this.mensajeError = this.getMensaje("noClienteRegistrado");
				this.mostraMensajeError="display:;";
			}
			if(!error){
				String clave = this.pedidoPersiste.getCliente().getId().toString()+this.codigoCupon.trim().toUpperCase();
				if(this.mapCuponByIdCliente.get(clave)==null){
					this.mapCuponByIdCliente.put(clave, IConsultaRices.getCuponCliente(this.pedidoPersiste.getCliente().getId(), this.codigoCupon.trim().toUpperCase()));
				}
				CuponCliente cuponCliente = this.mapCuponByIdCliente.get(clave);
				if(cuponCliente==null){
					this.mensajeError = this.getMensaje("cuponNoValido");
					this.mostraMensajeError="display:;";
					error = true;
				}else if(cuponCliente.getUsado().equals("S")){
					this.mensajeError = this.getMensaje("cuponUtilizado");
					this.mostraMensajeError="display:;";
					error = true;
				}else{
					this.mensajeError = "";
					this.mostraMensajeError="display:none;";
					this.pedidoPersiste.setDescuento(cuponCliente.getTransientCupon().getPorcentaje());
					BigDecimal valorSobreCien = cuponCliente.getTransientCupon().getPorcentaje().divide(new BigDecimal(100));
					BigDecimal valorMultiplica = (new BigDecimal(1)).subtract(valorSobreCien);
					this.pedidoPersiste.setMultiplicador(valorMultiplica);
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getSubtotal().multiply(valorMultiplica));
					this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().setScale(2, RoundingMode.HALF_DOWN));
					this.pedidoPersiste.setTransientCuponCliente(cuponCliente);
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public List<Producto> getListadoProducto() {
		return listadoProducto;
	}


	public DetallePedido getDetallePedido() {
		if(this.detallePedido==null){
			this.detallePedido = new DetallePedido();
		}
		return detallePedido;
	}

	public void setDetallePedido(DetallePedido detallePedido) {
		this.detallePedido = detallePedido;
	}

	public Pedido getPedidoPersiste() {
		return pedidoPersiste;
	}

	public void setPedidoPersiste(Pedido pedidoPersiste) {
		this.pedidoPersiste = pedidoPersiste;
	}

	public List<DetallePedido> getListadoDetallePedido() {
		return listadoDetallePedido;
	}

	public boolean isShowSeleccionarProducto() {
		return showSeleccionarProducto;
	}

	public boolean isShowCheckout() {
		return showCheckout;
	}

	public boolean isShowPedidoRegistrado() {
		return showPedidoRegistrado;
	}

	public String getCodigoCupon() {
		return codigoCupon;
	}

	public void setCodigoCupon(String codigoCupon) {
		this.codigoCupon = codigoCupon;
	}

	public String getMostraMensajeError() {
		return mostraMensajeError;
	}

	public String getMensajeError() {
		return mensajeError;
	}

}
