package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	private Pedido         pedidoPersiste;
	private DetallePedido  detallePedido;

	private List<Producto> listadoProducto;
	private List<DetallePedido> listadoDetallePedido;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		try{
			
			this.pedidoRegistrado = false;
			this.showSeleccionarProducto = true;
			this.pedidoPersiste = new Pedido();
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
		this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().add(this.detallePedido.getPrecio()));
		this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(this.detallePedido.getPrecio()));
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
		this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().subtract(pDetalle.getPrecio()));
		this.listadoDetallePedido.remove(pDetalle);
	}

	public void restarCantidad(DetallePedido pDetalle){
		if(pDetalle.getCantidad() > 1){
			pDetalle.setCantidad(pDetalle.getCantidad()-1);
			pDetalle.setPrecio(pDetalle.getPrecio().subtract(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().subtract(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().subtract(pDetalle.getProducto().getProductoPrecio().getPrecio()));
		}
	}

	public void sumarCantidad(DetallePedido pDetalle){
		if(pDetalle.getCantidad() < 99){
			pDetalle.setCantidad(pDetalle.getCantidad()+1);
			pDetalle.setPrecio(pDetalle.getPrecio().add(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setSubtotal(this.pedidoPersiste.getSubtotal().add(pDetalle.getProducto().getProductoPrecio().getPrecio()));
			this.pedidoPersiste.setTotal(this.pedidoPersiste.getTotal().add(pDetalle.getProducto().getProductoPrecio().getPrecio()));
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
				List<Cliente> resultados = IConsultaRices.getClientesPorParametro(null, pEmail);
				if(resultados.size()>0){
					Cliente pCliente = resultados.get(0);
					if(pCliente.isGuardaDatos()){
						this.pedidoPersiste.setCliente(pCliente);
					}else{
						this.pedidoPersiste.getCliente().setId(pCliente.getId());
					}
				}else{
					this.pedidoPersiste.setCliente(new Cliente());
					this.pedidoPersiste.getCliente().setEmail(pEmail);
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void cerrarPedido(){
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			if(sesion!=null){
				sesion.invalidate();
				sesion=null;
			}
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("/rices/pages/domicilios/seleccionarProducto.jsf");
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

}
