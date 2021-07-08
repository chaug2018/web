function judgeCountIdBranch() {
	if((document.getElementById("selectIdCenter").value.length !== 0) 
		&& (document.getElementById("countIdBranch").checked == true)
		&& (document.getElementById("selectIdBank").value.length == 0)){
		document.getElementById("alertErr").innerHTML="<font color='red'>分行不能统计总行</font>";
	}else if((document.getElementById("countIdBranch").checked == true)
			&& (document.getElementById("selectIdBank").value.length !== 0)){
		document.getElementById("alertErr").innerHTML="<font color='red'>网点不能统计总行</font>";
	}else{
		document.getElementById("alertErr").innerHTML="";
	}
}

function judgeCountIdCenter() {
	if((document.getElementById("selectIdBank").value.length)!== 0
			&& (document.getElementById("countIdCenter").checked == true)){
		document.getElementById("alertErr").innerHTML="<font color='red'>网点不能统计分行</font>";
	}else{
		document.getElementById("alertErr").innerHTML="";
	}
}

function judgeCountIdBank() {
	document.getElementById("alertErr").innerHTML="";
}

function judgeSelectIdBank(){
	if(document.getElementById("countIdBranch").checked == true){
		judgeCountIdBranch();
	}else if(document.getElementById("countIdCenter").checked == true){
		judgeCountIdCenter();
	}
}