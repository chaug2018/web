package com.yzj.ebs.database.temp.hbm;



public class EbsDataState implements java.io.Serializable {

	private static final long serialVersionUID = -7760236417085151403L;

	private String datadate;
	private String datastate;
	private String tablename;
	private long linenum;
	private long autoid;
	
	
	
	public long getAutoid() {
		return autoid;
	}
	public void setAutoid(long autoid) {
		this.autoid = autoid;
	}
	public String getDatadate() {
		return datadate;
	}
	public void setDatadate(String datadate) {
		this.datadate = datadate;
	}

	public String getDatastate() {
		return datastate;
	}
	public void setDatastate(String datastate) {
		this.datastate = datastate;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public long getLinenum() {
		return linenum;
	}
	public void setLinenum(long linenum) {
		this.linenum = linenum;
	}

}