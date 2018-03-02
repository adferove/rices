package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;

public interface IActualizaRices {


	public static Integer registrarCliente(Cliente pCliente)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.clientes(                                               ");
			builder.append("         nombres_cliente, apellidos_cliente, correo_cliente,               ");
			builder.append("         direccion_cliente, telefono_cliente, barrio_cliente, registrado)  ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?)                                              ");
			builder.append(" RETURNING id_cliente;                                                     ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;  
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pCliente.getNombre());
				cs.setObject(2, pCliente.getApellido());
				cs.setString(3, pCliente.getEmail());
				cs.setString(4, pCliente.getDireccion());
				cs.setString(5, pCliente.getCelular());
				cs.setString(6, pCliente.getBarrio());
				cs.setObject(7, pCliente.isGuardaDatos());
				cs.execute();
				rs = cs.getResultSet();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}
	
	public static boolean actualizarCliente(Cliente pCliente)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.clientes                                                          ");
			builder.append(" SET    nombres_cliente=?, apellidos_cliente=?, correo_cliente=?,               ");
			builder.append("        direccion_cliente=?, telefono_cliente=?, barrio_cliente=?, registrado=? ");
			builder.append(" WHERE  id_cliente = ?                                                          ");
			
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pCliente.getNombre());
				cs.setObject(2, pCliente.getApellido());
				cs.setString(3, pCliente.getEmail());
				cs.setString(4, pCliente.getDireccion());
				cs.setString(5, pCliente.getCelular());
				cs.setString(6, pCliente.getBarrio());
				cs.setObject(7, pCliente.isGuardaDatos());
				cs.setInt(8, pCliente.getId());
				int value = cs.executeUpdate();
				if(value==1){
					resultado = true;
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}
	
	public static Integer registrarPedido(Pedido pPedido)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.pedidos(                                           ");
			builder.append("         id_cliente, total_pedido, subtotal_pedido, cargo_domicilio,  ");
			builder.append("         iva, fecha_pedido, hora_pedido, estado_pedido)               ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?)                                      ");
			builder.append(" RETURNING id_pedido;                                                 ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet         rs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pPedido.getCliente().getId());
				cs.setObject(2, pPedido.getTotal());
				cs.setObject(3, pPedido.getSubtotal());
				cs.setObject(4, pPedido.getCargoDomicilio());
				cs.setObject(5, pPedido.getIva());
				cs.setObject(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				cs.setObject(7, new java.sql.Time(Calendar.getInstance().getTime().getTime()));
				cs.setString(8, pPedido.getEstado());
				cs.execute();
				rs = cs.getResultSet();
				if(rs.next()){
					resultado = rs.getInt(1);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}
	
	public static boolean registrarDetallePedido(DetallePedido pDetallePedido)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.detalle_pedidos(        ");
			builder.append("         id_pedido, id_producto, cantidad, "); 
			builder.append("         precio, observacion)              ");
			builder.append(" VALUES (?, ?, ?, ?, ?);                   ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pDetallePedido.getIdpedido());
				cs.setObject(2, pDetallePedido.getProducto().getId());
				cs.setObject(3, pDetallePedido.getCantidad());
				cs.setObject(4, pDetallePedido.getPrecio());
				cs.setObject(5, pDetallePedido.getObservacion());
				int value = cs.executeUpdate();
				if(value==1){
					resultado = true;
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}

}
