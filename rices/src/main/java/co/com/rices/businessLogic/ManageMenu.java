package co.com.rices.businessLogic;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.ConsultarFuncionesAPI;
import co.com.rices.IConstants;
import co.com.rices.DAO.IInsertRices;
import co.com.rices.DAO.IQueryRices;
import co.com.rices.DAO.IUpdateRices;
import co.com.rices.objects.RiceMenu;

@ManagedBean
@ViewScoped
public class ManageMenu extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = 3695341519648774133L;
	
	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	
	private RiceMenu persisteRiceMenu;
	private RiceMenu cloneRiceMenu;
	
	private List<RiceMenu> listadoMenu;

	@PostConstruct
	public void init(){
		try{
			this.showConsulta = true;
			this.listadoMenu = IQueryRices.getRiceMenus(null);
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void nuevoMenu(){
		this.persisteRiceMenu = new RiceMenu();
		this.showConsulta = false;
		this.showCrear    = true;
	}
	
	public void verDetalleMenu(RiceMenu pRiceMenu){
		this.persisteRiceMenu = pRiceMenu;
		this.cloneRiceMenu    = pRiceMenu.clone();
		this.showConsulta = false;
		this.showEditar   = true;
	}
	
	public void registrarMenu(){
		try{
			boolean error = false;
			if(StringUtils.trimToNull(this.persisteRiceMenu.getDescription())==null){
				this.mostrarMensajeGlobal("ingreseDescripcion", "error");
				error = true;
			}
			if(this.persisteRiceMenu.getOrden()==null){
				this.mostrarMensajeGlobal("ingreseOrden", "error");
				error = true;
			}
			if(StringUtils.trimToNull(this.persisteRiceMenu.getEstado())==null){
				this.mostrarMensajeGlobal("ingreseEstado", "error");
				error = true;
			}
			if(!error){
				if(IInsertRices.saveRiceMenu(this.persisteRiceMenu)){
					this.listadoMenu.add(this.persisteRiceMenu);
					this.mostrarMensajeGlobal("menuRegistradoExito", "exito");
					this.showCrear    = false;
					this.showConsulta = true;
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void editarMenu(){
		try{
			try{
				boolean error = false;
				if(StringUtils.trimToNull(this.cloneRiceMenu.getDescription())==null){
					this.mostrarMensajeGlobal("ingreseDescripcion", "error");
					error = true;
				}
				if(this.persisteRiceMenu.getOrden()==null){
					this.mostrarMensajeGlobal("ingreseOrden", "error");
					error = true;
				}
				if(StringUtils.trimToNull(this.cloneRiceMenu.getEstado())==null){
					this.mostrarMensajeGlobal("ingreseEstado", "error");
					error = true;
				}
				if(!error){
					if(IUpdateRices.updateRiceMenu(this.cloneRiceMenu)){
						this.mostrarMensajeGlobal("menuActualizadoExito", "exito");
						this.persisteRiceMenu.setId(this.cloneRiceMenu.getId());
						this.persisteRiceMenu.setDescription(this.cloneRiceMenu.getDescription());
						this.persisteRiceMenu.setEstado(this.cloneRiceMenu.getEstado());
						this.persisteRiceMenu.setOrden(this.cloneRiceMenu.getOrden());
						this.persisteRiceMenu.setOpen(this.cloneRiceMenu.getOpen());
						this.persisteRiceMenu.setClosed(this.cloneRiceMenu.getClosed());
						this.showEditar   = false;
						this.showConsulta = true;
					}
				}
			}catch(Exception e){
				IConstants.log.error(e.toString(),e);
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

	public List<RiceMenu> getListadoMenu() {
		return listadoMenu;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public RiceMenu getPersisteRiceMenu() {
		return persisteRiceMenu;
	}

	public void setPersisteRiceMenu(RiceMenu persisteRiceMenu) {
		this.persisteRiceMenu = persisteRiceMenu;
	}

	public RiceMenu getCloneRiceMenu() {
		return cloneRiceMenu;
	}

	public void setCloneRiceMenu(RiceMenu cloneRiceMenu) {
		this.cloneRiceMenu = cloneRiceMenu;
	}

	public boolean isShowCrear() {
		return showCrear;
	}

	public boolean isShowEditar() {
		return showEditar;
	}
}
