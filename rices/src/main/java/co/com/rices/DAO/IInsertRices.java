package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.Product;

public interface IInsertRices {

	public static Integer saveProduct(Product pProduct)throws Exception{
		Integer resultado = null;
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" INSERT INTO rices.products(product_name, "); 
			builder.append("         description, creation_date,      ");
			builder.append("         state, login_usuario, ranking,   ");
			builder.append("         image_name, product_type, open,  ");
			builder.append("         closed)                          ");
			builder.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)    ");
			builder.append(" RETURNING id;                            ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;  
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, pProduct.getName().trim().toUpperCase());
				cs.setObject(2, pProduct.getDescription().trim());
				cs.setObject(3, new java.sql.Date(pProduct.getCreationDate().getTime()));
				cs.setObject(4, pProduct.getState());
				cs.setObject(5, pProduct.getLoginUsuario());
				cs.setObject(6, pProduct.getRanking());
				cs.setObject(7,  pProduct.getImageName());
				cs.setObject(8,  pProduct.getProductType());
				cs.setObject(9,  new java.sql.Time(pProduct.getOpen().getTime()));
				cs.setObject(10, new java.sql.Time(pProduct.getClosed().getTime()));
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
}
