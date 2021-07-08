package com.yzj.ebs.impl;

import com.yzj.ebs.util.FinalConstant;

public class CreateSql {
	public static String urgeStatisticsSql(String sql) {
		String resultStr = "select idcenter,idbranch,idbank,bankname,docdate,"
				+ "count(case when docstate>1 then voucherno end) as totalvouamount,"// 发出账单数
				+ "count(case when docstate>1 and urgeState=1 then voucherno end) as totaluergeamount,"// 退信总数
				+ "count(case when docstate>1 and urgeState=1 and urgetype=7 then voucherno end) as rejectedamount,"// 单位拒收
				+ "count(case when docstate>1 and urgeState=1 and urgetype=4 then voucherno end) as addrchangedamount,"// 原址拆迁
				+ "count(case when docstate>1 and urgeState=1 and urgetype=1 then voucherno end) as addrunknownamount,"// 地址不详
				+ "count(case when docstate>1 and urgeState=1 and urgetype=5 then voucherno end) as norecieveramount,"// 投递无人
				+ "count(case when docstate>1 and urgeState=1 and urgetype=3 then voucherno end) as unitnotexistamount,"// 无此单位
				+ "count(case when docstate>1 and urgeState=1 and urgetype=2 then voucherno end) as addrnotexistamount,"// 无此地址
				+ "count(case when docstate>1 and urgeState=1 and urgetype=6 then voucherno end) as noconnectionamount,"// 无法联系
				+ "count(case when docstate>1 and urgeState=1 and urgetype=8 then voucherno end) as otheramount "// 其他
				+ "from ebs_checkmaindata  "
				+ sql
				+ " group by idcenter,idbranch,idbank,bankname,docdate order by idcenter,idbank";
		return resultStr;
	}

	public static String rushStatisticsSql(String sql) {
		String resultStr = "select idcenter,idbranch,idbank,bankname,docdate,"
				+ "count(case when docstate>1 then voucherno end) as totalvouamount,"// 发出账单数
				+ "count(case when docstate>1 and rushstate=1 then voucherno end) as totalrushamount,"// 催收总数
				+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=0 and rushflag=1 then voucherno end) as rushedbytelamount,"// 电话催收
				+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=1 and rushflag=1 then voucherno end) as rushedbyemailamount,"// 邮件催收
				+ "count(case when docstate>1 and rushstate=1 and RUSHMETHOD=2 and rushflag=1 then voucherno end) as rushedfacetofaceamount,"// 面对面催收
				+ "count(case when docstate=3 and rushstate=1 then voucherno end) as rushsuccessamount "// 催收成功数
				+ "from ebs_checkmaindata "
				+ sql
		        + " group by idcenter,idbranch,idbank,bankname,docdate order by idcenter , idbank";
		return resultStr;
	}
	
	public static String businessStatisticsSql(String sql,String docDate) {
		String resultStr = "select b.idcenter,b.idbranch,b.idbank,b.bankname, "
				+ "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("柜台")+"' then a.accno end) as counteraccount, "// 柜台对账账户数
				+ "count(case when a.sendmode = '"+FinalConstant.sendModelMap.get("邮寄")+"' then a.accno end) as postaccount, "// 邮寄对账账户数
				+ "count(b.accno) as idcenterCount, "// 对账中心账户数
				+ "count(a.accno) as idcenteraccount2 "// 应对账账户数
				+ "from ebs_basicinfo b left join (select distinct(c.accno),c.sendmode from ebs_accnomaindata c where 1=1 and c.docdate='"+docDate.trim()+ "') a on(b.accno = a.accno) "
		        + sql 
		        +" group by b.idcenter, b.idbranch, b.idbank, b.bankname order by b.idcenter,b.idbank";
		return resultStr;
	}
}
