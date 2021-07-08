var orgTree;
var idCenter_js;
var idBank_js;
var curLevel;

/**
 * 初始化树
 * 
 * @param data
 *            json对象
 */
function initTree(data, preIdCenter, preIdBank) {
	orgTree = data;
	if (orgTree == null) {
		alert("未获取到机构树信息，可能是机构参数表数据有误");
		top.refresh();
		return;
	}
	curLevel = orgTree.curLevel;
	idCenter_js = document.getElementById("selectIdCenter");
	idBank_js = document.getElementById("selectIdBank");
	var idCenters = null;
	var idBanks = null;
	var index = 0;
	idCenters = orgTree.children; // 分行列表
	while (idCenters.length > index) {
		if (idCenters[index].orgId == preIdCenter) {
			idBanks = idCenters[index].children;
			break;
		}
		index++;
	}
	if (idBanks == null && idCenters.length > 0) { // 上次没有被选中的值
		idBanks = idCenters[0].children;
	}
	initSelect(idCenter_js, idCenters, preIdCenter);
	initSelect(idBank_js, idBanks, preIdBank);
	changeIdBank();
	if($("#idBank").val().length>0){
		document.getElementById("idBank").readOnly = true;
		document.getElementById("idBank").style.color = "#a5a5a5";
		dealNullSelect();
	}
}

/**
 * 若某个下拉框没有选项值，则给其新加一个value为""的值，防止表单提交时，忽略这个下拉框，导致该下拉框上一次传到后台的值不能被覆盖
 */
function dealNullSelect() {
	if (idBank_js.length == 0) {
		idBank_js.options[0] = new Option("", "");
	}
}

function dealPrevalue() {
	
}

/**
 * 分行变动事件
 */
function changeIdCenter() {
	var index = idCenter_js.selectedIndex;
	var idCenters = orgTree.children;	
	var idBanks = idCenters[index].children;
	showSelect(idBank_js, idBanks);
	changeIdBank();
}


/**
 * 网点变动事件
 */
function changeIdBank() {
	var idbank = document.getElementById("idBank");
	idbank.value = idBank_js.value;
	dealNullSelect();
}

/**
 * \ 显示下拉框
 * 
 * @param lebal
 *            下拉框
 * @param children
 *            下拉框的内容
 * @param preValue
 *            上一次下拉框上的值
 * @returns
 */
function showSelect(lebal, children) {
	lebal.options.length = 0; // 清除下拉框
	if (children == null) {
		return;
	}
	var index = 0;
	while (index < children.length) {
		lebal.options[index] = new Option(children[index].orgName,
				children[index].orgId);
		index++;
	}

}

/**
 * \ 初始化下拉框
 * 
 * @param lebal
 *            下拉框
 * @param children
 *            下拉框的内容
 * @param preValue
 *            上一次下拉框上的值
 * @returns
 */
function initSelect(lebal, children, preValue) {
	if (children == null || children.length == 0) {
		return;
	}
	lebal.options.length = 0; // 清除下拉框
	var index = 0;
	var preValueIndex = -1;
	while (index < children.length) {
		lebal.options[index] = new Option(children[index].orgName,
				children[index].orgId);
		if (preValue != null && children[index].orgId == preValue) {
			preValueIndex = index;
		}
		index++;
	}

	if (preValueIndex != -1) {
		lebal.options[preValueIndex].selected = true;
	} else {
		lebal.options[index - 1].selected = true;
	}
}