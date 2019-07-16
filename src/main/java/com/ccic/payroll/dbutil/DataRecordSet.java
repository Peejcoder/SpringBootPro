package com.ccic.payroll.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRecordSet {
	private PreparedStatement stmt;
	private ResultSet resultSet;
	protected Connection conn;

	public PreparedStatement getStmt() {
		return stmt;
	}

	public void setStmt(PreparedStatement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public void getRecordSet(final Connection conn, final String sql) throws SQLException {
		if (null != conn) {
			this.conn = conn;
			stmt = conn.prepareStatement(sql);
			stmt.executeQuery();
			resultSet = stmt.getResultSet();
		}
	}

	public void close() {
		try {
			if (null != stmt) {
				stmt.close();
				stmt = null;
			}

			if (null != resultSet) {
				resultSet.close();
				resultSet = null;
			}

			if (null != conn) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
