	function addSort() {
		// 增加一行未达项记录
		//获取输入值
		var docid = document.getElementById("docId").value;
		var voucherno = document.getElementById("voucherNo").value;
		var accno = document.getElementById("accNo").value;
		var trandate = document.getElementById("traceDate").value;
		var tranno = document.getElementById("traceNo").value;
		var inputdesc = document.getElementById("inputDesc").value;
		var amttran = document.getElementById("traceCredit").value;
		var reg=new RegExp("^[0-9]+(.[0-9]{1,4})?$");
		if(!reg.test(amttran)){
			alert("金额格式不对，请输入纯数字(小数点后不得超过4位)");
			return;
		}
		var direction_index = document.getElementById("direction");
		var direction = direction_index.options[direction_index.selectedIndex].text;
		var result_index = document.getElementById("result");
		var result = result_index.options[result_index.selectedIndex].text;
		var lineCount = document.getElementById("lineCount").value;
		//判断是否为空
		if (accno == "") {
			alert("请输入账号！");
			document.getElementById("accNo").focus();
			return;
		} else if (!CheckDate(trandate)) {
			document.getElementById("traceDate").focus();
			return;
		} 
//放开凭证号  （为什么要放开？ modify 20160114 加上这个）
		else if (tranno == "") {
			alert("请输入凭证号！");
			document.getElementById("traceNo").focus();
			return;
		} 
		else if (amttran == "") {
			alert("请输入发生金额！");
			document.getElementById("traceCredit").focus();
			return;
		} else if (direction == "" || direction == "--请选择--") {
			alert("请输入未达方向！");
			document.getElementById("direction").focus();
			return;
		} else if (result == "" || result == "--请选择--") {
			alert("请输入核对结果！");
			document.getElementById("result").focus();
			return;
		}
		$.post("notMatchInputAction_confirmTask.action", "accNo="+accno , function(data) {
		var isConfirm = false;
		if("1" == data)
		{
			if(confirm("该账号在账单中不存在,确定要继续吗?"))
				{
				isConfirm = true;
				}
	    }
		if(data == "2" || isConfirm)
			{
		//创建一行
		var row = document.createElement("tr");
		var item = accno;
		row.setAttribute("id", item);
		if(lineCount%2==0){
		row.setAttribute("bgcolor","#F4DDDF");
		}

		var cell0 = document.createElement("td");
		var cell1 = document.createElement("td");
		var cell2 = document.createElement("td");
		var cell3 = document.createElement("td");
		var cell4 = document.createElement("td");
		var cell5 = document.createElement("td");
		var cell6 = document.createElement("td");
		var cell7 = document.createElement("td");
		var cell8 = document.createElement("td");
		var cell9 = document.createElement("td");

		cell0.appendChild(document.createTextNode(lineCount));
		cell1.appendChild(document.createTextNode(docid));
		cell2.appendChild(document.createTextNode(voucherno));
		cell3.appendChild(document.createTextNode(accno));
		cell4.appendChild(document.createTextNode(trandate));
		cell5.appendChild(document.createTextNode(amttran));
		cell6.appendChild(document.createTextNode(tranno));
		cell7.appendChild(document.createTextNode(inputdesc));
		cell8.appendChild(document.createTextNode(direction));
		cell9.appendChild(document.createTextNode(result));

		row.appendChild(cell0);
		row.appendChild(cell1);
		row.appendChild(cell2);
		row.appendChild(cell3);
		row.appendChild(cell4);
		row.appendChild(cell5);
		row.appendChild(cell6);
		row.appendChild(cell7);
		row.appendChild(cell8);
		row.appendChild(cell9);
		
		var deleteButton = document.createElement("a");
		deleteButton.id = "a1";
		deleteButton.href = "#this";
		deleteButton.innerText = "删除";
		
		deleteButton.title = "delete";
		deleteButton.onclick = function() {
			deleteSort(item);
		};
		
		var editButton = document.createElement("a");
		editButton.id = "a2";
		editButton.href = "#this";
		editButton.innerText = "编辑";
		editButton.title = "modify";
		editButton.onclick = function(){
			modifyNotmatchItem(editButton);
		};
		cell10 = document.createElement("td");
		cell10.appendChild(editButton);
		var text = document.createTextNode("|");
		cell10.appendChild(text);
		cell10.appendChild(deleteButton);
		row.appendChild(cell10);
		
		document.getElementById("sortList").appendChild(row);

		//清空输入框
		document.getElementById("accNo").value = "";
		document.getElementById("traceDate").value = "";
		document.getElementById("traceNo").value = "";
		document.getElementById("inputDesc").value = "";
		document.getElementById("traceCredit").value = "";
		document.getElementById("direction").value = "-1";
		document.getElementById("result").value = "-1";

		document.getElementById("lineCount").value++;
		//重新设置焦点
		document.getElementById("accNo").focus();
			}
		});
	}
	// 删除选中行
	function deleteSort(id) {
		if (id != null) {
			//获取待删除行
			var rowToDelete = document.getElementById(id);
			var sortList = document.getElementById("sortList");
			sortList.removeChild(rowToDelete);
			//行计数减1
			document.getElementById("lineCount").value--;
			//重新设置行号及行背景色
			var objTable = document.getElementById("myTable");
			if (objTable.rows.length>1) {
				for ( var i = 1; i < objTable.rows.length+1; i++) {
					objTable.rows[i].cells[0].innerText=objTable.rows[i].cells[0].parentElement.rowIndex;
					if(objTable.rows[i].cells[0].parentElement.rowIndex%2==0){
						objTable.rows[i].setAttribute("bgcolor","#F4DDDF");
					}else if (objTable.rows[i].cells[0].parentElement.rowIndex%2==1){
						objTable.rows[i].setAttribute("bgcolor","#EBEBE6");
					}
				}
			}
		}
	}
	function deleteRow(name){
		deleteCurrentRow(name);
		//行计数减1
		document.getElementById("lineCount").value--;
		//重新设置行号及行背景色
		var objTable = document.getElementById("myTable");
		if (objTable.rows.length>1) {
			for ( var i = 1; i < objTable.rows.length+1; i++) {
				objTable.rows[i].cells[0].innerText=objTable.rows[i].cells[0].parentElement.rowIndex;
				if(objTable.rows[i].cells[0].parentElement.rowIndex%2==0){
					objTable.rows[i].setAttribute("bgcolor","#F4DDDF");
				}else if (objTable.rows[i].cells[0].parentElement.rowIndex%2==1){
					objTable.rows[i].setAttribute("bgcolor","#EBEBE6");
				}
			}
		}
	}
