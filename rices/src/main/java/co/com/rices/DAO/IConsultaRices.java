package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.beans.Cliente;
import co.com.rices.beans.Cupon;
import co.com.rices.beans.CuponCliente;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.EstructuraMenu;
import co.com.rices.beans.Pedido;
import co.com.rices.beans.Producto;
import co.com.rices.beans.ProductoPrecio;
import co.com.rices.beans.Usuario;

public interface IConsultaRices {

	public static List<DetallePedido> getDetallePedidoPorId(Integer pId)  throws Exception{
		List<DetallePedido> resultado=new ArrayList<DetallePedido>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id_detalle_pedido, id_pedido, id_producto, cantidad, observacion, precio ");
			builder.append(" FROM   rices.detalle_pedidos ");
			builder.append(" WHERE  id_pedido = ? ");

			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pId);
				rs = cs.executeQuery();
				while(rs.next()){
					DetallePedido detallepedido = new DetallePedido();
					detallepedido.setId(rs.getInt("id_detalle_pedido"));
					detallepedido.setIdpedido(rs.getInt("id_pedido"));
					detallepedido.setIdproducto(rs.getInt("id_producto"));
					detallepedido.setCantidad(rs.getInt("cantidad"));
					detallepedido.setObservacion(rs.getString("observacion"));
					detallepedido.setPrecio(rs.getBigDecimal("precio"));
					resultado.add(detallepedido);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}

