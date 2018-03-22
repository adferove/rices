package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ManagePurchaseOrder extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 911365794720003052L;

	private boolean        showSeleccionarProducto;
	
	private Product        mainProductSelected;

	private List<Product>  listadoProducto;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		try{
			this.showSeleccionarProducto = true;
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			
			//PRODUCTOS OFRECIDOS POR RICES
			if(sesion.getAttribute("RicesListProducts")==null){
				this.listadoProducto = IQueryRices.getProductsToSell();
				for(Product p: this.listadoProducto){
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
				sesion.setAttribute("RicesListProducts", this.listadoProducto);
			}else{
				this.listadoProducto = (List<Product>) sesion.getAttribute("RicesListProducts");
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void seleccionarComplemento(Product pProducto){
		this.mainProductSelected = pProducto;
		for(ProductStep s: this.mainProductSelected.getListProductStep()){
			for(StepDetail d: s.getListStepDetail()){
				d.setChecked(false);
			}
		}
		this.abrirModal("mdlSelectComplement");
	}
	
	public void seleccionUnicaListener(StepDetail pDetail){
		if(pDetail.getTransientProducStep().getSelectType().equals("U") && pDetail.isChecked()){
			for(StepDetail s: pDetail.getTransientProducStep().getListStepDetail()){
				if(s.getId().intValue()!=pDetail.getId().intValue()){
					s.setChecked(false);
				}
			}
		}
	}

	public boolean isShowSeleccionarProducto() {
		return showSeleccionarProducto;
	}

	public List<Product> getListadoProducto() {
		return listadoProducto;
	}

	public Product getMainProductSelected() {
		return mainProductSelected;
	}

}
