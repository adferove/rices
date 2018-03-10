package co.com.rices;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.DAO.IActualizaRices;
import co.com.rices.beans.Usuario;
import co.com.rices.general.RicesTools;

@ManagedBean
@ViewScoped
public class General extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -604280393976354264L;
	
	private String password;
	private String confirmarPassword;
	private Usuario usuario;
	
	@PostConstruct
	public void init(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
		if(sesion.getAttribute("RicesUser")!=null){
			this.usuario = (Usuario) sesion.getAttribute("RicesUser");
		}
	}
	
	
	public void cambiarPassword(){
		try{
			boolean error = false;
			if(this.password==null || this.password.trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingreseNuevoPassword", "error");
			}else if(this.password.trim().length()<8){
				error = true;
				this.mostrarMensajeGlobal("ochoCaracteresNuevoPassword", "error");
			}
			if(!error){
				if(this.password.trim().equals(this.confirmarPassword.trim())){
					String passwordEcriptada = RicesTools.encriptar(this.password.trim());
					if(IActualizaRices.actualizarPasswordByLogin(passwordEcriptada, this.usuario.getLogin())){
						this.mostrarMensajeGlobal("passwordCambiado", "exito");
					}
				}else{
					this.mostrarMensajeGlobal("noCoincideNuevoPassword", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getConfirmarPassword() {
		return confirmarPassword;
	}


	public void setConfirmarPassword(String confirmarPassword) {
		this.confirmarPassword = confirmarPassword;
	}
	
	
}
