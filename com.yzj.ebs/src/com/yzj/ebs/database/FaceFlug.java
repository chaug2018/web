package com.yzj.ebs.database;

public class FaceFlug implements java.io.Serializable{
	private String idCenter;
	private String sendMode;
	private String addressFlug;
	
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getAddressFlug() {
		return addressFlug;
	}
	public void setAddressFlug(String addressFlug) {
		this.addressFlug = addressFlug;
	}
//	public boolean equals(Object obj){
//		
//		if(!(obj instanceof FaceFlug)){
//			return false;
//		}
//		FaceFlug faceFlug = (FaceFlug)obj;
//		return new EqualsBuilder().appendSuper(super.equals(obj)).
//					append(this.idCenter,faceFlug.idCenter).
//					append(this.sendMode, faceFlug.sendMode).isEquals();
//	}
//	public int hashCode(){
//		return new HashCodeBuilder(-528253723, -475504089).appendSuper(super.hashCode()).
//				append(this.idCenter).append(this.sendMode).toHashCode();
//	}

}
