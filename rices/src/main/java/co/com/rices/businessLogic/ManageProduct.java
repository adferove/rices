package co.com.rices.businessLogic;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.DAO.IUpdateRices;
import co.com.rices.beans.Usuario;
import co.com.rices.objects.Product;

@ManagedBean
@ViewScoped
public class ManageProduct extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -3401698253084727629L;

	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	private Usuario  usuario;
	private Product productoPersiste;
	private Product productoConsulta;
	private Product productoClon;
	private List<Product> listadoProducto;

	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.productoConsulta = new Product();
			this.listadoProducto = IQueryRices.getProductsByParams(null);
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			if(sesion.getAttribute("RicesUser")!=null){
				this.usuario = (Usuario) sesion.getAttribute("RicesUser");
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void nuevoProducto(){
		this.productoPersiste = new Product();
		this.showConsulta = false;
		this.showCrear    = true;
	}

	public void consultarProducto(){

	}

	public void registrarProducto(){
		try{
			boolean error = false;
			if(this.productoPersiste.getName()==null || this.productoPersiste.getName().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseNombreProducto", "error");
			}else{
				this.productoPersiste.setName(this.productoPersiste.getName().trim().toUpperCase());
			}
			if(this.productoPersiste.getDescription()==null || this.productoPersiste.getDescription().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseDescripcionProducto", "error");
			}
			if(this.productoPersiste.getImageName()==null || this.productoPersiste.getImageName().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseRutaImagenProducto", "error");
			}
			if(!error){
				this.productoPersiste.setCreationDate(Calendar.getInstance().getTime());
				this.productoPersiste.setLoginUsuario(this.usuario.getLogin());
				int idProducto = IInsertRices.saveProduct(this.productoPersiste);
				if(idProducto > 0){
					this.productoPersiste.setId(idProducto);
					this.listadoProducto.add(this.productoPersiste);
					this.showConsulta = true;
					this.showCrear    = false;
					this.mostrarMensajeGlobal("productoRegistrado", "exito");
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
			boolean error = false;
			if(this.productoClon.getName()==null || this.productoClon.getName().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseNombreProducto", "error");
			}else{
				this.productoClon.setName(this.productoClon.getName().trim().toUpperCase());
			}
			if(this.productoClon.getDescription()==null || this.productoClon.getDescription().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseDescripcionProducto", "error");
			}
			if(this.productoClon.getImageName()==null || this.productoClon.getImageName().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseRutaImagenProducto", "error");
			}
			if(!error){
				if(IUpdateRices.updateProduct(this.productoClon)){
					this.productoPersiste.setDescription(this.productoClon.getDescription());
					this.productoPersiste.setState(this.productoClon.getState());
					this.productoPersiste.setName(this.productoClon.getName());
					this.productoPersiste.setRanking(this.productoClon.getRanking());
					this.productoPersiste.setImageName(this.productoClon.getImageName());
					this.productoPersiste.setProductType(this.productoClon.getProductType());
					this.showConsulta = true;
					this.showEditar   = false;
					this.mostrarMensajeGlobal("productoActualizado", "exito");
				}
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

	public void verDetalleProducto(Product pProducto){
		this.productoPersiste = pProducto;
		this.productoClon     = pProducto.clone();
		this.showConsulta = false;
		this.showEditar   = true;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public Product getProductoPersiste() {
		return productoPersiste;
	}

	public void setProductoPersiste(Product productoPersiste) {
		this.productoPersiste = productoPersiste;
	}

	public Product getProductoConsulta() {
		return productoConsulta;
	}

	public void setProductoConsulta(Product productoConsulta) {
		this.productoConsulta = productoConsulta;
	}

	public List<Product> getListadoProducto() {
		return listadoProducto;
	}

	public boolean isShowCrear() {
		return showCrear;
	}

	public boolean isShowEditar() {
		return showEditar;
	}

	public Product getProductoClon() {
		return productoClon;
	}

}
