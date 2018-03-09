package co.com.rices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.Usuario;
import co.com.rices.general.RicesTools;

@ManagedBean
@ViewScoped
public class Authenticate extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -1573486828665458226L;

	private String usuario;
	private String password;
	
	private Map<String, Usuario> mapUsuarioByLogin;
	
	@PostConstruct
	public void init(){
		this.mapUsuarioByLogin = new HashMap<String, Usuario>();
	}
	
	public void authenticate(){
		try{
			boolean error = false;
			if(this.usuario==null || this.usuario.trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaUsuario", "error");
			}
			if(this.password==null || this.password.trim().equals("")){
				error = true;
				this.mostrarMensajeGlobal("ingresaPassword", "error");
			}
			if(!error){
				String clave = this.usuario.trim().toLowerCase();
				if(this.mapUsuarioByLogin.get(clave)==null){
					Usuario u = IConsultaRices.getUsuarioByLogin(clave);
					if(u==null){
						u = new Usuario();
					}
					this.mapUsuarioByLogin.put(clave, u);
				}
				Usuario usuario = this.mapUsuarioByLogin.get(clave);
				if(usuario!=null && usuario.getLogin()!=null && !usuario.getLogin().trim().equals("")){
					String passwordEcriptada = RicesTools.encriptar(this.password.trim());
					if(passwordEcriptada.equals(usuario.getPassword())){
						//SE CONSULTAN LOS ROLES QUE TIENE EL USUARIO
						List<Integer> roles = IConsultaRices.getRolesByLogin(clave);
						FacesContext context = FacesContext.getCurrentInstance();
						HttpSession sesion = null; 
						sesion = (HttpSession) context.getExternalContext().getSession(true);
						if(sesion!=null){
							sesion.invalidate();
						}
						sesion = (HttpSession) context.getExternalContext().getSession(true);
						sesion.setAttribute("RicesUser", usuario);
						sesion.setAttribute("RicesRoles", roles);
						ExternalContext externalContext = context.getExternalContext();
						externalContext.redirect("/rices/pages/home.jsf");
					}else{
						this.mostrarMensajeGlobal("passwordInconrrecto", "error");
					}
				}else{
					this.mostrarMensajeGlobal("usuarioNoExiste", "error");
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
