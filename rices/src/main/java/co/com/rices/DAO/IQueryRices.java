package co.com.rices.DAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import co.com.rices.Conexion;
import co.com.rices.IConstants;
import co.com.rices.objects.City;
import co.com.rices.objects.Complement;
import co.com.rices.objects.CouponCode;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.StepDetail;

public interface IQueryRices {

	public static List<Product> getProductsByParams(Product pParam)  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description, creation_date, state, login_usuario, "); 
			builder.append("        ranking, image_name, product_type, open, closed, price              "); 
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
				if(StringUtils.trimToNull(pParam.getName())!=null){
					builder.append(" AND product_name LIKE ? ");
					params.put(i++, "%"+pParam.getName().trim()+"%");
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
					result.setPrice(rs.getBigDecimal("price"));
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
			builder.append(" ORDER BY step_order ");
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


	public static List<StepDetail> getDetailsByProductStepId(Integer pProductStepId, boolean pActivos) throws Exception{
		List<StepDetail> results=new ArrayList<StepDetail>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_step_id, selected_product_id, state, price ");
			builder.append(" FROM   rices.step_details                                     ");
			builder.append(" WHERE  product_step_id = ?                                    ");
			if(pActivos){
				builder.append(" AND state = ? ");
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pProductStepId);
				if(pActivos){
					cs.setString(2, "A");
				}
				rs = cs.executeQuery();
				while(rs.next()){
					StepDetail result = new StepDetail();
					result.setId(rs.getInt("id"));
					result.setProductStepId(rs.getInt("product_step_id"));
					result.setSelectedProductId(rs.getInt("selected_product_id"));
					result.setState(rs.getString("state"));
					result.setPrice(rs.getBigDecimal("price"));
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

	public static List<Product> getProductsToSell()  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description, "); 
			builder.append("        ranking, image_name, price     "); 
			builder.append(" FROM   rices.products                 ");
			builder.append(" WHERE  2018         = 2018            ");
			builder.append(" AND    open        <= ?               ");
			builder.append(" AND    closed      >= ?               ");
			builder.append(" AND    state        = ?               ");
			builder.append(" AND    product_type = ?               ");

			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, new java.sql.Time(Calendar.getInstance().getTime().getTime()));
				cs.setObject(2, new java.sql.Time(Calendar.getInstance().getTime().getTime()));
				cs.setObject(3, "A");
				cs.setObject(4, "P");
				rs = cs.executeQuery();
				while(rs.next()){
					Product result = new Product();
					result.setId(rs.getInt("id"));
					result.setName(rs.getString("product_name"));
					result.setDescription(rs.getString("description"));
					result.setRanking(rs.getInt("ranking"));
					result.setImageName(rs.getString("image_name"));
					result.setPrice(rs.getBigDecimal("price"));
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

	public static String getProductNameById(Integer pId)  throws Exception{
		String result = "";
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT product_name   "); 
			builder.append(" FROM   rices.products ");
			builder.append(" WHERE  id  = ?        ");

			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setInt(1, pId);
				rs = cs.executeQuery();
				if(rs.next()){
					result = rs.getString("product_name");
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
		return result;
	}

	public static List<City> getCities()throws Exception{
		List<City> results = new ArrayList<City>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT codigo, nombre, codigo_superior ");
			builder.append(" FROM rices.cities                      ");
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				rs = cs.executeQuery();
				while(rs.next()){
					City city = new City();
					city.setId(rs.getInt("codigo"));
					city.setName(rs.getString("nombre"));
					city.setIdSuperior(rs.getInt("codigo_superior"));
					results.add(city);
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

	public static CouponCode getCouponCode(CouponCode pCouponCode)throws Exception{
		CouponCode resultado = null;
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT client_id, coupon, used, percentage  ");
			builder.append(" FROM   rices.coupon_codes                    ");
			builder.append(" WHERE  345 = 345                             ");
			if(pCouponCode!=null){
				int i = 1;
				if(pCouponCode.getClientId()!=null){
					builder.append(" AND client_id = ? ");
					params.put(i++, pCouponCode.getClientId());
				}
				if(StringUtils.trimToNull(pCouponCode.getCoupon())!=null){
					builder.append(" AND coupon =  ? ");
					params.put(i++, pCouponCode.getCoupon());
				}
				if(StringUtils.trimToNull(pCouponCode.getUsed())!=null){
					builder.append(" AND  used = ? ");
					params.put(i++, pCouponCode.getUsed());
				}
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int i: params.keySet()){
					cs.setObject(i, params.get(i));
				}
				rs = cs.executeQuery();
				if(rs.next()){
					resultado = new CouponCode();
					resultado.setClientId(rs.getInt("client_id"));
					resultado.setCoupon(rs.getString("coupon"));
					resultado.setPercentage(rs.getBigDecimal("percentage"));
					resultado.setUsed(rs.getString("used"));
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
	
	public static List<Complement> getComplementsByDetail(Complement pComplement)throws Exception{
		List<Complement> results = new ArrayList<Complement>();
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		try{
			StringBuilder builder = new StringBuilder();
			builder.append(" SELECT id_detalle_pedido, id_product, id_product_step, price ");
			builder.append(" FROM   rices.complements                                     ");
			builder.append(" WHERE  345 = 345                                             ");
			if(pComplement!=null){
				int i = 1;
				if(pComplement.getDetailId()!=null){
					builder.append(" AND id_detalle_pedido = ? ");
					params.put(i++, pComplement.getDetailId());
				}
				if(pComplement.getProductStepId()!=null){
					builder.append(" AND id_product_step = ? ");
					params.put(i++, pComplement.getProductStepId());
				}
				if(pComplement.getSelectedProductId()!=null){
					builder.append(" AND id_product = ? ");
					params.put(i++, pComplement.getSelectedProductId());
				}
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				for(int i: params.keySet()){
					cs.setObject(i, params.get(i));
				}
				rs = cs.executeQuery();
				if(rs.next()){
					Complement complement = new Complement();
					complement.setDetailId(rs.getInt("id_detalle_pedido"));
					complement.setPrice(rs.getBigDecimal("price"));
					complement.setProductStepId(rs.getInt("id_product_step"));
					complement.setSelectedProductId(rs.getInt("id_product"));
					results.add(complement);
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
