package co.com.rices;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;

@ManagedBean
public class ConsultarFuncionesAPI implements Serializable{

	// PRIVADOS

	// PUBLICOS

	/**
	 * 
	 */
	private static final long serialVersionUID = 522842611904552706L;

	/**
	 * Abre un di�logo modal
	 * 
	 * @param aVariableWidgetDelModal
	 */
	public void abrirModal(String aVariableWidgetDelModal) {
		try {

			RequestContext.getCurrentInstance().execute("PF('" + aVariableWidgetDelModal + "').show()");

		} catch (Exception e) {
			IConstants.log.error(e, e);
		}
	}

	/**
	 * Cierra un di�logo modal
	 * 
	 * @param aVariableWidgetDelModal
	 */
	public void cerrarModal(String aVariableWidgetDelModal) {
		try {

			RequestContext.getCurrentInstance().execute("PF('" + aVariableWidgetDelModal + "').hide()");

		} catch (Exception e) {
			IConstants.log.error(e, e);
		}
	}

	/**
	 * Obtiene un mensaje del properties para poderlo mostrar en capa de
	 * presentacion
	 * 
	 * @param aId
	 * @return mensaje
	 */
	public String getMensaje(String aId) {
		String mensaje = "[[" + aId + "]]";
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "mensaje");
			if (bundle != null) {
				mensaje = bundle.getString(aId);
			}

		} catch (Exception e) {

		}

		return mensaje;
	}

	public String getMensajeParametros(String mensaje, String[] args) {
		String mensajeSalida = "";
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle archivoGeneral = context.getApplication().getResourceBundle(context, "mensaje");
		mensajeSalida = archivoGeneral.getString(mensaje);
		mensajeSalida = MessageFormat.format(mensajeSalida, (Object[])args);
		return mensajeSalida;
	}

	public void mostrarMensajeGlobalParametros(String aIdProperties, String aTipo, String[] args) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (aTipo == null || (aTipo != null && aTipo.equals("exito"))) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, this.getMensaje("INFORMACION"), this.getMensajeParametros(aIdProperties,args)));
		} else if (aTipo == null || (aTipo != null && aTipo.equals("advertencia"))) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, this.getMensaje("INFORMACION"), this.getMensajeParametros(aIdProperties,args)));
		} else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getMensaje("INFORMACION"), this.getMensajeParametros(aIdProperties,args)));
		}
	}

	/**
	 * Muestra un mensaje en pantalla
	 * 
	 * @param aIdProperties
	 * @param aTipo
	 */
	public void mostrarMensajeGlobal(String aIdProperties, String aTipo) {
		FacesContext context = FacesContext.getCurrentInstance();
		if (aTipo == null || (aTipo != null && aTipo.equals("exito"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));

		} else if (aTipo == null || (aTipo != null && aTipo.equals("advertencia"))) {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));

		} else {

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getMensaje("INFORMACION"), this.getMensaje(aIdProperties)));
		}
	}

	/**
	 * Convierte una fecha a java.util
	 * 
	 * @param aFecha
	 * @return fechaCasteada
	 */
	public Date getFechaJavaUtil(Object aFecha) {
		Date fechaCasteada = null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

			if (aFecha != null) {

				fechaCasteada = formato.parse(((String) aFecha));

			}
		} catch (Exception e) {

		}
		return fechaCasteada;
	}

	/**
	 * Determina si un valor es menor o igual a cero
	 * 
	 * @param aValor
	 * @return ok
	 */
	public boolean isMenorIgualCero(BigDecimal aValor) {
		boolean ok = false;
		if (aValor != null && aValor.compareTo(new BigDecimal(0)) <= 0) {
			ok = true;
		}
		return ok;
	}

	/**
	 * Determina si es menor a cero
	 * 
	 * @param aValor
	 * @return ok
	 */
	public boolean isMenorCero(BigDecimal aValor) {
		boolean ok = false;
		if (aValor != null && aValor.compareTo(new BigDecimal(0)) < 0) {
			ok = true;
		}
		return ok;
	}

	/**
	 * Redondea un valor a n decimales
	 * 
	 * @param aValor
	 * @param aNumeroDecimales
	 * @return valor
	 */
	public BigDecimal getValorRedondeado(BigDecimal aValor, Integer aNumeroDecimales) {

		BigDecimal valor = new BigDecimal(0);
		try {

			valor = aValor.setScale(aNumeroDecimales, RoundingMode.HALF_UP);

		} catch (Exception e) {
			valor = new BigDecimal(0);
		}

		return valor;
	}

	/**
	 * Valida si una sarta esta vac�a o no
	 * 
	 * @param aSarta
	 * @return vacio
	 */
	public boolean isVacio(String aSarta) {
		boolean vacio = true;
		if (aSarta != null && !aSarta.trim().equals("")) {
			vacio = false;
		}
		return vacio;
	}

	/**
	 * Retorna la sarta en may�scula y sin espacios.
	 * 
	 * @param aSarta
	 * @return sarta
	 */
	public String getMayusculaSinEspacios(String aSarta) {
		String sarta = null;
		if (aSarta != null) {
			sarta = aSarta.toUpperCase().trim();
		}
		return sarta;

	}

	/**
	 * Obtiene fselecitems de estados
	 * 
	 * @return itemsEstados
	 */
	public List<SelectItem> getItemsEstados() {
		List<SelectItem> itemsEstados = new ArrayList<SelectItem>();
		itemsEstados.add(new SelectItem("", this.getMensaje("comboVacio")));
		itemsEstados.add(new SelectItem(IConstants.ACTIVO, this.getMensaje("ESTADO_ACTIVO")));
		itemsEstados.add(new SelectItem(IConstants.INACTIVO, this.getMensaje("ESTADO_INACTIVO")));

		return itemsEstados;
	}
	public String fechaCorta(Date fecha){
		String cadenaFormatoFecha = "MMM/dd/yyyy";
		SimpleDateFormat formatoFecha = new SimpleDateFormat(cadenaFormatoFecha);
		if (fecha != null) {
			return formatoFecha.format(fecha);
		}
		return "";
	}

	public String horaCorta(Date fecha){
		String cadenaFormatoFecha = "HH:mm:ss";
		SimpleDateFormat formatoFecha = new SimpleDateFormat(cadenaFormatoFecha);
		if (fecha != null) {
			return formatoFecha.format(fecha);
		}
		return "";
	}
}
