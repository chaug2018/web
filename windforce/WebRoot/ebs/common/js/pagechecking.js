//验证只能为数字  
function checkNumber(obj){  
    var reg = /^[0-9]+$/;  
    if(obj.value!=""&&!reg.test(obj.value)){  
        alert(obj.title+"只能输入数字！");  
        obj.focus();  
        return false;  
    }else{
    	return true;
    }
} 
//验证邮政编码
function check_youbian(obj){
	var reg=/^\d{6}$/; 
	if(obj.value!=""&&!reg.test(obj.value)){
		alert('邮政编码格式输入错误！');
		obj.focus();
		return false;
	}
}
//页面输入的是否是正确格式的日期yyyy-mm-dd
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
   }} 
  } 