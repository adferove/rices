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
import co.com.rices.objects.Parametro;

@ManagedBean
@ViewScoped
public class ManageParametro extends ConsultarFuncionesAPI{

	private static final long serialVersionUID = -8267282785049423441L;
	
	private boolean showConsulta;
	private boolean showCrear;
	private boolean showEditar;
	
	private Parametro persisteParametro;
	private Parametro cloneParametro;
	
	private List<Parametro> listaParametro;
	
	@PostConstruct 
	public void init(){
		try{
			this.showConsulta = true;
			this.listaParametro = IQueryRices.getParametrosById(null);
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
	
	public void nuevoParametro(){
		this.persisteParametro = new Parametro();
		this.showConsulta = false;
		this.showCrear    = true;
	}
	
	public void editarParametro(Parametro pParametro){
		this.persisteParametro = pParametro;
		this.cloneParametro    = pParametro.clone();
		this.showConsulta = false;
		this.showEditar   = true;
	}
	
	public void registrarParametro(){
		try{
			boolean error = false;
			if(StringUtils.trimToNull(this.persisteParametro.getId())==null){
				this.mostrarMensajeGlobal("ingreseIdentificador", "error");
				error = true;
			}
			if(!error){
				if(IInsertRices.saveParametro(this.persisteParametro)){
					this.listaParametro.add(this.persisteParametro);
					this.mostrarMensajeGlobal("parametroRegistradoExito", "exito");
					this.showCrear    = false;
					this.showConsulta = true;
				}
			}
		}catch(Exception e){
			IConstants.log.error(e.toString(),e);
		}
	}
	
	public void editarParametro(){
		try{
			try{
				boolean error = false;
				if(!error){
					if(IUpdateRices.updateParametro(this.cloneParametro)){
						this.mostrarMensajeGlobal("parametroActualizadoExito", "exito");
						this.persisteParametro.setId(this.cloneParametro.getId());
						this.persisteParametro.setValorNumerico(this.cloneParametro.getValorNumerico());
						this.persisteParametro.setTextCorto(this.cloneParametro.getTextCorto());
						this.persisteParametro.setTextLargo(this.cloneParametro.getTextLargo());
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

	public Parametro getPersisteParametro() {
		return persisteParametro;
	}

	public void setPersisteParametro(Parametro persisteParametro) {
		this.persisteParametro = persisteParametro;
	}

	public Parametro getCloneParametro() {
		return cloneParametro;
	}

	public void setCloneParametro(Parametro cloneParametro) {
		this.cloneParametro = cloneParametro;
	}

	public boolean isShowConsulta() {
		return showConsulta;
	}

	public boolean isShowCrear() {
		return showCrear;
	}

	public boolean isShowEditar() {
		return showEditar;
	}

	public List<Parametro> getListaParametro() {
		return listaParametro;
	}

}
