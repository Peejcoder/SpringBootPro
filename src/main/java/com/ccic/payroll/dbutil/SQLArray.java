package com.ccic.payroll.dbutil;

import java.util.LinkedHashSet;

public class SQLArray {
	private LinkedHashSet<String> sqlArray = null;

	public void addSQL(final String sql) {
		if (null == sqlArray) {
			sqlArray = new LinkedHashSet<String>();
		}

		sqlArray.add(sql);
	}

	public LinkedHashSet<String> getSqlArray() {
		return sqlArray;
	}
}
