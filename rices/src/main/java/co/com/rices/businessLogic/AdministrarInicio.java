package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.Cupon;
import co.com.rices.general.EmailSender;
import co.com.rices.general.RicesTools;
import co.com.rices.objects.CouponCode;

@ManagedBean
@ViewScoped
public class AdministrarInicio extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -247498233455725789L;
	
	private Cliente clientePersiste;
	private String  email;
	private boolean aceptaTerminos;
	
	private MapModel simpleModel;
		
	@PostConstruct
	public void init(){
		this.simpleModel = new DefaultMapModel();
		LatLng coord1 = new LatLng(7.1191141, -73.111472);
		this.simpleModel.addOverlay(new Marker(coord1, "Rices Bucaramanga", null,"http://ricestogo.com/rices/images/logo.png"));
	}

	public void mostrarModalDescuento(){
		this.clientePersiste = new Cliente();
		this.abrirModal("mdlDescuento");
	}

	public void registrarCliente(){
		boolean error = false;
		try{
			if(this.clientePersiste.getEmail()==null     || this.clientePersiste.getEmail().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaEmail", "error");
			}else{
				if(!RicesTools.validateEmail(this.clientePersiste.getEmail().trim().toLowerCase())){
					error = true;
					this.mostrarMensajeGlobal("ingresaEmailValido", "error");
				}
			}
			if(this.clientePersiste.getNombre()==null    || this.clientePersiste.getNombre().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaNombre", "error");
			}
			if(this.clientePersiste.getCelular()==null   || this.clientePersiste.getCelular().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaTelefono", "error");
			}
			if(this.clientePersiste.getDireccion()==null || this.clientePersiste.getDireccion().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaDireccion", "error");
			}
//			if(this.clientePersiste.getBarrio()==null    || this.clientePersiste.getBarrio().trim().equals("")){
//				error = true;
//				this.mostrarMensajeGlobal("ingresaBarrio", "error");
//			}

			if(!error){

				String pEmail = this.clientePersiste.getEmail().trim().toLowerCase();
				List<Cliente> resultados = IConsultaRices.getClientesPorParametro(null, pEmail);
				if(resultados.size()>0){
					this.mostrarMensajeGlobal("clienteEncuentraRegistrado", "error");
				}else{
					this.clientePersiste.setEmail(this.clientePersiste.getEmail().trim().toLowerCase());
					this.clientePersiste.setNombre(this.clientePersiste.getNombre().trim());
					this.clientePersiste.setCelular(this.clientePersiste.getCelular().trim());
					this.clientePersiste.setDireccion(this.clientePersiste.getDireccion().trim());
					//this.clientePersiste.setBarrio(this.clientePersiste.getBarrio().trim());
					this.clientePersiste.setGuardaDatos(true);
					
					Integer idCliente = IActualizaRices.registrarCliente(this.clientePersiste);
					if(idCliente>0){
						//Consulta el cupón y registra el cuponcliente
						Cupon cupon = IConsultaRices.getCuponActivoByCupon(IConstants.CUPON_REGISTRO);
//						if(cupon!=null){
//							CuponCliente cuponCliente = new CuponCliente();
//							cuponCliente.setCupon(IConstants.CUPON_REGISTRO);
//							cuponCliente.setIdCliente(idCliente);
//							cuponCliente.setUsado("N");
//							IActualizaRices.registrarCuponCliente(cuponCliente);
//						}
						
						//GENERA EL CUPON
						if(cupon!=null){
							String code            = null;
							CouponCode couponQuery = new CouponCode();
							do{
								code ="RICES"+RicesTools.randomString(7);
								couponQuery.setCoupon(code);
							}while(IQueryRices.getCouponCode(couponQuery)!=null);
							
							CouponCode couponCode = new CouponCode();
							couponCode.setClientId(idCliente);
							couponCode.setCoupon(code);
							couponCode.setPercentage(cupon.getPorcentaje());
							couponCode.setUsed("N");
							IInsertRices.saveCouponCode(couponCode);
							this.cerrarModal("mdlDescuento");
							this.mostrarMensajeGlobal("clienteRegistradoDescuento", "exito");
							//String args[] = new String[1];
							//args[0] = code;
							//this.mostrarMensajeGlobalParametros("tuCuponDescuento", "exito", args);
							this.mostrarMensajeGlobal("RevisaCorreoCuponDescuento", "exito");
							String[] val = {this.clientePersiste.getNombre(), code};
							String[] sDestinatario = {pEmail};
							EmailSender.sendEmail(this.getMensaje("asunto"), this.getMensajeParametros("cuerpo", val), this.getMensaje("footer"), sDestinatario);
						}
					}
				}
			}	

		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void cerrarPedido(){
		this.cerrarModal("mdlDescuento");
	}
	
	public void suscribirse(){
		if(this.aceptaTerminos){
			if(!RicesTools.validateEmail(this.email.trim().toLowerCase())){
				this.mostrarMensajeGlobal("ingresaEmailValido", "error");
			}else{
				this.mostrarMensajeGlobal("listoSuscrito", "exito");
				this.email = "";
				this.aceptaTerminos = false;
			}
		}else{
			this.mostrarMensajeGlobal("debeAceptarTerminos", "advertencia");
		}
	}
	

	public Cliente getClientePersiste() {
		if(this.clientePersiste==null){
			this.clientePersiste = new Cliente();
		}
		return clientePersiste;
	}

	public void setClientePersiste(Cliente clientePersiste) {
		this.clientePersiste = clientePersiste;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAceptaTerminos() {
		return aceptaTerminos;
	}

	public void setAceptaTerminos(boolean aceptaTerminos) {
		this.aceptaTerminos = aceptaTerminos;
	}

	public MapModel getSimpleModel() {
		return simpleModel;
	}
	
}
