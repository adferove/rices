package co.com.rices.businessLogic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import co.com.rices.beans.Usuario;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;

@ManagedBean
@ViewScoped
public class ManageProduct extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -3401698253084727629L;

	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	private boolean showCreateStep;
	private boolean showProductStep;
	
	private Usuario  usuario;
	
	private Product productoPersiste;
	private Product productoConsulta;
	private Product productoClon;
	
	private ProductStep productStepPersist;
	private ProductStep productStepClone;
	
	private List<Product> listadoProducto;
	
	private List<ProductStep> listProductStep;

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
		try {
			this.listadoProducto = IQueryRices.getProductsByParams(this.productoConsulta);
		} catch (Exception e) {
			IConstants.log.error(e.toString(),e);
		}
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
					this.listProductStep = new ArrayList<ProductStep>(); 
					if(this.productoPersiste.getProductType().equals("P")){
						this.showProductStep = true;
					}else{
						this.showConsulta = true;
					}
					this.showCrear       = false;
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
		}else if(pValor==3){
			this.showConsulta    = true;
			this.showProductStep = false;
		}
	}

	public void verDetalleProducto(Product pProducto){
		this.productoPersiste = pProducto;
		this.productoClon     = pProducto.clone();
		this.showConsulta = false;
		this.showEditar   = true;
	}
	
	/**
	 * METODOS ASOCIADOS A TODO LO RELACIONADO CON ELECCIONES
	 * @return
	 */
	public void nuevaEleccion(){
		this.productStepPersist = new ProductStep();
		this.productStepPersist.setProductId(this.productoPersiste.getId());
		this.abrirModal("mdlCreateStep");
	}
	
	public void saveProductStep(){
		try{
			boolean error = false;
			if(StringUtils.trimToNull(this.productStepPersist.getDescription())==null){
				error = true;
				this.mostrarMensajeGlobal("enterStepDescripction", "error");
			}
			if(this.productStepPersist.getStepOrder()==null){
				error = true;	
				this.mostrarMensajeGlobal("enterStepOrder", "error");
			}
			
			if(!error){
				Integer idProductStep = IInsertRices.saveProductStep(this.productStepPersist);
				if(idProductStep!=null){
					this.productStepPersist.setId(idProductStep);
					this.listProductStep.add(this.productStepPersist);
					this.cerrarModal("mdlCreateStep");
					this.mostrarMensajeGlobal("stepCreatedSuccessfully", "exito");
				}else{
					this.mostrarMensajeGlobal("errorSavingStep", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void exitCreateStep(){
		this.cerrarModal("mdlCreateStep");
	}

	
	public void verEleccionProducto(Product pProducto){
		try{
			this.productoPersiste = pProducto;
			this.listProductStep = IQueryRices.getProductStepsByProductId(pProducto.getId(), false);
			this.showConsulta     = false;
			this.showProductStep  = true;
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void verStepProduct(ProductStep productStep){
		this.productStepPersist = productStep;
		this.productStepClone   = this.productStepPersist.clone();
		this.abrirModal("mdlEditStep");
	}	
	
	public void updateProductStep(){
		try{
			boolean error = false;
			if(StringUtils.trimToNull(this.productStepClone.getDescription())==null){
				error = true;
				this.mostrarMensajeGlobal("enterStepDescripction", "error");
			}
			if(this.productStepClone.getStepOrder()==null){
				error = true;	
				this.mostrarMensajeGlobal("enterStepOrder", "error");
			}

			if(!error){
				if(IUpdateRices.updateProductStep(this.productStepClone)){
					this.productStepPersist.setId(this.productStepClone.getId());
					this.productStepPersist.setDescription(this.productStepClone.getDescription());
					this.productStepPersist.setProductId(this.productStepClone.getProductId());
					this.productStepPersist.setSelectType(this.productStepClone.getSelectType());
					this.productStepPersist.setState(this.productStepClone.getState());
					this.productStepPersist.setStepOrder(this.productStepClone.getStepOrder());
					this.cerrarModal("mdlEditStep");
					this.mostrarMensajeGlobal("stepUpdateddSuccessfully", "exito");
				}else{
					this.mostrarMensajeGlobal("errorUpdatingStep", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void exitEditStep(){
		this.cerrarModal("mdlEditStep");
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

	public boolean isShowCreateStep() {
		return showCreateStep;
	}

	public ProductStep getProductStepPersist() {
		return productStepPersist;
	}

	public void setProductStepPersist(ProductStep productStepPersist) {
		this.productStepPersist = productStepPersist;
	}

	public ProductStep getProductStepClone() {
		return productStepClone;
	}

	public void setProductStepClone(ProductStep productStepClone) {
		this.productStepClone = productStepClone;
	}

	public List<ProductStep> getListProductStep() {
		return listProductStep;
	}

	public void setProductoClon(Product productoClon) {
		this.productoClon = productoClon;
	}

	public boolean isShowProductStep() {
		return showProductStep;
	}

}
