package co.com.rices.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IConsultaRices;
import co.com.rices.beans.EstructuraMenu;


@ManagedBean
@SessionScoped
public class Menu extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 3163117800154798178L;
	private MenuModel model;
	private Map<Integer, List<EstructuraMenu>> hijos;

	@PostConstruct
	public void init(){
		try{
			if(this.model == null){
				this.model = new DefaultMenuModel();
				List<EstructuraMenu> estructuraMenus = IConsultaRices.getEsctructuraPorRol(1);
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

	public MenuModel getModel() {
		return model;
	}

}
