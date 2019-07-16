package com.ccic.payroll.dbutil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MySQLDBHelper {
	public final static String MSG_NOT_RECEIVE_RETURN = "没有收到任何返回信息.";
	public final static String FLAG_CALL_STORED_PROCEDURE_SUCCEED = "ok";
	public final static String MSG_NO_ANY_DATA = "没有获取任何数据";
	private static Logger log = LoggerFactory.getLogger(MySQLDBHelper.class);

	@Autowired
	private BasicDataSource1 dataSource;

	@Autowired
	private BasicDataSource2 dataSource2;

	/**
	 * getConnection out - out SQL exception Retrieve a MySQL connection
	 * 
	 * @return Connection
	 */
	private Connection getConnection() throws SQLException {
		log.info("getNumActive:" + dataSource.getNumActive() + "  getNumIdle:" + dataSource.getNumIdle());
		return dataSource.getConnection();
	}

	private Connection getConnection(int dbtype) throws SQLException {
		log.info("getNumActive:" + dataSource2.getNumActive() + "  getNumIdle:" + dataSource2.getNumIdle());
		switch(dbtype){
			case 2:
				log.info("使用数据源:" + dataSource2.getUrl());
				return dataSource2.getConnection();
			default:
				log.info("使用数据源:" + dataSource.getUrl());
				return dataSource.getConnection();
		}
	}

	/**
	 * execute a single SQL statement
	 * 
	 * @param sql return: true - success, false - failed
	 */
	protected boolean execute(String sql) throws SQLException {
		boolean bRet = false;
		Connection conn = null;
		Statement statement = null;
		SQLException exception = null;
		try {
			conn = getConnection();
			if (null != conn) {
				statement = conn.createStatement();
				statement.executeUpdate(sql);
				statement.close();
				conn.close();
				bRet = true;
			}
		} catch (SQLException e) {
			exception = e;
		} finally {
			if (null != statement) {
				statement.close();
			}

			if (null != conn) {
				conn.close();
			}
		}

		if (null != exception) {
			throw exception;
		}
		return bRet;
	}

	/**
	 * call a MySQL stored procedure
	 * 
	 * @param procName
	 * @param outParamsLen - out parameters number
	 * @param params
	 * @return true - indicate successful, false - failure example: Execute a stored
	 *         procedure named SaveAssetInStock // The last one is the returned
	 *         parameter SqlDataObject outResp = new
	 *         SqlDataObject(java.sql.Types.VARCHAR, "");
	 * 
	 *         boolean bSave = callProc("SaveAssetInStock", 1, new
	 *         SqlDataObject(java.sql.Types.VARCHAR, "param1"), new
	 *         SqlDataObject(java.sql.Types.INTEGER, 1), new
	 *         SqlDataObject(java.sql.Types.DATE, Date()), outResp); if (null ==
	 *         outResp.getValue()) { outResp.setValue("操作失败"); } if (!bSave ||
	 *         !outResp.getValue().equals("ok")) { // Execute failed, get error
	 *         description print(outResp.getValue().toString()); return false; }
	 *         return true;
	 * 
	 */
	public boolean callProc(String procName, int outParamsLen, final SqlDataObject... params) throws SQLException {
		String sql = "CALL " + procName;
		String paraFlag = "";
		boolean bRet = false;
		SQLException exception = null;
		log.info(sql);

		for (int i = 0; i < params.length; i++) {
			if (i == params.length - 1)
				paraFlag += "?";
			else
				paraFlag += "?,";
		}

		if (paraFlag.length() > 0) {
			sql += "(" + paraFlag + ")";
		}

		log.info("getConnection......");
		Connection conn = null;
		CallableStatement stmt = null;
		try {
			conn = getConnection();
			if (null != conn) {
				log.info("prepareCall......");
				stmt = conn.prepareCall(sql);

				for (int i = 1; i < params.length; i++) {
					SqlDataObject para = params[i - 1];

					stmt.setObject(i, para.getValue(), para.getType());
				}

				for (int i = (params.length - outParamsLen + 1); i <= params.length; i++) {
					SqlDataObject out = params[i - 1];
					stmt.registerOutParameter(i, out.getType());
				}

				stmt.execute();

				// Get result
				for (int i = (params.length - outParamsLen + 1); i <= params.length; i++) {
					SqlDataObject out = params[i - 1];
					out.setValue(stmt.getObject(i));
				}
				bRet = true;
			}
		} catch (SQLException e) {
			exception = e;
		} finally {

			if (null != stmt) {
				stmt.close();
				stmt = null;
			}

			if (null != conn) {
				conn.close();
			}
		}

		if (null != exception) {
			throw exception;
		}

		return bRet;
	}

	/**
	 * Batch execute SQL
	 * 
	 * @param sqls - SQLArray
	 * @return true - success, false - failed
	 */
	public boolean execute_batch(final SQLArray sqls) throws SQLException {
		boolean bRet = false;

		Connection conn = null;
		Statement statement = null;
		SQLException exception = null;
		try {
			conn = getConnection();
			if (null != conn) {
				conn.setAutoCommit(false);
				statement = conn.createStatement();

				for (String sql : sqls.getSqlArray()) {
					statement.executeUpdate(sql);
				}
				bRet = true;
			}
		} catch (SQLException e) {
			exception = e;
		} finally {
			if (null != statement) {
				statement.close();
			}

			if (null != conn) {
				conn.commit();
				conn.close();
			}
		}

		if (null != exception) {
			throw exception;
		}

		return bRet;
	}

	/**
	 * Batch execute SQL
	 *
	 * @param sqls - SQLArray
	 * @return true - success, false - failed
	 */
	public boolean execute_batch(final SQLArray sqls,int datasource) throws SQLException {
		boolean bRet = false;

		Connection conn = null;
		Statement statement = null;
		SQLException exception = null;
		try {
			conn = getConnection(datasource);
			if (null != conn) {
				conn.setAutoCommit(false);
				statement = conn.createStatement();

				for (String sql : sqls.getSqlArray()) {
					statement.executeUpdate(sql);
				}
				bRet = true;
			}
		} catch (SQLException e) {
			exception = e;
		} finally {
			if (null != statement) {
				statement.close();
			}

			if (null != conn) {
				conn.commit();
				conn.close();
			}
		}

		if (null != exception) {
			throw exception;
		}

		return bRet;
	}
	/**
	 * get ResultSet with a SQL statement
	 * 
	 * @param sql
	 * @return ResultSet
	 */
	public DataRecordSet getRs(String sql) throws SQLException {
		DataRecordSet rs = null;
		SQLException exception = null;
		try {
			Connection conn = getConnection();
			if (null != conn) {
				rs = new DataRecordSet();
				rs.getRecordSet(conn, sql);
			}
		} catch (SQLException e) {
			exception = e;
			if (null != rs) {
				rs.close();
				rs = null;
			}
		}

		if (null != exception) {
			throw exception;
		}

		return rs;
	}
	/**
	 * get ResultSet with a SQL statement
	 *
	 * @param sql
	 * @datasource datasource
	 * @return ResultSet
	 */
	public DataRecordSet getRs(String sql,int datasource) throws SQLException {
		DataRecordSet rs = null;
		SQLException exception = null;
		try {
			Connection conn = getConnection(datasource);
			if (null != conn) {
				rs = new DataRecordSet();
				rs.getRecordSet(conn, sql);
			}
		} catch (SQLException e) {
			exception = e;
			if (null != rs) {
				rs.close();
				rs = null;
			}
		}

		if (null != exception) {
			throw exception;
		}

		return rs;
	}
}
