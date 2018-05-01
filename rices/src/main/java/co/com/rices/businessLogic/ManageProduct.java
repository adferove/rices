package co.com.rices.businessLogic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.DAO.IUpdateRices;
import co.com.rices.beans.Usuario;
import co.com.rices.general.RiceFile;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.RiceMenu;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ManageProduct extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -3401698253084727629L;

	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	private boolean showCreateStep;
	private boolean showProductStep;
	private boolean showStepDetail;
	
	private String   productChain;
	
	private Usuario  usuario;
	
	private Product productoPersiste;
	private Product productoConsulta;
	private Product productoClon;
		
	private ProductStep productStepPersist;
	private ProductStep productStepClone;
	private StepDetail  stepDetailPersist;
	private StepDetail  stepDetailClone;
	
	private RiceFile    file;
	
	private List<Product> listadoProducto;
	
	private List<ProductStep> listProductStep;
	
	private List<StepDetail>  listStepDetail;
	
	private List<SelectItem>  itemMenu;
 	
	private Map<Integer, String> mapProductName;

	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.productoConsulta = new Product();
			this.mapProductName = new HashMap<Integer, String>();
			this.listadoProducto = IQueryRices.getProductsByParams(null);
			for(Product p: this.listadoProducto){
				this.mapProductName.put(p.getId(), p.getName());
			}
			List<RiceMenu> riceMenus = IQueryRices.getRiceMenus(null);
			this.itemMenu = new ArrayList<SelectItem>();
			for(RiceMenu rm: riceMenus){
				this.itemMenu.add(new SelectItem(rm.getId(), rm.getDescription()));
			}
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
				if(this.productoPersiste.getName().trim().contains("-")){
					error = true;
					this.mostrarMensajeGlobal("ingreseNombreSinGuion", "error");
				}else{
					this.productoPersiste.setName(this.productoPersiste.getName().trim());
				}
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
					this.mapProductName.put(idProducto, this.productoPersiste.getName());
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
				if(this.productoClon.getName().trim().contains("-")){
					error = true;
					this.mostrarMensajeGlobal("ingreseNombreSinGuion", "error");
				}else{
					this.productoClon.setName(this.productoClon.getName().trim());
				}
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
				this.productoClon.setContentType(null);
				if(this.file!=null){
					this.productoClon.setContentType(this.file.getContentType());
				}
				if(IUpdateRices.updateProduct(this.productoClon)){
					this.mapProductName.put(this.productoClon.getId(), this.productoClon.getName());
					this.productoPersiste.setDescription(this.productoClon.getDescription());
					this.productoPersiste.setState(this.productoClon.getState());
					this.productoPersiste.setName(this.productoClon.getName());
					this.productoPersiste.setRanking(this.productoClon.getRanking());
					this.productoPersiste.setImageName(this.productoClon.getImageName());
					this.productoPersiste.setProductType(this.productoClon.getProductType());
					this.productoPersiste.setPrice(this.productoClon.getPrice());
					this.productoPersiste.setIdMenu(this.productoClon.getIdMenu());

					//Guarda la imagen en disco
					if(this.file!=null){
						InputStream in = new ByteArrayInputStream(this.file.getContents());
						BufferedImage bImageFromConvert = ImageIO.read(in);
						ImageIO.write(bImageFromConvert, this.productoClon.getMime(), new File(IConstants.PATH_DISK+this.productoClon.getImageName()+"."+this.productoClon.getMime()));
						this.productoPersiste.setImage(this.file.getContents());
					}
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
		}else if(pValor==4){
			this.showProductStep = true;
			this.showStepDetail  = false;
		}
	}

	public void verDetalleProducto(Product pProducto){
		this.productoPersiste = pProducto;
		this.productoClon     = pProducto.clone();
		this.showConsulta = false;
		this.showEditar   = true;
		this.file = null;
		if(pProducto.getImage()!=null){
			this.file = new RiceFile();
			this.file.setContentType(pProducto.getContentType());
			this.file.setContents(pProducto.getImage());
		}
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
	
	public void exitEditStep(){
		this.cerrarModal("mdlEditStep");
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
	
	public void nuevoProductoEleccion(){
		this.stepDetailPersist = new StepDetail();
		this.stepDetailPersist.setPrice(new BigDecimal(0));
		this.stepDetailPersist.setTransientProduct(new Product());
		this.productChain = null;
		this.abrirModal("mdlCrearStepDetail");
	}
	
	public void verDetalleEleccion(ProductStep productStep){
		try{
			this.productStepPersist = productStep;
			this.listStepDetail = IQueryRices.getDetailsByProductStepId(productStep.getId(), false);
			for(StepDetail sd: this.listStepDetail){
				sd.setTransientProduct(new Product());
				sd.getTransientProduct().setName(this.mapProductName.get(sd.getSelectedProductId()));
			}
			this.showProductStep = false;
			this.showStepDetail  = true;
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void saveStepDetail(){
		try{
			boolean error = false;
			if(StringUtils.trimToNull(this.productChain)==null){
				error = true;
				this.mostrarMensajeGlobal("ingreseNombreProductoValido", "error");
			}
			
			if(!error){
				if(this.stepDetailPersist.getPrice()==null){
					this.stepDetailPersist.setPrice(new BigDecimal(0));
				}
				String[] productValue = this.productChain.split("-");
				this.stepDetailPersist.getTransientProduct().setId(new Integer(productValue[0]));
				this.stepDetailPersist.getTransientProduct().setName(productValue[1]);
				this.stepDetailPersist.setSelectedProductId(this.stepDetailPersist.getTransientProduct().getId());
				this.stepDetailPersist.setProductStepId(this.productStepPersist.getId());
				Integer idStepProduct = IInsertRices.saveStepDetail(this.stepDetailPersist);
				if(idStepProduct!=null){
					this.stepDetailPersist.setId(idStepProduct);
					this.listStepDetail.add(this.stepDetailPersist);
					this.mostrarMensajeGlobal("productoAsociadoEleccion", "exito");
					this.cerrarModal("mdlCrearStepDetail");
				}else{
					this.mostrarMensajeGlobal("noFuePosibleGuardar", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void exitCreateStepDetail(){
		this.cerrarModal("mdlCrearStepDetail");
	}
	
	public List<String> productAutoComplete(String pTexto){
		List<String> resultados = null;
		try{
			if(StringUtils.trimToNull(pTexto)!=null){
				Product p = new Product();
				p.setName(pTexto.trim());
				List<Product> products = IQueryRices.getProductsByParams(p);
				if(products.size()>0){
					resultados = new ArrayList<String>();
					for(Product prod: products){
						resultados.add(prod.getId()+"-"+prod.getName());
					}
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
		return resultados;
	}
	
	public void goEditStepDetail(StepDetail pStepDetail){
		this.stepDetailPersist = pStepDetail;
		this.stepDetailClone = new StepDetail();
		this.stepDetailClone.setId(pStepDetail.getId());
		this.stepDetailClone.setState(pStepDetail.getState());
		this.stepDetailClone.setPrice(pStepDetail.getPrice());
		this.abrirModal("mdlEditStepDetail");
	}
	
	public void exitEditStepDetail(){
		this.cerrarModal("mdlEditStepDetail");
	}
	
	public void updateStepDetail(){
		try{
			if(this.stepDetailClone.getPrice()==null){
				this.stepDetailClone.setPrice(new BigDecimal(0));
			}
			if(IUpdateRices.updateStepDetail(this.stepDetailClone)){
				this.stepDetailPersist.setState(this.stepDetailClone.getState());
				this.stepDetailPersist.setPrice(this.stepDetailClone.getPrice());
				this.mostrarMensajeGlobal("productoAsociadoEleccionEditado", "exito");
				this.cerrarModal("mdlEditStepDetail");
			}else{
				this.mostrarMensajeGlobal("noFuePosibleGuardar", "error");
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	/**
	 * Recibir el archivo y asignarlo al recurso.
	 * 
	 * @param event
	 */
	public void recibirArchivoRecurso(FileUploadEvent event) {
		try {
			UploadedFile f = event.getFile();
			this.file = new RiceFile();
			this.file.setFileName(f.getFileName());
			this.file.setContentType(f.getContentType());
			this.file.setContents(f.getContents());
			this.mostrarMensajeGlobal("archivoRecibido", "advertencia");
		} catch (Exception e) {
			IConstants.log.error(e.toString(), e);
		}
	}
	
	public void limpiarArchivoCargado() {
		this.file = null;
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

	public boolean isShowStepDetail() {
		return showStepDetail;
	}

	public StepDetail getStepDetailPersist() {
		return stepDetailPersist;
	}

	public void setStepDetailPersist(StepDetail stepDetailPersist) {
		this.stepDetailPersist = stepDetailPersist;
	}

	public StepDetail getStepDetailClone() {
		return stepDetailClone;
	}

	public void setStepDetailClone(StepDetail stepDetailClone) {
		this.stepDetailClone = stepDetailClone;
	}

	public List<StepDetail> getListStepDetail() {
		return listStepDetail;
	}

	public String getProductChain() {
		return productChain;
	}

	public void setProductChain(String productChain) {
		this.productChain = productChain;
	}

	public List<SelectItem> getItemMenu() {
		return itemMenu;
	}

	public RiceFile getFile() {
		return file;
	}

	public void setFile(RiceFile file) {
		this.file = file;
	}

}
