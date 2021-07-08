/**
 * submit="true" match="conditionKey"
 */
$(document).ready(function() {
	var options = {
		// target: '#output2', // target element(s) to be updated with server
		// response
		beforeSubmit : showRequest, // pre-submit callback
		success : showResponse
	// post-submit callback

	// other available options:
	// url: url // override for form's 'action' attribute
	// type: type // 'get' or 'post', override for form's 'method' attribute
	// dataType: null // 'xml', 'script', or 'json' (expected server response
	// type)
	// clearForm: true // clear all form fields after successful submit
	// resetForm: true // reset the form after successful submit

	// $.ajax options can be used here too, for example:
	// timeout: 3000
	};
	
	// bind to the form's submit event
	$('#myForm').submit(function() {
		// inside event callbacks 'this' is the DOM element so we first
		// wrap it in a jQuery object and then invoke ajaxSubmit
		$(this).ajaxSubmit(options);

		// !!! Important !!!
		// always return false to prevent standard browser submit and page
		// navigation
		return false;
	});

});

// pre-submit callback
function showRequest(formData, jqForm, options) {
	// formData is an array; here we use $.param to convert it to a string to
	// display it
	// but the form plugin does this for you automatically when it submits the
	// data
	var isValid = true;
	var tags = document.getElementsByTagName("input");
	for(var i = 0 ; i < tags.length;i++)
		{
		if(isValid)
			{
		if(tags[i].value == null || tags[i].value == "")
			{
			alert(tags[i].title+"不能为空！");
			isValid = false;
			}
			}
		}
	var tags2 = document.getElementsByTagName("select");
	for(var i = 0 ; i < tags2.length;i++)
	{
	if(isValid)
		{
	if(tags2[i].value == null || tags2[i].value == "")
		{
		alert(tags2[i].title+"不能为空！");
		isValid = false;
		}
		}
	}
	
	var index = 0;
	if(isValid) {
		var queryString = $.param(formData);
		// jqForm is a jQuery object encapsulating the form element. To access the
		// DOM element for the form do this:
		// var formElement = jqForm[0];
		queryString = '['+queryString+']';
		var reportName = document.getElementById("reportName").value;
		var isOnly = true;
		
		for(var i = 0 ; i < tags.length;i++) {
			index++;
			if(tags[i].checkType == "01") {
				isOnly = false;
				//查数据库，进行比对
				$.post("ParamsManagerAction_getDataforIsiterance.action","opeateType=0&checkTableName="+reportName+"&checkKey="+tags[i].id+"&checkValue="+tags[i].value,function(data){
					if(data!=0){
						isValid = false;
						alert(data);
				
					}
					if(index == tags.length)
					{
					 if(isValid)
					 {
					 $.post("ParamsManagerAction_addParams.action","queryForm="+queryString+"&reportName="+reportName, function(data){
						 alert(data);
						 //top.paramframeForward("ParamsManagerAction_getReportData.action?reportName="+reportName);
						// 关闭界面 
						window.close();
						window.returnValue ="true";
					 });
					 }
					}
					
				});
				
			}
		
		}
		if(index == tags.length && isOnly)
			{
			$.post("ParamsManagerAction_addParams.action","queryForm="+queryString+"&reportName="+reportName, function(data){
				 alert(data);
				 //top.paramframeForward("ParamsManagerAction_getReportData.action?reportName="+reportName);
				// 关闭界面 
				window.close();
				window.returnValue ="true";
			 });
			}
		
	//$('#myForm').append("<input type='hidden' name='queryForm' value='{"+queryString+"}'/>");

	 // 此处是获取表格的表头信息
		}
	
	// here we could return false to prevent the form from being submitted;
	// returning anything other than false will allow the form submit to
	// continue
	return false;
}

// post-submit callback
function showResponse(responseText, statusText, xhr, $form) {
	// for normal html responses, the first argument to the success callback
	// is the XMLHttpRequest object's responseText property

	// if the ajaxSubmit method was passed an Options Object with the dataType
	// property set to 'xml' then the first argument to the success callback
	// is the XMLHttpRequest object's responseXML property

	// if the ajaxSubmit method was passed an Options Object with the dataType
	// property set to 'json' then the first argument to the success callback
	// is the json data object returned by the server

	alert('status: '
			+ statusText
			+ '\n\nresponseText: \n'
			+ responseText
			+ '\n\nThe output div should have already been updated with the responseText.');
	return false;
}
function initFormData(formId) {
	$("#" + formId)
			.find("input")
			.each(
					function(i) {
						var currName = $("#" + formId).find("input")[i].name;
						var currType = $("#" + formId).find("input")[i].type;
						if (currType == "text" || currType == "") {
							//$("#" + formId).find("input")[i].value = "${reportName}";//"<s:property value='"
								//	+ $("#" + formId).find("input")[i].name
							//		+ "'/>";
						} else if (currType == "checkbox") {

						} else if (currType == "radio") {

						}
					});


};
