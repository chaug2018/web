package com.yzj.wf.scan.service;

import java.util.List;

import com.yzj.wf.scan.util.ImageNode;

/**
 *创建于:2012-9-18<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 影像传输接口
 * @author 陈林江
 * @version 1.0
 */
public interface IUploadService {

	void upload(List<ImageNode> nodes)throws Exception;
}
