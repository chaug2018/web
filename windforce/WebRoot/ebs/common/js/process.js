/**
 * 启动进度条
 */
function startProcess(notice){
	top.startProcess(notice,document);
};

/**
 * 关闭进度条
 */
function stopProcess(){
	top.stopProcess(document);
};

function changeProcessTitle(notice){
	top.changeProcessTitle(notice,document);
}

/**
 * 打开理由列表
 * 
 * @param reasons
 *            后台传过来的包含理由信息的数组
 * @param actionPath
 *            任务需要提交的地址
 * @param nextTaskPath
 *            下一笔任务的地址
 */
function openUntreadReason(reasons,actionPath,nextTaskPath,params){
	var reasonTable=document.getElementById("reasonTable");
	var index=0;
	var content;
	var row=null;
	var cell;
	while(index<reasons.length){
		content="";
		if(index%2==0){
			row=reasonTable.insertRow(-1);
		}
		cell=row.insertCell(-1);
		content=content+"<td>&nbsp&nbsp" +
				"<input type='checkbox'  name='reason' onClick='showData();'  id='checkboxId"+index+"' value='"+reasons[index]+"'/>&nbsp<label for='checkboxId"+index+"'>"+
				reasons[index]+"</label></td>";
		if(index==reasons.length-1){
			content=content+"<input type='hidden' id='actionPath' name='actionPath' value='"+actionPath+"'/>";
			content=content+"<input type='hidden' id='nextTaskPath' name='nextTaskPath' value='"+nextTaskPath+"'/>";
			content=content+"<input type='hidden' id='params' name='params' value='"+params+"'/>";
		}
		cell.innerHTML=content;
		index=index+1;
	}
	$(".overlay").css({
		'display' : 'block',
		'opacity' : '0.8'
	});
	$(".untreadReason").stop(false).animate({
		'opacity' : '1'
	}, 200);
	$(".untreadReason").css({
		'display' : 'block',
		'opacity' : '0'
	});
  document.getElementById("untreadReasonInput").style.color="#AAAAAA";
};


function closeUntreadReason(){
	var reasonTable=document.getElementById("reasonTable");
	var input=document.getElementById("untreadReasonInput");
	input.value="请选择或输入理由信息";
	//$(".untreadReason").stop(false).animate({
		//'opacity' : '0'
	//},200);
	$(".overlay").css({
		'display' : 'none',
		'opacity' : '0'
	});
	$(".untreadReason").css({
		'display' : 'none',
		'opacity' : '0'
	});
	while(reasonTable.rows.length!=0){
		reasonTable.deleteRow(0);
	}	
};

function initUi(path){
	 var content="<div class='overlay'></div>";
		 content=content+"<div id='AjaxLoading' class='showbox'>";
	     content=content+"<div class='loadingWord'>";
	     content=content+"<img src='"+path+"/ebs/common/images/waiting.gif'></img>";
	     content=content+"<p id='processNotice'></p>";
	     content=content+"</div>";
	     content=content+"</div>";
	     content=content+"<div class='untreadReason' id='untreadReason'>";
	     content=content+"<div class='reasons'>";
	     content=content+"</div>";
	     content=content+"</div>";
	     document.write(content);
}

/**
 * 显示子窗口
 */
function showSon(){
	$(".overlay").css({
		'display' : 'block',
		'opacity' : '0.8'
	});
	$(".son_div").css({
		'display' : 'block'
	});
	$(".sonpage").css({
		'display' : 'block'
	});
	$(".son_div").stop(false).animate({
		'margin-top' : '200px',
		'opacity' : '1'
	}, 200);
	$(".sonpage").stop(false).animate({
	     'opacity' : '1'
	}, 200);
}
function showSon2(){
	$(".overlay").css({
		'display' : 'block',
		'opacity' : '0.8'
	});
	$(".son2_div").css({
		'display' : 'block'
	});
	$(".sonpage2").css({
		'display' : 'block'
	});
	$(".son2_div").stop(false).animate({
		'margin-top' : '200px',
		'opacity' : '1'
	}, 200);
	$(".sonpage2").stop(false).animate({
	     'opacity' : '1'
	}, 200);
}

/**
 * 关闭子窗口
 */
function closeSon(){
	$(".son_div").css({
		'display' : 'none'
	});
	$(".sonpage").css({
		'display' : 'none'
	});
	$(".overlay").css({
		'display' : 'none',
		'opacity' : '0'
	});
	$(".son_div").stop(false).animate({
		'margin-top' : '250px',
		'opacity' : '0'
	},200);	
	$(".sonpage").stop(false).animate({
	     'opacity' : '0'
	}, 200);
}
function closeSon2(){
	$(".son2_div").css({
		'display' : 'none'
	});
	$(".sonpage2").css({
		'display' : 'none'
	});
	$(".overlay").css({
		'display' : 'none',
		'opacity' : '0'
	});
	$(".son2_div").stop(false).animate({
		'margin-top' : '250px',
		'opacity' : '0'
	},200);	
	$(".sonpage2").stop(false).animate({
	     'opacity' : '0'
	}, 200);
}
function onFoucs(o){
//	   document.getElementById("untreadReasonInput").style.color="#FF0000";
	document.getElementById("untreadReasonInput").style.color="#333333";
	   if(o.value == "请选择或输入理由信息"){
	    o.value = "";
	   }
	}

	function onBlur(o){
	   if(o.value == ""){
		document.getElementById("untreadReasonInput").style.color="#AAAAAA";
	    o.value ="请选择或输入理由信息";
	   }
	}
	function   IsDate(sm)   {
		 var strDate = sm.value;
		 
         var result = strDate.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
         if(strDate == "")
        	 {
        	 return true;
        	 }else{
         if(null==result){
        	 alert("请保证"+sm.title+"中输入的日期格式为yyyy-mm-dd或正确的日期!"); 
        	 docdate.focus();
              return false; 
          } else {
               return true;
        }
        	 }
         
	     
	    } 
	function   IsShortDate(sm)   {
		 var strDate = sm.value;
		 
        var result = strDate.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
        if(strDate == "")
       	 {
       	 return true;
       	 }else{
        if(null==result){
       	 alert("请保证"+sm.title+"中输入的日期格式为yyyy-mm-dd或正确的日期!"); 
       	 //docdate.focus();
             return false; 
         } else {
              return true;
       }
       	 }
        
	     
	    }