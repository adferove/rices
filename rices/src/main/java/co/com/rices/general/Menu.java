package co.com.rices.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.EstructuraMenu;


@ManagedBean
@ViewScoped
public class Menu extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 3163117800154798178L;
	private MenuModel model;
	private Map<Integer, List<EstructuraMenu>> hijos;
	
	private boolean permisoMenu;
	private boolean permisoPagina;
	private List<String> opcionesValidas;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			if(sesion.getAttribute("RicesUser")!=null && sesion.getAttribute("RicesRoles")!=null){
				List<Integer> roles = (List<Integer>) sesion.getAttribute("RicesRoles");
				if(roles.size()>0){

					if(sesion.getAttribute("modeloMenu")!=null){
						this.model = (MenuModel) sesion.getAttribute("modeloMenu");
					}

					if(this.model == null){
						this.opcionesValidas = new ArrayList<String>();
						this.model = new DefaultMenuModel();
						sesion.setAttribute("modeloMenu", this.model);
						sesion.setAttribute("opcionesValidas", this.opcionesValidas);
						List<EstructuraMenu> estructuraMenus = IConsultaRices.getEsctructuraPorRol(roles);
						if(estructuraMenus!=null && estructuraMenus.size()>0){
							List<EstructuraMenu> padres = new ArrayList<EstructuraMenu>();
							this.hijos = new HashMap<Integer, List<EstructuraMenu>>();
							for(EstructuraMenu e: estructuraMenus){
								if(e.getIdMenuSuperior()==null){
									padres.add(e);
								}else{
									if(this.hijos.get(e.getIdMenuSuperior().intValue())==null){
										this.hijos.put(e.getIdMenuSuperior().intValue(), new ArrayList<EstructuraMenu>());
									}
									this.hijos.get(e.getIdMenuSuperior().intValue()).add(e);
								}
							}
							for(EstructuraMenu padre: padres){
								if(this.hijos.get(padre.getIdMenu().intValue())==null){

									if(padre.getUrl()!=null && !padre.getUrl().trim().equals("")){
										DefaultMenuItem item = new DefaultMenuItem(padre.getDescripcion().trim());
										if(padre.getUrl().trim().contains("/pages/")){
											item.setOutcome(padre.getUrl().trim());
											this.opcionesValidas.add(padre.getUrl().trim());
										}else{
											item.setUrl(padre.getUrl());
											item.setTarget("_blank");
										}
										item.setIcon("ui-icon-extlink");
										model.addElement(item);
									}
								}else {
									DefaultSubMenu submenu = new DefaultSubMenu(padre.getDescripcion().trim());
									submenu.setIcon("ui-icon-newwin");
									this.armarSubMenu(submenu, padre);
									this.model.addElement(submenu);
								}
							}
						}
					}

					if(this.model!=null){
						this.permisoMenu = true;
						HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
						if (req != null){
							if(req.getRequestURI()!=null){
								this.opcionesValidas = (List<String>) sesion.getAttribute("opcionesValidas");
								for(String uri: this.opcionesValidas){
									if(req.getRequestURI().trim().contains(uri)){
										this.permisoPagina = true;
										break;
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}

	private void armarSubMenu(DefaultSubMenu pSubMenu, EstructuraMenu pPadre){
		for(EstructuraMenu hijo: this.hijos.get(pPadre.getIdMenu().intValue())){
			if(this.hijos.get(hijo.getIdMenu().intValue())==null){
				if(hijo.getUrl()!=null && !hijo.getUrl().trim().equals("")){
					DefaultMenuItem item = new DefaultMenuItem(hijo.getDescripcion().trim());
					if(hijo.getUrl().trim().contains("/pages/")){
						item.setOutcome(hijo.getUrl().trim());
						this.opcionesValidas.add(hijo.getUrl().trim());
					}else{					
						item.setUrl(hijo.getUrl());
						item.setTarget("_blank");
					}
					item.setIcon("ui-icon-extlink");
					pSubMenu.addElement(item);
				}
			}else {
				DefaultSubMenu submenu = new DefaultSubMenu(hijo.getDescripcion().trim().toLowerCase());
				submenu.setIcon("ui-icon-newwin");
				this.armarSubMenu(submenu, hijo);
				pSubMenu.addElement(submenu);
			}
		}
	}
	
	public void cerrarSesion(){
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext().getSession(true);
			if(sesion!=null){
				sesion.invalidate();
				sesion=null;
			}
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			externalContext.redirect("/rices/");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public MenuModel getModel() {
		return model;
	}

	public boolean isPermisoMenu() {
		return permisoMenu;
	}

	public boolean isPermisoPagina() {
		return permisoPagina;
	}

}
