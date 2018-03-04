package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Producto;
import co.com.rices.beans.ProductoPrecio;

@ManagedBean
@ViewScoped
public class AdministrarProducto extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -2345646931914506093L;
	
	private boolean showConsulta;
	
	private Producto productoPersiste;
	private Producto productoConsulta;
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
	}
	
	public void consultarProducto(){
		
	}
	
	public void verDetalleProducto(Producto pProducto){
		this.productoPersiste = pProducto;
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

}