	public static List<Producto> getProductoPorEstado(String pEstado)  throws Exception{
		List<Producto> resultados=new ArrayList<Producto>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id_producto, nombre_producto, descripcion_producto, fecha_creacion, "); 
			builder.append("        estado, login_usuario, ranking, ruta_imagen                         ");
			builder.append(" FROM   rices.productos                                                     ");
			builder.append(" WHERE  35 = 35   ");
			Map<Integer, Object> parametros=new HashMap<Integer,Object>();
			int i=1;
			if (pEstado!=null && !pEstado.trim().equals("")){
				builder.append(" AND estado = ? ");
				parametros.put(i++, pEstado);
			}
			builder.append("ORDER BY estado ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: parametros.keySet()){
					cs.setObject(j, parametros.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Producto producto = new Producto();
					producto.setId(rs.getInt("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					producto.setDescripcion(rs.getString("descripcion_producto"));
					producto.setFechacreacion(rs.getDate("fecha_creacion"));
					producto.setEstado(rs.getString("estado"));
					producto.setLoginusuario(rs.getString("login_usuario"));
					producto.setRating(rs.getInt("ranking"));
					producto.setRutaImagen(rs.getString("ruta_imagen"));
					producto.setProductoPrecio(IConsultaRices.getProductoPrecioPorProducto(producto.getId()));
					if(producto.getProductoPrecio()==null){
						producto.setProductoPrecio(new ProductoPrecio());
					}
					resultados.add(producto);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}

	public static List<Pedido> getPedidoPorEstado(String pEstado)  throws Exception{
		List<Pedido> resultados=new ArrayList<Pedido>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append("SELECT id_pedido, id_cliente, total_pedido, fecha_pedido, hora_pedido, ");
			builder.append("       estado_pedido, descuento,subtotal_pedido                        ");
			builder.append("FROM   rices.Pedidos                                                   ");
			builder.append("WHERE  35 = 35                                                         ");
			Map<Integer, Object> parametros=new HashMap<Integer,Object>();
			int i=1;
			if (pEstado!=null && !pEstado.trim().equals("")){
				builder.append(" AND estado_pedido=?");
				parametros.put(i++, pEstado);
			}
			builder.append("ORDER  BY fecha_pedido ASC,  hora_pedido ASC");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: parametros.keySet()){
					cs.setObject(j, parametros.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Pedido pedido = new Pedido();
					pedido.setId(rs.getInt("id_pedido"));
					pedido.setIdcliente(rs.getInt("id_cliente"));
					pedido.setTotal(rs.getBigDecimal("total_pedido"));
					pedido.setFecha(rs.getDate("fecha_pedido"));
					pedido.setHora(rs.getTime("hora_pedido"));
					pedido.setEstado(rs.getString("estado_pedido"));
					pedido.setDescuento(rs.getBigDecimal("descuento"));
					pedido.setSubtotal(rs.getBigDecimal("subtotal_pedido"));
					resultados.add(pedido);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}


	public static List<Cliente> getClientesPorParametro(Integer pId, String pEmail)throws Exception{
		List<Cliente> resultados = new ArrayList<Cliente>();
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT id_cliente, nombres_cliente, apellidos_cliente, correo_cliente,"); 
			builder.append(" direccion_cliente, telefono_cliente, barrio_cliente,registrado        ");
			builder.append(" FROM   rices.clientes                                                 ");
			builder.append(" WHERE  35 = 35                                                        ");
			Map<Integer, Object> parametros = new HashMap<Integer, Object>();
			int i = 1;
			if(pId!=null){
				builder.append(" AND id_cliente = ? ");
				parametros.put(i++, pId);
			}
			if(pEmail!=null && !pEmail.trim().equals("")){
				builder.append(" AND correo_cliente = ? ");
				parametros.put(i++, pEmail.trim().toLowerCase());
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: parametros.keySet()){
					cs.setObject(j, parametros.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Cliente cliente = new Cliente();
					cliente.setId(rs.getInt("id_cliente"));
					cliente.setNombre(rs.getString("nombres_cliente"));
					cliente.setApellido(rs.getString("apellidos_cliente"));
					cliente.setEmail(rs.getString("correo_cliente"));
					cliente.setDireccion(rs.getString("direccion_cliente"));
					cliente.setBarrio(rs.getString("barrio_cliente"));
					cliente.setCelular(rs.getString("telefono_cliente"));
					cliente.setGuardaDatos(rs.getBoolean("registrado"));
					resultados.add(cliente);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}

	public static ProductoPrecio getProductoPrecioPorProducto(Integer pIdProducto)throws Exception{
		ProductoPrecio resultado = null;
		try{
			StringBuilder builder = new StringBuilder();

			builder.append(" SELECT id_producto_precio, id_producto, precio_producto, estado_producto_precio, "); 
			builder.append(" fecha_creacion_producto_precio, fecha_actualizacion_producto_precio,             ");
			builder.append(" usuario_creacion, usuario_actualiza                                              ");
			builder.append(" FROM rices.producto_precios                                                      ");
			builder.append(" WHERE  id_producto = ?                                                           ");
			builder.append(" AND    estado_producto_precio = ?                                                ");
			builder.append(" ORDER  BY fecha_creacion_producto_precio DESC                                    ") ;


			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pIdProducto);
				cs.setString(2, "A");
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new ProductoPrecio();
					resultado.setId(rs.getInt("id_producto_precio"));
					resultado.setIdProducto(rs.getInt("id_producto"));
					resultado.setPrecio(rs.getBigDecimal("precio_producto"));
					resultado.setEstado(rs.getString("estado_producto_precio"));
					resultado.setFechaCrea(rs.getDate("fecha_creacion_producto_precio"));
					resultado.setFechaActualiza(rs.getDate("fecha_actualizacion_producto_precio"));
					resultado.setUsuarioCrea(rs.getString("usuario_creacion"));
					resultado.setUsuarioActualiza(rs.getString("usuario_actualiza"));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}	
	
	public static ProductoPrecio getPrecioPorProductoActivo(Integer pIdProducto)throws Exception{
		ProductoPrecio resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT precio_producto                        "); 
			builder.append(" FROM   rices.producto_precios                 ");
			builder.append(" WHERE  id_producto = ?                        ");
			builder.append(" AND    estado_producto_precio = ?             ");
			builder.append(" ORDER  BY fecha_creacion_producto_precio DESC ") ;
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pIdProducto);
				cs.setString(2, "A");
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new ProductoPrecio();
					resultado.setPrecio(rs.getBigDecimal("precio_producto"));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}	
	
	public static List<Producto> getProductoActivo()  throws Exception{
		List<Producto> resultados=new ArrayList<Producto>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT p.id_producto, p.nombre_producto, p.descripcion_producto, "); 
			builder.append("        p.ranking, p.ruta_imagen, v.precio_producto               ");             
			builder.append(" FROM   rices.productos p, rices.producto_precios v               ");                                         
			builder.append(" WHERE  v.id_producto = p.id_producto                             ");
			builder.append(" AND    p.estado = ?                                              ");
			builder.append(" AND    v.estado_producto_precio = ?                              ");
			builder.append(" ORDER  BY v.fecha_creacion_producto_precio DESC                  "); 
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, "A");
				cs.setString(2, "A");
				rs = cs.executeQuery();
				while(rs.next()){
					Producto producto = new Producto();
					producto.setId(rs.getInt("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					producto.setDescripcion(rs.getString("descripcion_producto"));
					producto.setRating(rs.getInt("ranking"));
					producto.setRutaImagen(rs.getString("ruta_imagen"));
					producto.setProductoPrecio(new ProductoPrecio());
					producto.getProductoPrecio().setPrecio(rs.getBigDecimal("precio_producto"));
					resultados.add(producto);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static List<EstructuraMenu> getEsctructuraPorRol(List<Integer> pRol)throws Exception{
		List<EstructuraMenu> resultados = new ArrayList<EstructuraMenu>();
		try{
			Conexion conexion = new Conexion();
			CallableStatement cs  = null;
			ResultSet         rs  = null;
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT m.id_menu, m.descripcion, m.orden, "); 
			builder.append("    m.url, m.estado, m.id_menu_sup         ");
			builder.append(" FROM   rices.menus m, rices.menu_rol r    ");
			builder.append(" WHERE  m.id_menu = r.id_menu              "); 
			builder.append(" AND    m.estado = ?                       ");
			builder.append(" AND    r.estado = ?                       ");
			if(pRol!=null && pRol.size()>0){
				builder.append(" AND    r.id_rol IN (?");
				for(int i=1; i < pRol.size(); i++){
					builder.append(",?");
				}
				builder.append(")");
			}
			try{
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, "A");
				cs.setString(2, "A");
				if(pRol!=null && pRol.size()>0){
					int i = 3;
					for(Integer value: pRol){
						cs.setInt(i++, value);
					}
				}
				rs = cs.executeQuery();
				while(rs.next()){
					EstructuraMenu estructuraMenu = new EstructuraMenu();
					estructuraMenu.setIdMenu(rs.getInt("id_menu"));
					estructuraMenu.setDescripcion(rs.getString("descripcion"));
					estructuraMenu.setOrden(rs.getInt("orden"));
					if(rs.getObject("id_menu_sup")!=null){
						estructuraMenu.setIdMenuSuperior(rs.getInt("id_menu_sup"));
					}
					estructuraMenu.setUrl(rs.getString("url"));
					estructuraMenu.setEstado(rs.getString("estado"));
					resultados.add(estructuraMenu);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static Cupon getCuponActivoByCupon(String pCupon)throws Exception{
		Cupon resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append("  SELECT cupon, descripcion, porcentaje, estado, "); 
			builder.append("         usuario_crea, fecha_crea, hora_crea     ");
			builder.append("  FROM   rices.cupones                           ");
			builder.append("  WHERE  cupon   = ?                             ");   
			builder.append("  AND    estado  = ?                             ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pCupon);
				cs.setString(2, "A");
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new Cupon();
					resultado.setCupon(rs.getString("cupon"));
					resultado.setDescripcion(rs.getString("descripcion"));
					resultado.setPorcentaje(rs.getBigDecimal("porcentaje"));
					resultado.setEstado(rs.getString("estado"));
					resultado.setUsuarioCrea(rs.getString("usuario_crea"));
					resultado.setFechaCrea(rs.getDate("fecha_crea"));
					resultado.setHoraCrea(rs.getDate("hora_crea"));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}
	
	public static CuponCliente getCuponCliente(Integer pIdCliente, String pCupon)throws Exception{
		CuponCliente resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append("  SELECT d.descripcion, d.porcentaje, c.usado    ");
			builder.append("  FROM   rices.cupones d, rices.cupon_clientes c ");
			builder.append("  WHERE  c.cupon = d.cupon                       ");
			builder.append("  AND    c.id_cliente = ?                        ");
			builder.append("  AND    d.cupon      = ?                        ");
			builder.append("  AND    d.estado     = ?                        ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(   1, pIdCliente);
				cs.setString(2, pCupon);
				cs.setString(3, "A");
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new CuponCliente();
					resultado.setIdCliente(pIdCliente);
					resultado.setCupon(pCupon);
					resultado.setUsado(rs.getString("usado"));
					resultado.setTransientCupon(new Cupon());
					resultado.getTransientCupon().setDescripcion(rs.getString("descripcion"));
					resultado.getTransientCupon().setPorcentaje(rs.getBigDecimal("porcentaje"));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}	
	
	public static Usuario getUsuarioByLogin(String pLogin)throws Exception{
		Usuario resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT login_usuario, documento_id, primer_nombre, segundo_nombre, primer_apellido,   "); 
			builder.append("        segundo_apellido, fecha_creacion, tipo_doc_id, password_usuario, email_usuario ");
			builder.append(" FROM   rices.usuarios                                                                 ");
			builder.append(" WHERE  login_usuario = ?                                                              ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pLogin);
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new Usuario();
					resultado.setLogin(rs.getString("login_usuario"));
					resultado.setDocumento(rs.getLong("documento_id"));
					resultado.setPrimernombre(rs.getString("primer_nombre"));
					resultado.setSegundonombre(rs.getString("segundo_nombre"));
					resultado.setPrimerapellido(rs.getString("primer_apellido"));
					resultado.setSegundoapellido(rs.getString("segundo_apellido"));
					resultado.setFecha(rs.getDate("fecha_creacion"));
					resultado.setTipodocumento(rs.getShort("tipo_doc_id"));
					resultado.setPassword(rs.getString("password_usuario"));
					resultado.setEmail(rs.getString("email_usuario"));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultado;
	}
	
	public static List<Integer> getRolesByLogin(String pLogin)  throws Exception{
		List<Integer> resultados=new ArrayList<Integer>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append("  SELECT id_rol              ");
			builder.append("  FROM   rices.roles_usuario ");
			builder.append("  WHERE  login_usuario = ?   "); 
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pLogin);
				rs = cs.executeQuery();
				while(rs.next()){
					resultados.add(rs.getInt(1));
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static Map<Integer,Producto> getMapProductoActivo()throws Exception{
		Map<Integer,Producto> resultados = new HashMap<Integer,Producto>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT p.id_producto, p.nombre_producto, p.descripcion_producto, "); 
			builder.append("        p.ranking, p.ruta_imagen, v.precio_producto               ");             
			builder.append(" FROM   rices.productos p, rices.producto_precios v               ");                                         
			builder.append(" WHERE  v.id_producto = p.id_producto                             ");
			builder.append(" AND    p.estado = ?                                              ");
			builder.append(" AND    v.estado_producto_precio = ?                              ");
			builder.append(" ORDER  BY v.fecha_creacion_producto_precio DESC                  "); 
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, "A");
				cs.setString(2, "A");
				rs = cs.executeQuery();
				while(rs.next()){
					Producto producto = new Producto();
					producto.setId(rs.getInt("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					producto.setDescripcion(rs.getString("descripcion_producto"));
					producto.setRating(rs.getInt("ranking"));
					producto.setRutaImagen(rs.getString("ruta_imagen"));
					producto.setProductoPrecio(new ProductoPrecio());
					producto.getProductoPrecio().setPrecio(rs.getBigDecimal("precio_producto"));
					resultados.put(producto.getId(), producto);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static List<Pedido> getPedidoPorParametros(String pEstado, Date pFechaDesde, Date pFechaHasta)  throws Exception{
		List<Pedido> resultados=new ArrayList<Pedido>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append("SELECT id_pedido, id_cliente, total_pedido, fecha_pedido, hora_pedido, ");
			builder.append("       estado_pedido, descuento,subtotal_pedido                        ");
			builder.append("FROM   rices.Pedidos                                                   ");
			builder.append("WHERE  35 = 35                                                         ");
			Map<Integer, Object> parametros=new HashMap<Integer,Object>();
			int i=1;
			if (pEstado!=null && !pEstado.trim().equals("")){
				builder.append(" AND estado_pedido=?");
				parametros.put(i++, pEstado);
			}
			if(pFechaDesde!=null){
				builder.append(" AND fecha_pedido >= ? ");
				parametros.put(i++, new java.sql.Date(pFechaDesde.getTime()));
			}
			if(pFechaHasta!=null){
				builder.append(" AND fecha_pedido <= ? ");
				parametros.put(i++, new java.sql.Date(pFechaHasta.getTime()));
			}
			builder.append("ORDER  BY fecha_pedido ASC,  hora_pedido ASC LIMIT 100 ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: parametros.keySet()){
					cs.setObject(j, parametros.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Pedido pedido = new Pedido();
					pedido.setId(rs.getInt("id_pedido"));
					pedido.setIdcliente(rs.getInt("id_cliente"));
					pedido.setTotal(rs.getBigDecimal("total_pedido"));
					pedido.setFecha(rs.getDate("fecha_pedido"));
					pedido.setHora(rs.getTime("hora_pedido"));
					pedido.setEstado(rs.getString("estado_pedido"));
					pedido.setDescuento(rs.getBigDecimal("descuento"));
					pedido.setSubtotal(rs.getBigDecimal("subtotal_pedido"));
					resultados.add(pedido);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static Map<Integer,Producto> getMapProductoTodos()throws Exception{
		Map<Integer,Producto> resultados = new HashMap<Integer,Producto>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT p.id_producto, p.nombre_producto "); 
			builder.append(" FROM   rices.productos p                ");                                         
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				rs = cs.executeQuery();
				while(rs.next()){
					Producto producto = new Producto();
					producto.setId(rs.getInt("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					resultados.put(producto.getId(), producto);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
	
	public static List<Cliente> getClientesPorNombreEmail(String pNombre, String pEmail)throws Exception{
		List<Cliente> resultados = new ArrayList<Cliente>();
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT id_cliente, nombres_cliente, apellidos_cliente, correo_cliente,"); 
			builder.append(" direccion_cliente, telefono_cliente, barrio_cliente,registrado        ");
			builder.append(" FROM   rices.clientes                                                 ");
			builder.append(" WHERE  35 = 35                                                        ");
			Map<Integer, Object> parametros = new HashMap<Integer, Object>();
			int i = 1;
			if(pNombre!=null && !pNombre.trim().equals("")){
				builder.append(" AND nombres_cliente LIKE  ? ");
				parametros.put(i++, "%" +pNombre.trim().toUpperCase()+ "%");
			}
			if(pEmail!=null && !pEmail.trim().equals("")){
				builder.append(" AND correo_cliente LIKE ? ");
				parametros.put(i++, "%" +pEmail.trim().toLowerCase()+ "%");
			}
			builder.append(" ORDER BY nombres_cliente,apellidos_cliente desc");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: parametros.keySet()){
					cs.setObject(j, parametros.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Cliente cliente = new Cliente();
					cliente.setId(rs.getInt("id_cliente"));
					cliente.setNombre(rs.getString("nombres_cliente"));
					cliente.setApellido(rs.getString("apellidos_cliente"));
					cliente.setEmail(rs.getString("correo_cliente"));
					cliente.setDireccion(rs.getString("direccion_cliente"));
					cliente.setBarrio(rs.getString("barrio_cliente"));
					cliente.setCelular(rs.getString("telefono_cliente"));
					cliente.setGuardaDatos(rs.getBoolean("registrado"));
					resultados.add(cliente);
				}
			}catch(SQLException sq){
				IConstants.log.error(sq.toString(),sq);
			}finally{
				rs.close();
				cs.close();
				conexion.cerrarConexion();
			}
		}catch(Exception e){
			throw new Exception(e);
		}
		return resultados;
	}
}
