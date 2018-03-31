package co.com.rices.businessLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.City;
import co.com.rices.objects.Complement;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

@ManagedBean
@ViewScoped
public class ManageRegisteredOrder extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -5176873363876048439L;

	private List<Pedido> listadoPedido;
	
	private Map<Integer, String> mapCities;
	private Map<Integer, Product> mapProducts;
	private Map<Integer, List<ProductStep>> mapProductStep;
	
	@PostConstruct
	public void init(){
		try{


			this.mapCities = new HashMap<Integer,String>();
			List<City> cities = IQueryRices.getCities();
			for(City city: cities){
				this.mapCities.put(city.getId(), city.getName());
			}

			this.mapProducts = new HashMap<Integer,Product>();
			List<Product> products = IQueryRices.getProductsByParams(null);
			for(Product product: products){
				this.mapProducts.put(product.getId(), product);
			}

			this.mapProductStep = new HashMap<Integer, List<ProductStep>>();

			this.listadoPedido = IConsultaRices.getPedidoPorEstado("R");

			for(Pedido p: this.listadoPedido){
				p.setCityName(this.mapCities.get(p.getCodigoCiudad()));
				p.setListadoDetalle(IConsultaRices.getDetallePedidoPorId(p.getId()));
				for(DetallePedido d: p.getListadoDetalle()){
					d.setMainProduct(this.mapProducts.get(d.getIdproducto()));
					if(this.mapProductStep.get(d.getMainProduct().getId().intValue())==null){
						this.mapProductStep.put(d.getMainProduct().getId().intValue(), IQueryRices.getProductStepsByProductId(d.getMainProduct().getId(), false));
					}
					d.getMainProduct().setListProductStep(this.mapProductStep.get(d.getMainProduct().getId().intValue()));
					for(ProductStep ps: d.getMainProduct().getListProductStep()){
						Complement pComplement = new Complement();
						pComplement.setProductStepId(ps.getId());
						pComplement.setDetailId(d.getId());
						List<Complement> complements = IQueryRices.getComplementsByDetail(pComplement);
						if(complements.size()>0){
							ps.setListStepDetail(new ArrayList<StepDetail>());
							for(Complement complement: complements){
								StepDetail stepDetail = new StepDetail();
								stepDetail.setPrice(complement.getPrice());
								stepDetail.setTransientProduct(this.mapProducts.get(complement.getSelectedProductId()));
								ps.getListStepDetail().add(stepDetail);
							}
						}
					}
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	
	public void actualizarEstado(Pedido pPedido){
		try{
			if(!pPedido.getEstado().equals("R")){
				if(IActualizaRices.actualizarEstadoPedido(pPedido)){
					this.listadoPedido.remove(pPedido);
					this.mostrarMensajeGlobal("listaPedidoActualizada", "exito");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}


	public List<Pedido> getListadoPedido() {
		return listadoPedido;
	}
	
	
}
