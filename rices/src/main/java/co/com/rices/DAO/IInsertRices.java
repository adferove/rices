package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.beans.DetallePedido;
import co.com.rices.beans.Pedido;
import co.com.rices.objects.Complement;
import co.com.rices.objects.CouponCode;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

public interface IInsertRices {

	public static Integer saveProduct(Product pProduct)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.products(product_name, "); 
			builder.append("         description, creation_date,      ");
			builder.append("         state, login_usuario, ranking,   ");
			builder.append("         image_name, product_type, open,  ");
			builder.append("         closed, price)                   ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			builder.append(" RETURNING id;                            ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;  
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProduct.getName().trim());
				cs.setObject(2, pProduct.getDescription().trim());
				cs.setObject(3, new java.sql.Date(pProduct.getCreationDate().getTime()));
				cs.setObject(4, pProduct.getState());
				cs.setObject(5, pProduct.getLoginUsuario());
				cs.setObject(6, pProduct.getRanking());
				cs.setObject(7,  pProduct.getImageName());
				cs.setObject(8,  pProduct.getProductType());
				cs.setObject(9,  new java.sql.Time(pProduct.getOpen().getTime()));
				cs.setObject(10, new java.sql.Time(pProduct.getClosed().getTime()));
				cs.setObject(11, pProduct.getPrice());
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
	
	public static Integer saveProductStep(ProductStep pProductStep)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			 
			builder.append(" INSERT INTO rices.product_steps(       ");
			builder.append("        product_id, select_type,        ");
			builder.append("        state, description, step_order) ");
			builder.append(" VALUES (?, ?, ?, ?, ?)                 "); 
			builder.append(" RETURNING id;                          ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;  
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProductStep.getProductId());
				cs.setObject(2, pProductStep.getSelectType());
				cs.setObject(3, pProductStep.getState());
				cs.setObject(4, pProductStep.getDescription());
				cs.setObject(5, pProductStep.getStepOrder());
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
	
	public static Integer saveStepDetail(StepDetail pStepDetail)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			 
			
			builder.append(" INSERT INTO rices.step_details( ");
			builder.append("         product_step_id,        ");
			builder.append("         selected_product_id,    "); 
			builder.append("         state, price)           ");
			builder.append(" VALUES (?, ?, ?, ?)             ");
			builder.append(" RETURNING id;                   ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;  
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pStepDetail.getProductStepId());
				cs.setObject(2, pStepDetail.getSelectedProductId());
				cs.setObject(3, pStepDetail.getState());
				cs.setObject(4, pStepDetail.getPrice());
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
	
	public static boolean saveCouponCode(CouponCode pCouponCode)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.coupon_codes(                  ");
			builder.append("             client_id, coupon, used, percentage) ");
			builder.append(" VALUES      (?, ?, ?, ?);                        "); 
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pCouponCode.getClientId());
				cs.setString(2, pCouponCode.getCoupon());
				cs.setString(3, pCouponCode.getUsed());
				cs.setBigDecimal(4, pCouponCode.getPercentage());
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
	
	public static Integer saveOrderDetail(DetallePedido pDetallePedido)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.detalle_pedidos(         ");
			builder.append("         id_pedido, id_producto, cantidad,  "); 
			builder.append("         precio, observacion, product_price)");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?)                  ");
			builder.append(" RETURNING id_detalle_pedido;               ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet         rs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pDetallePedido.getIdpedido());
				cs.setObject(2, pDetallePedido.getMainProduct().getId());
				cs.setObject(3, pDetallePedido.getCantidad());
				cs.setObject(4, pDetallePedido.getPrecio());
				cs.setObject(5, pDetallePedido.getObservacion());
				cs.setObject(6, pDetallePedido.getMainProduct().getPrice());
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
	
	public static Integer savePurchaseOrder(Pedido pPedido)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.pedidos(                                                ");
			builder.append("         id_cliente, total_pedido, subtotal_pedido, cargo_domicilio,       ");
			builder.append("         iva, fecha_pedido, hora_pedido, estado_pedido, descuento,         ");
			builder.append("         nombre_cliente, direccion_cliente, celular_cliente, codigo_ciudad)");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)                            ");
			builder.append(" RETURNING id_pedido;                                                      ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet         rs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, null);
				cs.setObject(2, pPedido.getTotal());
				cs.setObject(3, pPedido.getSubtotal());
				cs.setObject(4, pPedido.getCargoDomicilio());
				cs.setObject(5, pPedido.getIva());
				cs.setObject(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
				cs.setObject(7, new java.sql.Time(Calendar.getInstance().getTime().getTime()));
				cs.setString(8, pPedido.getEstado());
				cs.setObject(9, pPedido.getDescuento());
				cs.setObject(10, pPedido.getNombreCliente().trim());
				cs.setObject(11, pPedido.getDireccionCliente().trim());
				cs.setObject(12, pPedido.getCelularCliente());
				cs.setObject(13, pPedido.getCodigoCiudad());
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
	
	public static boolean saveComplement(Complement pComplement)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.complements( ");
			builder.append("         id_detalle_pedido,     ");
			builder.append("         id_product,            ");
			builder.append("         id_product_step,       ");
			builder.append("         price)                 ");
			builder.append(" VALUES (?, ?, ?, ?);           ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pComplement.getDetailId());
				cs.setObject(2, pComplement.getSelectedProductId());
				cs.setObject(3, pComplement.getProductStepId());
				cs.setObject(4, pComplement.getPrice());
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
