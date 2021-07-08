/**
 * 
 */
package com.yzj.ebs.storeserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.store.common.DocObject;
import com.yzj.wf.store.common.IDocObject;
import com.yzj.wf.store.common.IStoreObject;
import com.yzj.wf.store.common.IStoreOperator;
import com.yzj.wf.store.common.StoreException;
import com.yzj.wf.store.common.StoreObject;

/**
 * 创建于:2012-9-19<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 影像接收action
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class StoreAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3852318144314329974L;

	private static WFLogger logger = WFLogger.getLogger(StoreAction.class);

	private List<File> fileList = new ArrayList<File>(); // 文件列表
	private List<String> nameList = new ArrayList<String>(); // 文件名列表

	private IStoreOperator storeOper;

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String save() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		if (fileList == null || fileList.size() == 0) {
			response.getWriter().write("fail未获取到传输过来的影像");
			return null;
		}
		if (fileList.size() != nameList.size()) { // 判断文件个数和文件名个数是否相等
			response.getWriter().write("fail文件个数不匹配,可能是数据丢失");
			return null;
		}
		IStoreObject storeObject = new StoreObject();
		for (int i = 0; i < fileList.size(); i++) { // 重命名文件并拼装成存储对象，因为structs返回的是一个temp文件
			File temp = fileList.get(i);
			File target = new File(temp.getParent(),temp.getName().replace("tmp", "jpg"));
			if (temp.renameTo(target)) {  //重命名成功后，要新建一个和源文件同名（name）的文件，不然的话在这批文件处理结束前如果来了下一批文件，
				                         //下一批文件就有可能被命名成name(因为struts发现这个name没人用),
				                         //如果这时第一个请求处理结束了，struts就会去删掉name这个文件(因为这也是第一次请求的临时文件)，导致第二个请求的文件被丢失
			 temp.createNewFile();
			}else { // 重命名出现失败案例
				logger.error("文件重命名出现错误,文件路径为:"+temp.getAbsolutePath());
				response.getWriter().write("fail文件重命名出现错误");
				return null;
			}
			IDocObject docObject = new DocObject();
			docObject.setDocFile(target.getAbsolutePath());
			storeObject.addDocObject(docObject);
		}
		try {
			IStoreObject result = storeOper.addObject(storeObject); // 上传存储
			if (result == null || result.getObjectId() == null
					|| "".equals(result.getObjectId())) {
				response.getWriter().write("fail影像存储出现错误");
				return null;
			}
			String imgs=this.getImgUrl(result);
			imgs=imgs+result.getObjectId();  //加上存储id号
			logger.info("影像上传存储完毕!");
			response.getWriter().write("success" + imgs);
		} catch (StoreException e) {
			logger.error("处理该批影像出现异常", e);
			response.getWriter().write("fail影像存储出现错误");
			return null;
		} catch (Exception e) {
			logger.error("出现未知异常", e);
			response.getWriter().write("fail影像存储出现错误");
			return null;
		}
		return null;
	}
	
	/**
	 * 获取影像图片映射在web容器中的虚拟路径
	 * @param storeObject 存储对象
	 * @return 虚拟路径 多个时按分号进行分割
	 */
	private String getImgUrl(IStoreObject storeObject){
		List<IDocObject> docList = storeObject.getDocObjectList();
		String result="";
		for (IDocObject iDocObject : docList) {    //把存储路径转换为虚拟目录下对应的路径
			String sourcePath=iDocObject.getDocFile();
			String[] subs=sourcePath.split("\\"+File.separator);   //获取路径肢解后的数组
			sourcePath=subs[subs.length-3]+"/"+subs[subs.length-2]+"/"+subs[subs.length-1];
			logger.info("截取到文件的路径为:"+sourcePath);
			result+=sourcePath+";";
			} 
		return result;
	}

	public List<File> getFile() {
		return fileList;
	}

	public void setData(List<File> fileList) {
		this.fileList = fileList;
	}

	public void setDataFileName(List<String> names) {
		this.nameList = names;
	}

	public IStoreOperator getStoreOper() {
		return storeOper;
	}

	public void setStoreOper(IStoreOperator storeOper) {
		this.storeOper = storeOper;
	}

}
