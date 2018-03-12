package co.com.rices;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Conexion implements Serializable{

	private static final long serialVersionUID = -8644297010476367394L;
	
	private Connection				con								= null;
	private Statement				stmt							= null;
	private PreparedStatement	    pstmt							= null;
	private ResultSet				rs								= null;

	/**
	 * Instancia uan nueva conexiÃ³n para realizar CRUD'S
	 */
	public Conexion() {
		try {
			//			InitialContext ctx = new InitialContext();
			//			DataSource ds = (DataSource) ctx.lookup("java:/ricesDS");
			//			this.con = ds.getConnection();
			//			this.con.setAutoCommit(true);
			// **************openshuftV3********************
			String DB_driver = "org.postgresql.Driver";

			String url = "jdbc:postgresql://postgresql:5432/rices";
			String username = "admin";
			String password = "admin";
			Class.forName(DB_driver);
			con = DriverManager.getConnection(url, username, password);
			con.setAutoCommit(true);
		} catch (Exception e) {
			IConstants.log.error(e.toString(), e);
		}

	}

	/******************************************************************************
	 ******************** METODOS MÃ�S USADOS PARA CRUD'S****************************
	 ******************************************************************************/

	/**
	 * Realiza una nueva consulta a la base de datos
	 * 
	 * @param aSentencia
	 * @param aParametros
	 * @return rs
	 */
	public ResultSet consultarBD(String aSentencia, List<Object> aParametros) {

		String[] condiciones = null;
		int i = 0;
		if (aParametros != null && aParametros.size() > 0) {
			condiciones = new String[aParametros.size()];
			for (Object o : aParametros) {
				condiciones[i] = o.toString();
				i++;
			}
			rs = this.armarConsultaBD(aSentencia, condiciones);
		} else {
			rs = this.armarConsultaBD(aSentencia);
		}

		return rs;
	}

	/**
	 * Inserta un registro en la base de datos enviando parÃ¡metros
	 * 
	 * @param aTabla
	 * @param aInformacion
	 * @return ok
	 */
	public boolean insertarBD(String aTabla, Map<String, Object> aInformacion) throws Exception {
		boolean ok = false;
		String[] campos = null;
		Object[] valores = null;
		int i = 0;
		if (aInformacion != null && aInformacion.size() > 0) {
			campos = new String[aInformacion.size()];
			valores = new Object[aInformacion.size()];
			for (String l : aInformacion.keySet()) {
				campos[i] = l;
				valores[i] = aInformacion.get(l);
				i++;
			}
			ok = armarInsert(aTabla, campos, valores);
			if (!ok) {
				throw new Exception();
			}
		}
		return ok;

	}

	/**
	 * Actualiza en base de datos un registro de acuerdo a sus condiciones
	 * 
	 * @param aTabla: el nombre de la tabla en base de datos
	 * @param aCampoActualizar: Es un mapa con los campos que desea actualizar. Son los campos set del update, ejemplo: update tabla set campoa = ?, campob = ?
	 * @param aCondiciones
	 * @return ok
	 * @throws Exception
	 */
	public boolean actualizarBD(String aTabla, Map<String, Object> aCampoActualizar, Map<String, Object> aCondiciones) throws Exception {
		boolean ok = false;
		try {
			String[] camposCondiciones = null;
			Object[] valoresCondiciones = null;

			String[] camposActualizar = null;
			Object[] valoresActualizar = null;

			int i = 0;
			if (aCondiciones != null && aCondiciones.size() > 0) {
				camposCondiciones = new String[aCondiciones.size()];
				valoresCondiciones = new Object[aCondiciones.size()];
				for (String l : aCondiciones.keySet()) {
					camposCondiciones[i] = l;
					valoresCondiciones[i] = aCondiciones.get(l);
					i++;
				}
			}

			if (!(aCampoActualizar != null && aCampoActualizar.size() > 0)) {

				throw new Exception();

			} else {
				i = 0;
				if (aCampoActualizar != null && aCampoActualizar.size() > 0) {
					camposActualizar = new String[aCampoActualizar.size()];
					valoresActualizar = new Object[aCampoActualizar.size()];
					for (String l : aCampoActualizar.keySet()) {
						camposActualizar[i] = l;
						valoresActualizar[i] = aCampoActualizar.get(l);
						i++;
					}
				}

			}

			ok = armarUpdate(aTabla, camposActualizar, valoresActualizar, camposCondiciones, valoresCondiciones);
			if (!ok) {
				throw new Exception();
			}

		} catch (Exception e) {
			throw new Exception(e);
		}
		return ok;

	}

	/**
	 * Elimina de base de datos un registro de acuerdo a las condiciones pasadas,
	 * en caso de no exito lo arroja al catch de la transacciÃ³n
	 * 
	 * @param aTabla
	 * @param aInformacion
	 * @return ok
	 */
	public boolean eliminarBD(String aTabla, Map<String, Object> aCondiciones) throws Exception {
		boolean ok = false;
		try {
			String[] campos = null;
			Object[] valores = null;
			int i = 0;
			if (aCondiciones != null && aCondiciones.size() > 0) {
				campos = new String[aCondiciones.size()];
				valores = new Object[aCondiciones.size()];
				for (String l : aCondiciones.keySet()) {
					campos[i] = l;
					valores[i] = aCondiciones.get(l);
					i++;
				}
				ok = armarDelete(aTabla, campos, valores);
				if (!ok) {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return ok;

	}

	/**
	 * Ejecuta una sentencia sql en base de datos
	 * 
	 * @param sentencia
	 * @return true-false
	 */
	public boolean ejecutarQueryBD(String sentencia) {
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sentencia);
		} catch (SQLException e) {
			IConstants.log.error(e, e);
			return false;
		} catch (RuntimeException e) {
			IConstants.log.error(e, e);
			return false;
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;
	}

	/**
	 * Bloquear transacciÃ³n
	 */
	public void lockTransaccion() {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement("SET LOCK MODE TO WAIT");
			ps.execute();
		} catch (SQLException e) {
			IConstants.log.error(e, e);
		} finally {

			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {

			}
		}

	}

	/**
	 * Obtiene Ãºltimo serial del registro insertado
	 * 
	 * @param aTabla
	 * @return
	 */
	public Integer getUltimoSerialTransaccion(String aTabla) {
		Integer valor = null;

		try {
			String[] condiciones = new String[1];
			condiciones[0] = aTabla;
			rs = this.armarConsultaBD("SELECT DBINFO( 'sqlca.sqlerrd1' ) FROM systables WHERE tabname = ?", condiciones);
			if (rs.next()) {
				valor = rs.getInt(1);
			}
		} catch (Exception e) {
			IConstants.log.error(e, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
		}
		return valor;

	}

	/**
	 * Cierra la conexiÃ³n instanciada, no arroja al catch de la transacciÃ³n pues
	 * la conexiÃ³n siempre la cerramos en el finally
	 */
	public void cerrarConexion() {
		closeConnection(con);
	}

	/**
	 * Determina si es atÃ³mica o no una transaciÃ³n
	 * 
	 * @param parametro
	 * @return true-false
	 */
	public boolean setAutoCommitBD(boolean parametro) {
		try {
			con.setAutoCommit(parametro);
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;
	}

	/**
	 * Guarda cambios en BD
	 * 
	 * @return true-false
	 */
	public boolean commitBD() {
		try {
			con.commit();
			return true;
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
	}

	/**
	 * Aborta transacciÃ³n
	 * 
	 * @return con
	 */
	public boolean rollbackBD() {
		try {
			con.rollback();
			return true;
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
	}

	/**
	 * Ejecuta una instrucciÃ³n a base de datos. Puede ser un procedimiento
	 * almacenado por ejemplo
	 * 
	 * @param sentencia
	 * @return true-false
	 */
	public boolean executeBD(String sentencia) {
		try {
			stmt = con.createStatement();
			stmt.execute(sentencia);

		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;
	}

	/******************************************************************************
	 ******************************************************************************
	 ******************************************************************************/

	// gets de uso propio

	/**
	 * Obtiene la conexiÃ³n activa
	 * 
	 * @return con
	 */
	public Connection getConnection() {
		return con;
	}

	/**
	 * Cierra la conexiÃ³n activa
	 * 
	 * @param aConexion
	 */
	private void closeConnection(Connection aConexion) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Consulta la base de datos con parÃ¡metros
	 * 
	 * @param aSentencia
	 * @param aCondiciones
	 * @return rs
	 */
	private ResultSet armarConsultaBD(String aSentencia, String[] aCondiciones) {

		int numParam = aCondiciones.length;
		int posic = -1;
		int n = -1;
		pstmt = null;

		try {
			pstmt = con.prepareStatement(aSentencia);

			for (int i = 0; i <= numParam - 1; i++) {
				posic = aSentencia.indexOf("?", posic + 1);
				n = aSentencia.indexOf("LIKE", posic - 6);// Revisa si el
				if ((n < posic) && (n != -1)) {// ac es un 'like'
					pstmt.setString(i + 1, "%" + aCondiciones[i] + "%");
				} else {
					pstmt.setString(i + 1, aCondiciones[i]);
				}
			}
			rs = pstmt.executeQuery();
		} catch (Exception e) {
			IConstants.log.error(e, e);
		}
		return rs;
	}
	
	

	/**
	 * Consulta la base de datos, sin parÃ¡metros
	 * 
	 * @param aSentencia
	 * @return rs
	 */
	private ResultSet armarConsultaBD(String aSentencia) {
		try {
			stmt = con.createStatement(); // ResultSet.TYPE_SCROLL_SENSITIVE,
			// ResultSet.CONCUR_READ_ONLY
			rs = stmt.executeQuery(aSentencia);

		} catch (Exception e) {
			IConstants.log.error(e, e);
		}

		return rs;
	}

	/**
	 * Arma un delete con parÃ¡metros
	 * 
	 * @param aTabla
	 * @param aCampos
	 * @param aValores
	 * @return true-false
	 */
	private boolean armarDelete(String aTabla, String[] aCampos, Object[] aValores) {

		String sentencia = new String();
		StringBuffer buffer = new StringBuffer();
		int numParam = aValores.length;
		pstmt = null;
		buffer.append("DELETE FROM " + aTabla);
		numParam = aCampos.length;
		for (int j = 0; j <= numParam - 1; j++) {
			if (j == 0) {
				buffer.append(" WHERE " + aCampos[j] + " =  ?");

			} else {
				buffer.append(" AND " + aCampos[j] + " =  ?");
			}
		}
		sentencia = buffer.toString();
		numParam = aValores.length;
		try {
			pstmt = con.prepareStatement(sentencia);
			for (int j = 0; j <= numParam - 1; j++) {
				if (aValores[j] == null) {
					pstmt.setNull(j + 1, 0);
				} else if (aValores[j].getClass().getName().equals("java.lang.String")) {
					String StringParam = (aValores[j].toString());
					pstmt.setString(j + 1, StringParam);
				} else if (aValores[j].getClass().getName().equals("java.lang.Integer")) {
					Integer IntegerParam = new Integer(aValores[j].toString());
					pstmt.setInt(j + 1, IntegerParam.intValue());
				} else if (aValores[j].getClass().getName().equals("java.lang.Long")) {
					pstmt.setLong(j + 1, ((Long) aValores[j]).longValue());
				} else if (aValores[j].getClass().getName().equals("java.util.Date")) {// Es
					pstmt.setDate(j + 1, new java.sql.Date((((Date) aValores[j]).getTime())));
				} else if (aValores[j].getClass().getName().equals("java.math.BigDecimal")) {// Es
					pstmt.setBigDecimal(j + 1, (BigDecimal) aValores[j]);
				} else {
					pstmt.setObject(j + 1, aValores[j]);
				}
			}
			if (pstmt.executeUpdate() == 0) {
				return false;
			}
		} catch (SQLException ee) {
			IConstants.log.error(ee, ee);
			return false;
		}

		catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;
	}

	/**
	 * Arma un nuevo insert de forma dinÃ¡mica
	 * 
	 * @param aTabla
	 * @param aCampos
	 * @param aValores
	 * @return true-false
	 */
	private boolean armarInsert(String aTabla, String[] aCampos, Object[] aValores) {

		String sentencia = new String();
		StringBuffer buffer = new StringBuffer();

		int numParam = aValores.length;
		pstmt = null;

		buffer.append("INSERT INTO " + aTabla + " (");
		for (int j = 0; j <= numParam - 2; j++) {
			buffer.append(aCampos[j] + ",");
		}
		buffer.append(aCampos[numParam - 1] + ") values (");
		for (int i = 0; i <= numParam - 2; i++) {
			buffer.append("?,");
		}
		buffer.append("?);");
		sentencia = buffer.toString();

		try {
			pstmt = con.prepareStatement(sentencia);
			for (int j = 0; j <= numParam - 1; j++) {

				if (aValores[j] == null) {
					pstmt.setNull(j + 1, 0);
				} else if (aValores[j].getClass().getName().equals("java.lang.String")) {// Es
					pstmt.setString(j + 1, (String) aValores[j]);
				} else if (aValores[j].getClass().getName().equals("java.lang.Integer")) {// Es
					pstmt.setInt(j + 1, ((Integer) aValores[j]).intValue());
				} else if (aValores[j].getClass().getName().equals("java.lang.Long")) {// Es
					pstmt.setLong(j + 1, ((Long) aValores[j]).longValue());
				} else if (aValores[j].getClass().getName().equals("java.util.Date")) {// Es
					pstmt.setDate(j + 1, new java.sql.Date((((Date) aValores[j]).getTime())));
				} else if (aValores[j].getClass().getName().equals("java.math.BigDecimal")) {// Es
					pstmt.setBigDecimal(j + 1, (BigDecimal) aValores[j]);
				} else if (aValores[j].getClass().getName().equals("java.math.BigDecimal")) {// Es
					pstmt.setBigDecimal(j + 1, (BigDecimal) aValores[j]);
				} else {
					pstmt.setObject(j + 1, aValores[j]);
				}
			}
			pstmt.executeUpdate();
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;

	}

	/**
	 * Arma un update de forma dinÃ¡mica
	 * 
	 * @param aTabla
	 * @param aCamposActualizar
	 * @param aValoresActualizar
	 * @param aCamposCondiciones
	 * @param aValoresCondiciones
	 * @return tre-false
	 */
	private boolean armarUpdate(String aTabla, String[] aCamposActualizar, Object[] aValoresActualizar, String[] aCamposCondiciones, Object[] aValoresCondiciones) {
		try {
			String sentencia = new String();
			StringBuffer buffer = new StringBuffer();

			int numParametrosCondiciones = 0;
			int totalCampos = 0;
			int k = 0;

			int numParam = aValoresActualizar.length;
			pstmt = null;
			buffer.append("UPDATE " + aTabla + " SET ");
			for (int i = 0; i <= numParam - 1; i++) {
				if (i == 0) {
					buffer.append(aCamposActualizar[i] + "=?");
				} else {
					buffer.append("," + aCamposActualizar[i] + "=?");
				}
			}

			if (aCamposCondiciones != null && aCamposCondiciones.length > 0) {
				numParametrosCondiciones = aCamposCondiciones.length;
				for (int j = 0; j <= numParametrosCondiciones - 1; j++) {
					if (j == 0) {
						buffer.append(" WHERE " + aCamposCondiciones[j] + " = ?");

					} else {
						buffer.append(" AND " + aCamposCondiciones[j] + " = ?");
					}
				}
			}

			sentencia = buffer.toString();

			pstmt = con.prepareStatement(sentencia);
			totalCampos = (numParam - 1);
			if (numParametrosCondiciones > 0) {
				totalCampos += numParametrosCondiciones;
			}

			for (int j = 0; j <= totalCampos; j++) {

				if (j <= (numParam - 1)) {

					if (aValoresActualizar[j] == null) {
						pstmt.setNull(j + 1, 0);
					} else if (aValoresActualizar[j].getClass().getName().equals("java.lang.String")) {

						String StringParam = (aValoresActualizar[j].toString());
						pstmt.setString(j + 1, StringParam);
					} else if (aValoresActualizar[j].getClass().getName().equals("java.lang.Integer")) {
						Integer IntegerParam = new Integer(aValoresActualizar[j].toString());
						pstmt.setInt(j + 1, IntegerParam.intValue());
					} else if (aValoresActualizar[j].getClass().getName().equals("java.lang.Long")) {
						pstmt.setLong(j + 1, ((Long) aValoresActualizar[j]).longValue());
					} else if (aValoresActualizar[j].getClass().getName().equals("java.util.Date")) {// Es
						pstmt.setDate(j + 1, new java.sql.Date((((Date) aValoresActualizar[j]).getTime())));
					} else if (aValoresActualizar[j].getClass().getName().equals("java.math.BigDecimal")) {// Es
						pstmt.setBigDecimal(j + 1, (BigDecimal) aValoresActualizar[j]);
					} else {
						pstmt.setObject(j + 1, aValoresActualizar[j]);
					}

				} else {

					k = j - numParam;
					if (aValoresCondiciones[k] == null) {
						pstmt.setNull(j + 1, 0);
					} else if (aValoresCondiciones[k].getClass().getName().equals("java.lang.String")) {

						String StringParam = (aValoresCondiciones[k].toString());
						pstmt.setString(j + 1, StringParam);
					} else if (aValoresCondiciones[k].getClass().getName().equals("java.lang.Integer")) {
						Integer IntegerParam = new Integer(aValoresCondiciones[k].toString());
						pstmt.setInt(j + 1, IntegerParam.intValue());
					} else if (aValoresCondiciones[k].getClass().getName().equals("java.lang.Long")) {
						pstmt.setLong(j + 1, ((Long) aValoresCondiciones[k]).longValue());
					} else if (aValoresCondiciones[k].getClass().getName().equals("java.util.Date")) {// Es
						pstmt.setDate(j + 1, new java.sql.Date((((Date) aValoresCondiciones[k]).getTime())));
					} else if (aValoresCondiciones[k].getClass().getName().equals("java.math.BigDecimal")) {// Es
						pstmt.setBigDecimal(j + 1, (BigDecimal) aValoresCondiciones[k]);
					} else {
						pstmt.setObject(j + 1, aValoresCondiciones[k]);
					}

				}

			}
			if (pstmt.executeUpdate() == 0) {
				return false;
			}
		} catch (Exception e) {
			IConstants.log.error(e, e);
			return false;
		}
		return true;
	}



}
