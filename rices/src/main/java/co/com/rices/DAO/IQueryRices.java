package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.businessLogic.ProductStep;
import co.com.rices.objects.Product;

public interface IQueryRices {
	
	public static List<Product> getProductsByParams(Product pParam)  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description, creation_date, state, login_usuario, "); 
			builder.append("        ranking, image_name, product_type, open, closed                     "); 
			builder.append(" FROM   rices.products                                                      ");
			builder.append(" WHERE  2018 = 2018                                                         ");
			Map<Integer, Object> params=new HashMap<Integer,Object>();
			int i=1;
			if(pParam!=null){
				if(pParam.getId()!=null){
					builder.append(" AND id = ? ");
					params.put(i++, pParam.getId());
				}
				if(StringUtils.trimToNull(pParam.getProductType())!=null){
					builder.append(" AND product_type = ? ");
					params.put(i++, pParam.getProductType());
				}
				if(StringUtils.trimToNull(pParam.getState())!=null){
					builder.append(" AND state = ? ");
					params.put(i++, pParam.getState());
				}
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int j: params.keySet()){
					cs.setObject(j, params.get(j));
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Product result = new Product();
					result.setId(rs.getInt("id"));
					result.setName(rs.getString("product_name"));
					result.setDescription(rs.getString("description"));
					result.setCreationDate(rs.getDate("creation_date"));
					result.setState(rs.getString("state"));
					result.setLoginUsuario(rs.getString("login_usuario"));
					result.setRanking(rs.getInt("ranking"));
					result.setImageName(rs.getString("image_name"));
					result.setProductType(rs.getString("product_type"));
					result.setOpen(rs.getTime("open"));
					result.setClosed(rs.getTime("closed"));
					results.add(result);
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
		return results;
	}
	
	public static List<ProductStep> getProductStepsByProductId(Integer pProductId, boolean pActivos) throws Exception{
		List<ProductStep> results=new ArrayList<ProductStep>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_id, select_type, state, description, step_order ");
			builder.append(" FROM   rices.product_steps                                         "); 
			builder.append(" WHERE  product_id = ?                                              ");
			if(pActivos){
				builder.append(" AND state = ? ");
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pProductId);
				if(pActivos){
					cs.setString(2, "A");
				}
				rs = cs.executeQuery();
				while(rs.next()){
					ProductStep result = new ProductStep();
					result.setId(rs.getInt("id"));
					result.setProductId(rs.getInt("product_id"));
					result.setSelectType(rs.getString("select_type"));
					result.setState(rs.getString("state"));
					result.setDescription(rs.getString("description"));
					result.setStepOrder(rs.getInt("step_order"));
					results.add(result);
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
		return results;
	}

}
