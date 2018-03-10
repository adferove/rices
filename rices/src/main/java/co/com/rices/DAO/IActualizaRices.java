package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.CuponCliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;
import co.com.rices.beans.ProductoPrecio;

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
			builder.append("         iva, fecha_pedido, hora_pedido, estado_pedido, descuento)    ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)                                   ");
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
				cs.setObject(9, pPedido.getDescuento());
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
	
	public static Integer registrarProducto(Producto pProducto)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();

			builder.append(" INSERT INTO rices.productos(                                   ");
			builder.append("         nombre_producto, descripcion_producto, fecha_creacion, "); 
			builder.append("         estado, login_usuario, ranking, ruta_imagen)           ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?)                                   ");
			builder.append(" RETURNING id_producto;                                         ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet         rs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProducto.getNombre().trim());
				cs.setObject(2, pProducto.getDescripcion().trim());
				cs.setObject(3, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				cs.setObject(4, pProducto.getEstado());
				cs.setObject(5, pProducto.getLoginusuario().trim());
				cs.setObject(6, pProducto.getRating());
				cs.setObject(7, pProducto.getRutaImagen());
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

	public static Integer registrarProductoPrecio(ProductoPrecio pProductoPrecio)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			
			builder.append(" INSERT INTO rices.producto_precios(                                           ");
			builder.append("         id_producto, precio_producto, estado_producto_precio,                 ");
			builder.append("         fecha_creacion_producto_precio, fecha_actualizacion_producto_precio,  ");
			builder.append("         usuario_creacion, usuario_actualiza)                                  ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?)                                                  ");
			builder.append(" RETURNING id_producto_precio;                                         ");
			
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet         rs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pProductoPrecio.getIdProducto());
				cs.setObject(2, pProductoPrecio.getPrecio());
				cs.setObject(3, pProductoPrecio.getEstado());
				cs.setObject(4, new java.sql.Date(pProductoPrecio.getFechaCrea().getTime()));
				cs.setObject(5, new java.sql.Date(pProductoPrecio.getFechaActualiza().getTime()));
				cs.setObject(6, pProductoPrecio.getUsuarioCrea().trim());
				cs.setObject(7, pProductoPrecio.getUsuarioActualiza().trim());
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
	
	public static boolean actualizarProductoPrecio(ProductoPrecio pProductoPrecio)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			
			builder.append(" UPDATE rices.producto_precios                    ");
			builder.append(" SET    estado_producto_precio = ?,               "); 
			builder.append("        fecha_actualizacion_producto_precio = ?,  ");
			builder.append("        usuario_actualiza = ?                     ");
			builder.append(" WHERE  id_producto_precio = ?                    ");
			
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pProductoPrecio.getEstado().toUpperCase());
				cs.setObject(2, new java.sql.Date(pProductoPrecio.getFechaActualiza().getTime()));
				cs.setObject(3, pProductoPrecio.getUsuarioActualiza().toLowerCase());
				cs.setInt(4, pProductoPrecio.getId());
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
	
	public static boolean actualizarProducto(Producto pProducto)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			
			builder.append(" UPDATE rices.productos                             ");
			builder.append(" SET    nombre_producto=?, descripcion_producto=?,  ");
			builder.append("        estado=?, ranking=?, ruta_imagen=?          ");
			builder.append(" WHERE id_producto = ?                              ");
			
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProducto.getNombre().trim());
				cs.setObject(2, pProducto.getDescripcion().trim());
				cs.setObject(3, pProducto.getEstado());
				cs.setObject(4, pProducto.getRating());
				cs.setObject(5, pProducto.getRutaImagen());
				cs.setInt(6, pProducto.getId());
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
	
	public static boolean actualizarEstadoCupon(CuponCliente pCuponCliente)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.cupon_clientes ");
			builder.append(" SET    usado=?              ");
			builder.append(" WHERE  id_cliente=?         ");
			builder.append(" AND    cupon=?              ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pCuponCliente.getUsado());
				cs.setInt(2, pCuponCliente.getIdCliente());
				cs.setString(3, pCuponCliente.getCupon());
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
	
	public static boolean registrarCuponCliente(CuponCliente pCuponCliente)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.cupon_clientes(  ");
			builder.append("          id_cliente, cupon, usado) ");
			builder.append(" VALUES   (?, ?, ?);                "); 
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pCuponCliente.getIdCliente());
				cs.setString(2, pCuponCliente.getCupon());
				cs.setString(3, pCuponCliente.getUsado());
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
	
	public static boolean actualizarPasswordByLogin(String pPassword, String pLogin)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.usuarios     ");
			builder.append(" SET    password_usuario=? ");
			builder.append(" WHERE  login_usuario=?    ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pPassword);
				cs.setString(2, pLogin);
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
	
	public static boolean actualizarEstadoPedido(Pedido pPedido)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.pedidos     ");
			builder.append(" SET    estado_pedido = ? ");
			builder.append(" WHERE  id_pedido = ?     ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pPedido.getEstado());
				cs.setInt(2, pPedido.getId());
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
