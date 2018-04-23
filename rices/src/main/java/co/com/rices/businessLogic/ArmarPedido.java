package co.com.rices.businessLogic;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.Product;
import co.com.rices.objects.RiceMenu;

@ManagedBean
@ViewScoped
public class ArmarPedido extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 126945293261024517L;
	
	private boolean        showSeleccionarProducto;
	
	private int            tabActual;
	
	private Pedido         pedidoPersiste;
	
	private List<RiceMenu> listaRiceMenu;
	
	@PostConstruct
	public void init(){
		try{
			this.showSeleccionarProducto = true;
			this.tabActual               = 0;
			this.pedidoPersiste          = new Pedido();
			this.pedidoPersiste.setCargoDomicilio(new BigDecimal(0));
			this.pedidoPersiste.setSubtotal(new BigDecimal(0));
			this.pedidoPersiste.setIva(new BigDecimal(0));
			this.pedidoPersiste.setTotal(new BigDecimal(0));
			this.pedidoPersiste.setEstado("R");
			RiceMenu pRiceMenu = new RiceMenu();
			pRiceMenu.setEstado("A");
			pRiceMenu.setOpen(Calendar.getInstance().getTime());
			pRiceMenu.setClosed(Calendar.getInstance().getTime());
			this.listaRiceMenu = IQueryRices.getRiceMenus(pRiceMenu);
			for(RiceMenu rm: this.listaRiceMenu){
				rm.setListProducts(IQueryRices.getProductsByMenu(rm.getId()));
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
		}else{
			this.mostrarMensajeGlobal("paraContinuarNombre", "advertencia");
		}
	}
	
	public void seleccionarComplemento(Product pProducto){

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
	
	

}
