package com.ccic.payroll.dbutil;

public class SqlDataObject {
	private int type;
	private Object value;

	public SqlDataObject(int type, Object value) {
		this.setType(type);
		this.setValue(value);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
}
