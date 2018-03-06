package co.com.rices.businessLogic;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Producto;
import co.com.rices.beans.ProductoPrecio;

@ManagedBean
@ViewScoped
public class AdministrarProducto extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -2345646931914506093L;
	
	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	
	private Producto productoPersiste;
	private Producto productoConsulta;
	private Producto productoClon;
	private List<Producto> listadoProducto;
	
	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.productoConsulta = new Producto();
			this.listadoProducto = IConsultaRices.getProductoPorEstado(null);
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void nuevoProducto(){
		this.productoPersiste = new Producto();
		this.productoPersiste.setProductoPrecio(new ProductoPrecio());
		this.showConsulta = false;
		this.showCrear    = true;
	}
	
	public void consultarProducto(){
		
	}
	
	public void registrarProducto(){
		try{
			boolean error = false;
			if(this.productoPersiste.getNombre()==null || this.productoPersiste.getNombre().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseNombreProducto", "error");
			}
			if(this.productoPersiste.getDescripcion()==null || this.productoPersiste.getDescripcion().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseDescripcionProducto", "error");
			}
			if(this.productoPersiste.getRutaImagen()==null || this.productoPersiste.getRutaImagen().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseRutaImagenProducto", "error");
			}
			if(this.productoPersiste.getProductoPrecio().getPrecio()==null){
				error = true;
				this.mostrarMensajeGlobal("ingresePrecioProducto", "error");
			}
			if(!error){
				this.productoPersiste.setFechacreacion(Calendar.getInstance().getTime());
				this.productoPersiste.setLoginusuario("arodrive");
				int idProducto = IActualizaRices.registrarProducto(this.productoPersiste);
				if(idProducto > 0){
					this.productoPersiste.setId(idProducto);
					this.productoPersiste.getProductoPrecio().setIdProducto(idProducto);
					this.productoPersiste.getProductoPrecio().setEstado("A");
					this.productoPersiste.getProductoPrecio().setFechaActualiza(Calendar.getInstance().getTime());
					this.productoPersiste.getProductoPrecio().setFechaCrea(Calendar.getInstance().getTime());
					this.productoPersiste.getProductoPrecio().setUsuarioActualiza("arodrive");
					this.productoPersiste.getProductoPrecio().setUsuarioCrea("arodrive");
					if(IActualizaRices.registrarProductoPrecio(this.productoPersiste.getProductoPrecio())>0){
						this.listadoProducto.add(this.productoPersiste);
						this.showConsulta = true;
						this.showCrear    = false;
						this.mostrarMensajeGlobal("productoRegistrado", "exito");
					}else{
						this.mostrarMensajeGlobal("problemaPrecioRegistro", "error");
					}
				}else{
					this.mostrarMensajeGlobal("problemaProductoRegistro", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void editarProducto(){
		try{	
			if(IActualizaRices.actualizarProducto(this.productoClon)){
				//ACTUALIZA A INACTIVO EL PRODUCTO PRECIO Y REGISTRA UNO NUEVO
				if(this.productoClon.getProductoPrecio().getPrecio().compareTo(this.productoPersiste.getProductoPrecio().getPrecio())!=0){
					this.productoClon.getProductoPrecio().setEstado("I");
					this.productoClon.getProductoPrecio().setFechaActualiza(Calendar.getInstance().getTime());
					this.productoClon.getProductoPrecio().setUsuarioActualiza("arodrive");
					
					boolean exitoPrecio = IActualizaRices.actualizarProductoPrecio(this.productoClon.getProductoPrecio());
					if(!exitoPrecio){
						this.mostrarMensajeGlobal("problemaPrecioRegistro", "error");
					}else{
						this.productoClon.getProductoPrecio().setEstado("A");
						this.productoClon.getProductoPrecio().setId(null);
						this.productoClon.getProductoPrecio().setFechaCrea(Calendar.getInstance().getTime());
						this.productoClon.getProductoPrecio().setUsuarioCrea("arodrive");
						int idProductoPrecio = IActualizaRices.registrarProductoPrecio(this.productoClon.getProductoPrecio());
						if(idProductoPrecio < 0){
							this.mostrarMensajeGlobal("problemaPrecioRegistro", "advertencia");
						}else{
							this.productoClon.getProductoPrecio().setId(idProductoPrecio);
						}
					}
				}
				this.productoPersiste.setDescripcion(this.productoClon.getDescripcion());
				this.productoPersiste.setEstado(this.productoClon.getEstado());
				this.productoPersiste.setNombre(this.productoClon.getNombre());
				this.productoPersiste.setProductoPrecio(this.productoClon.getProductoPrecio());
				this.productoPersiste.setRating(this.productoClon.getRating());
				this.productoPersiste.setRutaImagen(this.productoClon.getRutaImagen());
				this.showConsulta = true;
				this.showEditar   = false;
				this.mostrarMensajeGlobal("productoActualizado", "exito");
			}

		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void regresar(int pValor){
		if(pValor==1){
			this.showConsulta = true;
			this.showCrear    = false;
		}else if(pValor==2){
			this.showConsulta = true;
			this.showEditar   = false;
		}
	}
	
	public void verDetalleProducto(Producto pProducto){
		this.productoPersiste = pProducto;
		this.productoClon     = pProducto.clon();
		this.showConsulta = false;
		this.showEditar   = true;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public Producto getProductoPersiste() {
		return productoPersiste;
	}

	public void setProductoPersiste(Producto productoPersiste) {
		this.productoPersiste = productoPersiste;
	}

	public Producto getProductoConsulta() {
		return productoConsulta;
	}

	public void setProductoConsulta(Producto productoConsulta) {
		this.productoConsulta = productoConsulta;
	}

	public List<Producto> getListadoProducto() {
		return listadoProducto;
	}

	public boolean isShowCrear() {
		return showCrear;
	}

	public boolean isShowEditar() {
		return showEditar;
	}

	public Producto getProductoClon() {
		return productoClon;
	}

}
