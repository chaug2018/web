/**
 * WFPageLinksTag.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:jiangzhengqiu 2012-4-19
 */
package com.yzj.wf.web.common.pagelinks;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/***
 * 服务器分页控件
 * @author jiangzhengqiu
 *
 */
public class WFPageLinksTag extends ComponentTagSupport {  
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -7665748434137493644L;
	private String pageNo;  
    private String total;  
    private String styleClass;  
    private String theme;
    private String url;
    private String urlType;
      
    public void setUrlType(String urlType) {
       this.urlType = urlType;
    }
    public void setUrl(String url) {
       this.url = url;
    }
    public void setTheme(String theme) {  
        this.theme = theme;  
    }      
    public void setStyleClass(String styleClass) {  
        this.styleClass = styleClass;  
    }  
    public void setPageNo(String pageNo) {  
        this.pageNo = pageNo;  
    }  
    public void setTotal(String total) {  
        this.total = total;  
    }  
 
  
    @Override 
    public Component getBean(ValueStack arg0, HttpServletRequest arg1, HttpServletResponse arg2) {  
        return new WFPageLinksTagEx(arg0, arg1);  
    }  
 
    protected void populateParams() {  
        super.populateParams();  
          
        WFPageLinksTagEx pageLinksTagEx = (WFPageLinksTagEx)component;  
        pageLinksTagEx.setPageNo(pageNo);    
        pageLinksTagEx.setTotal(total);  
        pageLinksTagEx.setStyleClass(styleClass);  
        pageLinksTagEx.setTheme(theme);  
        pageLinksTagEx.setUrl(url);
        pageLinksTagEx.setUrlType(urlType);
 
    }  
}   