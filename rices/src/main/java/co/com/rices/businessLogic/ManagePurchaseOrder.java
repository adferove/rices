package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ManagePurchaseOrder extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 911365794720003052L;

	private boolean        showSeleccionarProducto;
	
	private Product        mainProductSelected;
	
	private Pedido         pedidoPersiste;
	private DetallePedido  detallePedido;

	private List<Product>  listadoProducto;
	
	private List<DetallePedido> listadoDetallePedido;
	
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
			this.pedidoPersiste          = new Pedido();
			this.pedidoPersiste.setCliente(new Cliente());
			this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
			this.pedidoPersiste.setSubtotal(new BigDecimal(0));
			this.pedidoPersiste.setIva(new BigDecimal(0));
			this.pedidoPersiste.setTotal(new BigDecimal(0));
			this.pedidoPersiste.setEstado("R");
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
		this.detallePedido = new DetallePedido();
		this.detallePedido.setMainProduct(this.mainProductSelected);
		this.detallePedido.setCantidad(1);
		this.detallePedido.setPrecio(this.mainProductSelected.getPrice());
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
		this.cerrarModal("mdlSelectComplement");
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
			this.showSeleccionarProducto = false;
			//this.showCheckout            = true;
		}else{
			this.mostrarMensajeGlobal("seleccioneUnProducto", "advertencia");
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

	public Pedido getPedidoPersiste() {
		return pedidoPersiste;
	}

	public void setPedidoPersiste(Pedido pedidoPersiste) {
		this.pedidoPersiste = pedidoPersiste;
	}

	public DetallePedido getDetallePedido() {
		return detallePedido;
	}

	public void setDetallePedido(DetallePedido detallePedido) {
		this.detallePedido = detallePedido;
	}

	public List<DetallePedido> getListadoDetallePedido() {
		return listadoDetallePedido;
	}

}