function deleteCurrentRow(obj){
	var tr = obj.parentNode.parentNode;
	var tbody = tr.parentNode;
	tbody.removeChild(tr);
}
	////////////////////////////////////////////////////////////////////////////////////////////
	//校验日期
	function CheckDate(strDate){
    //var strDate = document.getElementById("date_hour").value; 
    
    var reg=/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})/;
    if(!reg.test(strDate)){
        alert("日期格式不正确!正确格式为:YYYY-MM-DD");
        return false;
    }
    var ss=strDate.split("-");
    var year=ss[0];
    var month=ss[1];
    var date=ss[2];
    if(!checkYear(year)){return false;}
    if(!checkMonth(month)){return false;}
    if(!checkDate(year,month,date)){return false;}
    return true;
	}
	function checkYear(year){
    if(isNaN(parseInt(year)))
        {alert("年份输入有误,请重新输入!");
         return false;
    }
    else if(parseInt(year)<1950 || parseInt(year) >2050)
    { 
        alert("年份应该在1950-2050之间!"); 
        return false;
    }
    else return true;
}
function checkMonth(month){
if(isNaN(parseInt(month,10))){alert("月份输入有误,请重新输入!"); return false;}
    else if(parseInt(month,10) >12)
    { alert("月份应该在1-12之间!");
    return false;}
    else return true;
}
function checkDate(year,month,date){
var daysOfMonth=CalDays(parseInt(year),parseInt(month));
if(isNaN(parseInt(date))){alert("日期输入有误,请重新输入!"); return false;}
    else if(parseInt(date)>daysOfMonth){ alert("日期应该在1-"+daysOfMonth+"之间!"); return false;}
    else return true;
}
function CalDays(year,month){
var date= new Date(year,month,0);
return date.getDate();
}
function isLeapYear(year){
if((year %4==0 && year %100!=0) || (year %400==0)) return true;
else return false;
}

function submitForm(){
    if($('date_hour')){
        $('date_hour').value = '';
    }
    if($('fromDate_day')){
        $('fromDate_day').value = '';
    }
    if($('toDate_day')){
        $('toDate_day').value = '';
    }
    $('loginCountStatForm').submit();
}
