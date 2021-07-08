
//反显操作
function rulemodifyacc(data){
	document.getElementById("modifyOrAdd").value="2";
	var cell=data.parentNode;
	var row=cell.parentNode;
	document.getElementById("tableIndex").value=row.cells[0].innerHTML;  //当前行下标
    startProcess("正在从后台获取账号信息...");	
	$.getJSON("ruleOfAccCycleAction!ruleModify.action", 
			"index="+row.cells[0].innerHTML+"&temp="+new Date(), 
			 function(rule){
						if(rule==null){
							alert("未从后台缓存中获取到对应的账号信息,请尝试刷新界面");
							stopProcess();
						 }else{
								stopProcess();			
								document.getElementById("dataIndex").value=row.cells[0].innerHTML;
								document.getElementById("acc_div").value=rule.accCycle;
								document.getElementById("sub_div").value=rule.subjectNo
								document.getElementById("minBal_div").value=rule.minBal
								document.getElementById("maxBal_div").value=rule.maxBal
								document.getElementById("oneMinAccrual_div").value=rule.oneMinAccrual
								document.getElementById("oneMaxAccrual_div").value=rule.oneMaxAccrual
								document.getElementById("totalMinAccrual_div").value=rule.totalMinAccrual
								document.getElementById("otalMaxAccrual_div").value=rule.totalMaxAccrual
								showSon();
							}
			});
}
//增加操作中的显示界面 
	function addrule(){
		document.getElementById("modifyOrAdd").value="1";
		document.getElementById("acc_div").value="";
		document.getElementById("sub_div").value="";
		document.getElementById("minBal_div").value="";
		document.getElementById("maxBal_div").value="";
		document.getElementById("oneMinAccrual_div").value="";
	    document.getElementById("oneMaxAccrual_div").value="";
		document.getElementById("totalMinAccrual_div").value="";
		document.getElementById("otalMaxAccrual_div").value="";
	
		//window.shade.show();
		showSon();	
}
//删除操作
	function deleteacc(data){
		if(confirm("确认删除此信息！")){
		var cell=data.parentNode;
		var row=cell.parentNode;
		document.getElementById("tableIndex").value=row.rowIndex;  //当前行下标
		var data1 = row.cells[0].innerHTML;
		  $.ajax({
              type: "POST",
              url: "ruleOfAccCycleAction!deleteData.action",
              data: "rowNumber="+data1,
              success: function(result) {
                      if(result == "deleteSuccess"){
                    	 	 
                      }else{
                      	alert("删除失败！请稍后再试");
                      }
              }
      });
		  var curPage = document.getElementById(curPage);
		   refrsh(curPage);
	}
		   
}
//增加规则提交
function addsubmit(){

		var showFlog=4;
		var flog=1;
		var rowindex = document.getElementById("tableIndex").value;
		var modifyOrAdd = document.getElementById("modifyOrAdd").value;
		var acc = document.getElementById("acc_div").value;
		var sub = document.getElementById("sub_div").value;
		var min1 = document.getElementById("minBal_div").value;
		var max1 = document.getElementById("maxBal_div").value;
		var min2 = document.getElementById("oneMinAccrual_div").value;
		var max2 = document.getElementById("oneMaxAccrual_div").value;
		var min3 = document.getElementById("totalMinAccrual_div").value;
		var max3 = document.getElementById("otalMaxAccrual_div").value;
		//如果为"" 则赋初值0 
		if(min1 == ""){
			min1="0";
		}
		if(max1 == ""){
			max1="9999999999999";
		}
		if(min2 == ""){
			min2="0";
		}
		if(max2 == ""){
			max2="9999999999999";
		}
		if(min3 == ""){
			min3="0";
		}
		if(max3 == ""){
			max3="9999999999999";
		}
		//做校验  
		var patrn =/^([1-9][0-9]{0,12})|([0]{1})|([0-9]{0,12}).[0-9]{0-3}$/; 
		//余额校验
		if(!patrn.exec(min1)){
			alert("余额起 值错误!");
			flog=0;
		}	
		if(!patrn.exec(max1)){
			alert("余额止 值错误!");
			flog=0;
		}	
		//单笔发生额校验
		if(!patrn.exec(min2)){
			alert("单笔发生额起 值错误!");
			flog=0;
		}	
		if(!patrn.exec(max2)){
			alert("单笔发生额止 值错误!");
			flog=0;
		}	
		//累计发生额校验
		if(!patrn.exec(min3)){
			alert("累计发生额起 值错误!");
			flog=0;
		}	
		if(!patrn.exec(max3)){
			alert("累计发生额止 值错误!");
			flog=0;
		}	
		//起小于止	
		if(max1!="0"){
			if(min1!="0"){
				if(parseInt(min1)>parseInt(max1)){
					alert("余额起必须小于止的值！");
					flog=0;
				}
			}
		}
		if(max2!="0"){
			if(min2!="0"){
				if(parseInt(min2)>parseInt(max2)){
					alert("单笔发生额起必须小于止的值！");
					flog=0;
				}
			}
			
		}
		if(max3!="0"){
			if(min3!="0"){
				if(parseInt(min3)>parseInt(max3)){
					alert("累计发生额起必须小于止的值！");
					flog=0;
				}
			}	
		}
	
		if("" == acc){
			alert("请选择账户类型！");
			flog=0;
		}	
		var data = "accCycle="+acc+"&subjectNo="+sub+"&minBal="+min1
					+"&maxBal="+max1+"&oneMinAccrual="+min2+"&oneMaxAccrual="+max2
					+"&totalMinAccrual="+min3+"&otalMaxAccrual="+max3+"&modifyOrAdd="+modifyOrAdd
					+"&rowIndex="+rowindex;
		if(flog==1){
			   $.ajax({
	                type: "POST",
	                url: "ruleOfAccCycleAction!addOrModifyData.action",
	                data: data,
	                success: function(result) {
	                        if(result == "addormodify"){   
	                           var curPage = document.getElementById(curPage);
	               			   refrsh(curPage);
	                        }else{ 	
	                        	alert("操作失败！请稍后再试");
	                        }
	                }
	        });
			 
		}
		
}

	
