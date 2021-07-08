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
	var queryString = $.param(formData);
	// jqForm is a jQuery object encapsulating the form element. To access the
	// DOM element for the form do this:
	// var formElement = jqForm[0];
	var power = document.getElementById("powerName");
	var xmlFile = document.getElementById("xmlName");
	queryString = '['+queryString+']';
	if(power != null && xmlFile != null)
		{
		var currReportName = document.getElementById("powerName").value;
		var xmlName = document.getElementById("xmlName").value;
		 $.getJSON("queryDataTable_createHeadCol.action?powerName="+currReportName+"&xmlName="+xmlName, function(json){
			  
			  queryData(json,queryString);
		
		});
		}

	//$('#myForm').append("<input type='hidden' name='queryForm' value='{"+queryString+"}'/>");

	 // 此处是获取表格的表头信息
	 
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
