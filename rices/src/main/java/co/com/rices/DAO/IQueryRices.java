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
import co.com.rices.objects.Parametro;
import co.com.rices.objects.Product;
import co.com.rices.objects.ProductStep;
import co.com.rices.objects.RiceMenu;
import co.com.rices.objects.StepDetail;

public interface IQueryRices {

	public static List<Product> getProductsByParams(Product pParam)  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description, creation_date, state, login_usuario, texto, content_type_big, "); 
			builder.append("        ranking, image_name, product_type, open, closed, price, menu, content_type, agrupa_menu      "); 
			builder.append(" FROM   rices.products                                                                               ");
			builder.append(" WHERE  2018 = 2018                                                                                  ");
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
					result.setIdMenu(rs.getInt("menu"));
					result.setContentType(rs.getString("content_type"));
					result.setTexto(rs.getString("texto"));
					result.setAgrupaMenu(rs.getString("agrupa_menu"));
					result.setContentTypeBig(rs.getString("content_type_big"));
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
			builder.append(" SELECT id, product_name, description,           "); 
			builder.append("        ranking, image_name, price, content_type "); 
			builder.append(" FROM   rices.products                           ");
			builder.append(" WHERE  2018         = 2018                      ");
			builder.append(" AND    open        <= ?                         ");
			builder.append(" AND    closed      >= ?                         ");
			builder.append(" AND    state        = ?                         ");
			builder.append(" AND    product_type = ?                         ");

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
					result.setContentType(rs.getString("content_type"));
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

	public static List<RiceMenu> getRiceMenus(RiceMenu pRiceMenu)throws Exception{
		List<RiceMenu> results = new ArrayList<RiceMenu>();
		Map<Integer, Object> params = new HashMap<Integer,Object>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, description, orden, estado, open, closed ");
			builder.append(" FROM   rices.rices_menu                             ");
			builder.append(" WHERE  2045 = 2045                                  ");
			if(pRiceMenu!=null){
				int i = 1;
				if(StringUtils.trimToNull(pRiceMenu.getEstado())!=null){
					builder.append(" AND  estado = ? ");
					params.put(i++, pRiceMenu.getEstado());
				}
				if(pRiceMenu.getOpen()!=null){
					builder.append(" AND open <= ? ");
					params.put(i++, new java.sql.Time(pRiceMenu.getOpen().getTime()));
				}
				if(pRiceMenu.getClosed()!=null){
					builder.append(" AND closed >= ? ");
					params.put(i++, new java.sql.Time(pRiceMenu.getClosed().getTime()));
				}
			}
			builder.append(" ORDER BY orden ");
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
				while(rs.next()){
					RiceMenu riceMenu = new RiceMenu();
					riceMenu.setId(rs.getInt("id"));
					riceMenu.setDescription(rs.getString("description"));
					riceMenu.setOrden(rs.getInt("orden"));
					riceMenu.setEstado(rs.getString("estado"));
					riceMenu.setOpen(rs.getTime("open"));
					riceMenu.setClosed(rs.getTime("closed"));
					results.add(riceMenu);
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
	
	public static List<Product> getProductsByMenu(Integer pMenu)  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description,          "); 
			builder.append("        ranking, image_name, price,content_type "); 
			builder.append(" FROM   rices.products                          ");
			builder.append(" WHERE  2018         = 2018                     ");
			builder.append(" AND    open        <= ?                        ");
			builder.append(" AND    closed      >= ?                        ");
			builder.append(" AND    state        = ?                        ");
			builder.append(" AND    product_type = ?                        ");
			builder.append(" AND    menu         = ?                        ");

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
				cs.setInt(5, pMenu);
				rs = cs.executeQuery();
				while(rs.next()){
					Product result = new Product();
					result.setId(rs.getInt("id"));
					result.setName(rs.getString("product_name"));
					result.setDescription(rs.getString("description"));
					result.setRanking(rs.getInt("ranking"));
					result.setImageName(rs.getString("image_name"));
					result.setPrice(rs.getBigDecimal("price"));
					result.setContentType(rs.getString("content_type"));
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
	
	public static List<Product> getProductsNoonMoon(String pNoonMoon)  throws Exception{
		List<Product> results=new ArrayList<Product>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, product_name, description, creation_date, state, login_usuario, texto, content_type_big, "); 
			builder.append("        ranking, image_name, product_type, open, closed, price, menu, content_type, agrupa_menu      "); 
			builder.append(" FROM   rices.products                                                                               ");
			builder.append(" WHERE  state = ?                                                                                    ");
			builder.append(" AND    product_type = ?                                                                             ");
			if(pNoonMoon!=null){
				builder.append(" AND agrupa_menu = ? ");
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				cs.setObject(1, "A");
				cs.setObject(2, "P");
				if(pNoonMoon!=null){
					cs.setObject(3, pNoonMoon);
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
					result.setIdMenu(rs.getInt("menu"));
					result.setContentType(rs.getString("content_type"));
					result.setTexto(rs.getString("texto"));
					result.setAgrupaMenu(rs.getString("agrupa_menu"));
					result.setContentTypeBig(rs.getString("content_type_big"));
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
	
	public static List<Parametro> getParametrosById(String pId)throws Exception{
		List<Parametro> results = new ArrayList<Parametro>();
		try{
			StringBuilder builder=new StringBuilder();
			builder.append(" SELECT id, valor_num, texto_corto, texto_largo ");
			builder.append(" FROM   rices.parametros                        ");
			builder.append(" WHERE  2018=2018                               ");
			if(StringUtils.trimToNull(pId)!=null){
				builder.append(" AND id LIKE ? ");
			}
			Conexion conexion    = null;
			CallableStatement cs = null;
			ResultSet rs         = null;
			try{
				conexion = new Conexion();
				cs = conexion.getConnection().prepareCall(builder.toString());
				if(StringUtils.trimToNull(pId)!=null){
					cs.setObject(1, "%"+pId.trim().toUpperCase()+"%");
				}
				rs = cs.executeQuery();
				while(rs.next()){
					Parametro parametro = new Parametro();
					parametro.setId(rs.getString("id"));
					parametro.setValorNumerico(rs.getBigDecimal("valor_num"));
					parametro.setTextCorto(rs.getString("texto_corto"));
					parametro.setTextLargo(rs.getString("texto_largo"));
					results.add(parametro);
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
