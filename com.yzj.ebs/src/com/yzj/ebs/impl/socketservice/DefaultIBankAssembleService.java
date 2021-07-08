/**
 * IBankAssembleService.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:rain 2011-8-12
 */
package com.yzj.ebs.impl.socketservice;

import com.yzj.wf.com.ibank.common.IBankAssembleException;
import com.yzj.wf.com.ibank.common.IBankDefine.Direction;
import com.yzj.wf.com.ibank.common.IBankDefine.TradeDebug;
import com.yzj.wf.com.ibank.common.template.Field;
import com.yzj.wf.com.ibank.common.template.Trade;
import com.yzj.wf.com.ibank.core.DataProcAssemble;

/**
 * 默认IBankAssembleService服务, 对上传下传字节流数据加工（定长字节流）<br>
 * 20100820增加：<br>
 * 增加明细节点赋值
 * 
 * @author rain
 * @version 1.9.0
 */
public class DefaultIBankAssembleService extends DataProcAssemble {

	@Override
	protected byte[] getPackageData(Trade trade) throws IBankAssembleException {
		StringBuffer sbDataUp = new StringBuffer();

		for (Field field : trade.getFieldList().getFields()) {
			if (Direction.UP.getValue() == field.getDirect()
					|| Direction.UP_DOWN.getValue() == field.getDirect()) {

				String value = field.getValue();
				sbDataUp.append(value);
			}
		}

			return sbDataUp.toString().getBytes();
	}

	@Override
	protected void setPackageData(Trade trade, byte[] byteDataDown)
			throws IBankAssembleException {
		int curPosition = 0;
		for (Field field : trade.getFieldList().getFields()) {
			if (Direction.DOWN.getValue() == field.getDirect()
					|| Direction.UP_DOWN.getValue() == field.getDirect()) {

				if (byteDataDown == null
						&& (trade.getDebug() == TradeDebug.DEBUG_TEST
								.getValue()))
					break;
				
				//byte[] temp = new byte[field.getLen()];
//				System.arraycopy(byteDataDown, curPosition, temp, 0,
//						field.getLen());
				// 如果Field不为空, 则不予赋值
				System.out.println(new String(byteDataDown));
				if (null == field.getValue() || field.getValue().equals("")) {
					field.setValue(convertToUTF8(byteDataDown));
				}

				logger.debug("name_" + field.getName() + ": "
						+ field.getValue());
				curPosition = curPosition + field.getLen();

			}// end of if(direct)
		}// end of for
	}// end of setPackage
}
