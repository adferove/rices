package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IActualizaRices;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.Cupon;
import co.com.rices.beans.CuponCliente;
import co.com.rices.general.RicesTools;

@ManagedBean
@ViewScoped
public class AdministrarInicio extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -247498233455725789L;
	
	private Cliente clientePersiste;
	private String  email;
	private boolean aceptaTerminos;
	
	@PostConstruct
	public void init(){
		
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
			if(this.clientePersiste.getBarrio()==null    || this.clientePersiste.getBarrio().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaBarrio", "error");
			}

			if(!error){

				String pEmail = this.clientePersiste.getEmail().trim().toLowerCase();
				List<Cliente> resultados = IConsultaRices.getClientesPorParametro(null, pEmail);
				if(resultados.size()>0){
					this.mostrarMensajeGlobal("clienteEncuentraRegistrado", "error");
				}else{
					this.clientePersiste.setEmail(this.clientePersiste.getEmail().trim().toLowerCase());
					this.clientePersiste.setNombre(this.clientePersiste.getNombre().trim().toUpperCase());
					this.clientePersiste.setCelular(this.clientePersiste.getCelular().trim());
					this.clientePersiste.setDireccion(this.clientePersiste.getDireccion().trim().toUpperCase());
					this.clientePersiste.setBarrio(this.clientePersiste.getBarrio().trim().toUpperCase());
					this.clientePersiste.setGuardaDatos(true);
					
					Integer idCliente = IActualizaRices.registrarCliente(this.clientePersiste);
					if(idCliente>0){
						//Consulta el cupón y registra el cuponcliente
						Cupon cupon = IConsultaRices.getCuponActivoByCupon(IConstants.CUPON_REGISTRO);
						if(cupon!=null){
							CuponCliente cuponCliente = new CuponCliente();
							cuponCliente.setCupon(IConstants.CUPON_REGISTRO);
							cuponCliente.setIdCliente(idCliente);
							cuponCliente.setUsado("N");
							IActualizaRices.registrarCuponCliente(cuponCliente);
						}
						
						this.cerrarModal("mdlDescuento");
						this.mostrarMensajeGlobal("clienteRegistradoDescuento", "exito");
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
	
	
}
