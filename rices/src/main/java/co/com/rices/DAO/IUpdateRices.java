package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.SQLException;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;
import co.com.rices.objects.Product;

public interface IUpdateRices {
	
	public static boolean updateProduct(Product pProduct)throws Exception{
		boolean resultado = false;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" UPDATE rices.products                                    ");
			builder.append(" SET    product_name=?, description=?, state=?,           ");
			builder.append("        ranking=?, image_name=?, product_type=?, open=?,  ");
			builder.append("        closed=?, price=?                                 "); 
			builder.append(" WHERE  id=?;                                             ");
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
				cs.setInt(10, pProduct.getId());
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

}
