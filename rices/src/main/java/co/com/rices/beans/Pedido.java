package co.com.rices.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import co.com.rices.objects.CouponCode;

public class Pedido implements Serializable{

	private static final long serialVersionUID = -7004622626932386721L;
	
	private Integer             id;
	private Integer             idcliente;
	private BigDecimal          total;
	private Date                fecha;
	private Date                hora;
	private String              estado;
	private BigDecimal          subtotal;
	private BigDecimal          cargoDomicilio;
	private BigDecimal          iva;
	private Cliente             cliente;
	private BigDecimal          descuento;
	private BigDecimal          totalDescuento;
	private BigDecimal          multiplicador;
	private CuponCliente        transientCuponCliente;
	private List<DetallePedido> listadoDetalle;
	
	private String              nombreCliente;
	private String              direccionCliente;
	private String              celularCliente;
	private Integer             codigoCiudad;
	
	private CouponCode          transientCouponCode;
	private String              cityName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIdcliente() {
		return idcliente;
	}
	public void setIdcliente(Integer idcliente) {
		this.idcliente = idcliente;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Date getHora() {
		return hora;
	}
	public void setHora(Date hora) {
		this.hora = hora;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getCargoDomicilio() {
		return cargoDomicilio;
	}
	public void setCargoDomicilio(BigDecimal cargoDomicilio) {
		this.cargoDomicilio = cargoDomicilio;
	}
	public BigDecimal getIva() {
		return iva;
	}
	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public BigDecimal getDescuento() {
		return descuento;
	}
	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}
	public BigDecimal getMultiplicador() {
		return multiplicador;
	}
	public void setMultiplicador(BigDecimal multiplicador) {
		this.multiplicador = multiplicador;
	}
	public CuponCliente getTransientCuponCliente() {
		return transientCuponCliente;
	}
	public void setTransientCuponCliente(CuponCliente transientCuponCliente) {
		this.transientCuponCliente = transientCuponCliente;
	}
	public List<DetallePedido> getListadoDetalle() {
		return listadoDetalle;
	}
	public void setListadoDetalle(List<DetallePedido> listadoDetalle) {
		this.listadoDetalle = listadoDetalle;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getDireccionCliente() {
		return direccionCliente;
	}
	public void setDireccionCliente(String direccionCliente) {
		this.direccionCliente = direccionCliente;
	}
	public String getCelularCliente() {
		return celularCliente;
	}
	public void setCelularCliente(String celularCliente) {
		this.celularCliente = celularCliente;
	}
	public Integer getCodigoCiudad() {
		return codigoCiudad;
	}
	public void setCodigoCiudad(Integer codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}
	public CouponCode getTransientCouponCode() {
		return transientCouponCode;
	}
	public void setTransientCouponCode(CouponCode transientCouponCode) {
		this.transientCouponCode = transientCouponCode;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public BigDecimal getTotalDescuento() {
		return totalDescuento;
	}
	public void setTotalDescuento(BigDecimal totalDescuento) {
		this.totalDescuento = totalDescuento;
	}
	
}
