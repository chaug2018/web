// 回收/发送登记
	function bookIn(optionType)
	{
		// 回收    0:回收  1：发送
		if("0" == optionType)
		{
			$("#send1").hide();
			$("#call1").show();
			$("#operType").val("0");
			
			//document.getElementById("send1").style.display = "none";
			//document.getElementById("call1").style.display = "block";
			//document.getElementById("operType").value = "0";
		}else if("1" == optionType)
		{
			$("#call1").hide();
			$("#send1").show();
			$("#operType").val("1");
			
			//document.getElementById("send1").style.display = "block";
			//document.getElementById("call1").style.display = "none";
			//document.getElementById("operType").value = "1";
		}
		showSon();
	}
// 登记提交
	function call()
	{
		var operType =document.getElementById("operType").value; // 登记类型
		var docDate =document.getElementById("queryParam.docDate").value;// 对账日期
		var voucherNo =document.getElementById("queryParam.voucherNo").value; // 对账单编号
		var backDate =document.getElementById("queryParam.backDate").value; // 签收日期
		var letterSum =document.getElementById("queryParam.letterSum").value; //  份数
		var sendmode =document.getElementById("queryParam.sendmode").value; // 队长渠道
		var desc =document.getElementById("queryParam.desc").value; // 备注
		var accName =document.getElementById("queryParam.accName").value; // 单位名称
		var sendDate =document.getElementById("queryParam.sendDate").value; // 发送日期
		var idCenter = document.getElementById("idCenter").value;
		var idBranch = document.getElementById("idBranch").value;
		var idBank = document.getElementById("idBank").value;
		var param = "queryParam.opType="+operType+"&queryParam.docDate="+docDate+"&queryParam.voucherNo="+voucherNo+"&queryParam.backDate="+backDate+"&queryParam.letterSum="+letterSum+"&queryParam.sendmode="+sendmode
		 +"&queryParam.desc="+desc+"&queryParam.accName="+accName+"&queryParam.sendDate="+sendDate+"&queryParam.idBranch="+idBranch+"&queryParam.idBank="+idBank+"&queryParam.idCenter="+idCenter;
		// 执行添加
		$.post("exChangeBookAction_bookIn.action", 
				param, 
				function(result){
					if(result == "success")
					{
						alert("添加成功！");
						// 设置查询条件
						if(document.getElementById("docDate").value=="")
						{
							document.getElementById("docDate").value = docDate;
						}
						// 关闭当前页面/刷新主页面
						closeSon();
						toExChangeBookResult();
					}
					else{
						alert(result);
					}
			});	
	}
