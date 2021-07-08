/**
 * 
 */
package com.yzj.ebs.edata.job;

import java.io.Serializable;

/**
 * @author chender
 *
 */
public class AutoResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3372901330302728171L;
	
	private String startTime;
	private String time;
	private String desc;
	public AutoResult(String startTime,String time,String desc){
		this.startTime=startTime;
		this.time=time;
		this.desc=desc;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	

}
