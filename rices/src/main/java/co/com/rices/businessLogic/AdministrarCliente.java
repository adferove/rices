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
import co.com.rices.general.RicesTools;


@ManagedBean
@ViewScoped
public class AdministrarCliente extends ConsultarFuncionesAPI  {

	private static final long serialVersionUID = -3758804340906126251L;

	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;


	private Cliente clientePersiste;
	private Cliente clienteConsulta;
	private Cliente clienteClon;
	private List<Cliente> listadoCliente;


	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.clienteConsulta = new Cliente();
			this.listadoCliente = IConsultaRices.getClientesPorNombreEmail(null, null);
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void nuevoCliente(){
		this.clientePersiste = new Cliente();
		this.showConsulta = false;
		this.showCrear    = true;
	}
	public void modificarCliente(Cliente pCliente){
		this.clientePersiste = pCliente;
		this.clienteClon     = pCliente.clon();
		this.showConsulta = false;
		this.showEditar    = true;
	}


	public void registrarCliente(){
		try{
			boolean error = false;
			if(this.clientePersiste.getNombre()==null || this.clientePersiste.getNombre().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseNombre", "error");
			}
			
			if(this.clientePersiste.getEmail()==null || this.clientePersiste.getEmail().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaEmailValido", "error");
			}else{
				if(!RicesTools.validateEmail(this.clientePersiste.getEmail().trim().toLowerCase())){
					error = true;
					this.mostrarMensajeGlobal("ingresaEmailValido", "error");
				}
			}

			if(this.clientePersiste.getCelular()==null || this.clientePersiste.getCelular().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseTelefono", "error");
			}
			if(this.clientePersiste.getDireccion()==null || this.clientePersiste.getDireccion().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseDireccion", "error");
			}
			if(this.clientePersiste.getBarrio()==null || this.clientePersiste.getBarrio().trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseBarrio", "error");
			}
			if(!error){


				this.clientePersiste.setEmail(this.clientePersiste.getEmail().trim().toLowerCase());
				this.clientePersiste.setNombre(this.clientePersiste.getNombre().trim().toUpperCase());
				this.clientePersiste.setCelular(this.clientePersiste.getCelular().trim());
				this.clientePersiste.setDireccion(this.clientePersiste.getDireccion().trim().toUpperCase());
				this.clientePersiste.setBarrio(this.clientePersiste.getBarrio().trim().toUpperCase());
				this.clientePersiste.setGuardaDatos(true);
				int idCliente=IActualizaRices.registrarCliente(this.clientePersiste);
				this.showCrear=false;
				this.showConsulta=true;

				if (idCliente>0){
					this.mostrarMensajeGlobal("clienteRegistrado", "exito");
				}else{
					this.mostrarMensajeGlobal("noRegistraCliente", "error");
				}


			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void editarCliente(){
		try{	
			this.clienteClon.setEmail(this.clienteClon.getEmail().trim().toLowerCase());
			this.clienteClon.setNombre(this.clienteClon.getNombre().trim().toUpperCase());
			this.clienteClon.setCelular(this.clienteClon.getCelular().trim());
			this.clienteClon.setDireccion(this.clienteClon.getDireccion().trim().toUpperCase());
			this.clienteClon.setBarrio(this.clienteClon.getBarrio().trim().toUpperCase());
			if(IActualizaRices.actualizarCliente(this.clienteClon)){
				this.clientePersiste.setNombre(this.clienteClon.getNombre());
				this.clientePersiste.setEmail(this.clienteClon.getEmail());
				this.clientePersiste.setDireccion(this.clienteClon.getDireccion());
				this.clientePersiste.setCelular(this.clienteClon.getCelular());
				this.clientePersiste.setBarrio(this.clienteClon.getBarrio());
				this.showConsulta = true;
				this.showEditar   = false;
				this.mostrarMensajeGlobal("clienteActualizado", "exito");
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
	public void consultarCliente(String pNombre, String pEmail){
		try{
			this.showConsulta = true;
			this.clienteConsulta = new Cliente();
			this.listadoCliente = IConsultaRices.getClientesPorNombreEmail(pNombre, pEmail);
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	public void verCliente(Cliente pCliente){
		this.clientePersiste = pCliente;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public Cliente getClientePersiste() {
		return clientePersiste;
	}

	public void setClientePersiste(Cliente clientePersiste) {
		this.clientePersiste = clientePersiste;
	}

	public Cliente getClienteConsulta() {
		return clienteConsulta;
	}

	public void setClienteConsulta(Cliente clienteConsulta) {
		this.clienteConsulta = clienteConsulta;
	}

	public List<Cliente> getListadoCliente() {
		return listadoCliente;
	}
	public boolean isShowCrear() {
		return showCrear;
	}

	public boolean isShowEditar() {
		return showEditar;
	}
	public Cliente getClienteClon() {
		return clienteClon;
	}
}
