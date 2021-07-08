

调用的时候通过:top。xxx进行调用

/**
 * 自定义提示框
 * @param strContent 需要提示的内容
 * @param useCheck 是否显示禁用选项
 * @param parentView 在哪个界面进行弹出，如为空，则直接在最外层进行弹出
 * @param callback 关闭框时的回调函数
 */
function wfAlert(strContent,useCheck,parentView,callback)

/**
 * 打开进度条
 * @param strTitle 内容
 * @param parentView 父页面
 */
function startProcess(strTitle,parentView)

/**
 * 显示一个弹出界面
 * @param url 界面地址，可以使jsp，也可以是action
 * @param params 需要传递的参数
 * @param titleDesc 页面头描述
 * @param width 页面宽度
 * @param height 页面高度
 * @param parentView 页面所在的父页面
 */
function showPage(url,params,titleDesc,width,height,parentView)

/**
 * 关闭弹出窗口
 * @param parentView 父页面
 */
function closeShowPage(parentView)

/**
 * 修改进度条描述
 * @param desc 描述
 * @param parentView 父页面
 */
function changeProcessTitle(desc,parentView)

/**
 * 停止进度条
 * @param parentView 父页面
 */
function stopProcess(parentView)

