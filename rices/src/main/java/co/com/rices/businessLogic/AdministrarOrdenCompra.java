package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;
import co.com.rices.beans.ProductoPrecio;
import co.com.rices.general.RicesTools;

@ManagedBean
@SessionScoped
public class AdministrarOrdenCompra extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -8483194343442440237L;
	
	private boolean        showSeleccionarProducto;
	private boolean        showCheckout;
	private boolean        showPedidoRegistrado;
	
	private Pedido         pedidoPersiste;
	private DetallePedido  detallePedido;
	
	private List<Producto> listadoProducto;
	private List<DetallePedido> listadoDetallePedido;
	
	@PostConstruct
	public void init(){
		this.showSeleccionarProducto = true;
		this.listadoProducto = new ArrayList<Producto>();
		Producto producto = new Producto();
		producto.setId(1);
		producto.setNombre("Combo mix");
		producto.setDescripcion("Arroz mixto + bebida + papas");
		producto.setProductoPrecio(new ProductoPrecio());
		producto.getProductoPrecio().setPrecio(new BigDecimal(5500));
		producto.setRating(4);
		producto.setRutaImagen("combo_mix");
		this.listadoProducto.add(producto);
		Producto producto2 = new Producto();
		producto2.setId(2);
		producto2.setDescripcion("Arroz especial + bebida + papas");
		producto2.setNombre("Combo rices");
		producto2.setProductoPrecio(new ProductoPrecio());
		producto2.getProductoPrecio().setPrecio(new BigDecimal(6500));
		producto2.setRating(3);
		producto2.setRutaImagen("combo_rices");
		this.listadoProducto.add(producto2);
		
		this.pedidoPersiste = new Pedido();
		this.pedidoPersiste.setCliente(new Cliente());
		this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
		this.pedidoPersiste.setSubtotal(new BigDecimal(0));
		this.pedidoPersiste.setIva(new BigDecimal(0));
		this.pedidoPersiste.setTotal(new BigDecimal(0));
		this.detallePedido = new DetallePedido();
		this.listadoDetallePedido = new ArrayList<DetallePedido>();
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
				this.showCheckout         = false;
				this.showPedidoRegistrado = true;
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void cerrarPedido(){
		
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
