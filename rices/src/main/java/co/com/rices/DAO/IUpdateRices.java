package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.SQLException;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.objects.CouponCode;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.RiceMenu;
import co.com.rices.objects.StepDetail;

public interface IUpdateRices {
	
	public static boolean updateProduct(Product pProduct)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.products                                                    ");
			builder.append(" SET    product_name=?, description=?, state=?,                           ");
			builder.append("        ranking=?, image_name=?, product_type=?, open=?,                  ");
			builder.append("        closed=?, price=?, menu=?, content_type=?, texto=?, agrupa_menu=?,");
			builder.append("        content_type_big=?                                                ");
			builder.append(" WHERE  id=?;                                                             ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProduct.getName().trim());
				cs.setObject(2, pProduct.getDescription().trim());
				cs.setObject(3, pProduct.getState());
				cs.setObject(4, pProduct.getRanking());
				cs.setObject(5, pProduct.getImageName());
				cs.setObject(6, pProduct.getProductType());
				cs.setObject(7, new java.sql.Time(pProduct.getOpen().getTime()));
				cs.setObject(8, new java.sql.Time(pProduct.getClosed().getTime()));
				cs.setObject(9, pProduct.getPrice());
				cs.setObject(10, pProduct.getIdMenu());
				cs.setObject(11, pProduct.getContentType());
				cs.setObject(12, pProduct.getTexto());
				cs.setObject(13, pProduct.getAgrupaMenu());
				cs.setObject(14, pProduct.getContentTypeBig());
				cs.setInt(15, pProduct.getId());
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
	
	public static boolean updateProductStep(ProductStep pProductStep)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.product_steps         ");
			builder.append(" SET    select_type=?, state=?,     ");
			builder.append("        description=?, step_order=? ");
			builder.append(" WHERE  id=?;                       ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProductStep.getSelectType());
				cs.setObject(2, pProductStep.getState());
				cs.setObject(3, pProductStep.getDescription());
				cs.setObject(4, pProductStep.getStepOrder());
				cs.setInt(5, pProductStep.getId());
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
	
	public static boolean updateStepDetail(StepDetail pStepDetail)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.step_details ");
			builder.append(" SET    state=?, price=?   ");
			builder.append(" WHERE  id=?               ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pStepDetail.getState());
				cs.setObject(2, pStepDetail.getPrice());
				cs.setObject(3, pStepDetail.getId());
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
	
	public static boolean updateCouponState(CouponCode pCouponCode)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.coupon_codes ");
			builder.append(" SET    used = ?           ");
			builder.append(" WHERE  client_id = ?      ");
			builder.append(" AND    coupon = ?         ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setString(1, pCouponCode.getUsed());
				cs.setInt(2, pCouponCode.getClientId());
				cs.setString(3, pCouponCode.getCoupon());
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

	public static boolean updateRiceMenu(RiceMenu pRiceMenu)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.rices_menu                                   ");
			builder.append(" SET    description=?, orden=?, estado=?, open=?, closed=? ");
			builder.append(" WHERE  id=?;                                              ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pRiceMenu.getDescription());
				cs.setObject(2, pRiceMenu.getOrden());
				cs.setObject(3, pRiceMenu.getEstado());
				cs.setObject(4, new java.sql.Time(pRiceMenu.getOpen().getTime()));
				cs.setObject(5, new java.sql.Time(pRiceMenu.getClosed().getTime()));
				cs.setInt(6, pRiceMenu.getId());
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
