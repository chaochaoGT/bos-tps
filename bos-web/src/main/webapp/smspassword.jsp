<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回密码主页</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/style.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/css/style_grey.css" />
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link id="easyuiTheme" rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<style>
input[type=text] {
	width: 80%;
	height: 25px;
	font-size: 12pt;
	font-weight: bold;
	margin-left: 45px;
	padding: 3px;
	border-width: 0;
}

input[type=password] {
	width: 80%;
	height: 25px;
	font-size: 12pt;
	font-weight: bold;
	margin-left: 45px;
	padding: 3px;
	border-width: 0;
}

#loginform\:codeInput {
	margin-left: 1px;
	margin-top: 1px;
}

#loginform\:vCode {
	margin: 0px 0 0 60px;
	height: 34px;
}
</style>
<script type="text/javascript">
	if(window.self != window.top){
		window.top.location = window.location;
	}
	var active = true;//  判断按钮是否可以120s之内点击
	var second = 120; // 倒计时120秒
	var secondInterval;
	$(function(){
		// 发送验证码操作
		$("#go").click(function(){
			if(active == false) {
				return;
			}
			//  发送短信给用户手机..
				// 1 发送一个HTTP请求，通知服务器 发送短信给目标用户 
					var regex = /^1(3|5|7|8)\d{9}$/;
					var telephone =$("input[name='telephone']").val();// 获取用户输入的手机号
					if(telephone!=''&&regex.test(telephone)) {
						// 校验通过
						$.ajax({
							method: 'POST',
							url: '${pageContext.request.contextPath }/userAction_sendSms',// 发送验证码
							data : {
								telephone : telephone
							},
							success:function(data) {
								if(data){
								    $.messager.alert("恭喜!","发送短信成功,请查收手机!","info");
								    active = true;
									  if(active){
									      active = false;
											secondInterval = setInterval(function() {
												if(second < 0) {
													// 倒计时结束，允许重新发送 
													$("#go").html("重发验证码");
													active = true;
													second = 120;
													// 关闭定时器
													clearInterval(secondInterval);
													secondInterval = undefined;
												} else {
													// 继续计时
														$("#go").html(second + "秒后重发");
														second--;
												}
											}, 1000);
									  }
								}else{
									alert("发送短信出错，重新操作....");
									active = false;
								}
							}
						});
					} else {
						// 校验失败 
						$.messager.alert("警告!","手机号非法，请重新输入 ","warning");
						return;
					}
		});
		// 点击确认发送 发送用户验证码...
		$("#con").click(function(){
			if($("input[name='checkcode']").val()==""){
				$.messager.alert("警告!","请输入验证码","warning");
				return;
			}
			// 后台接受用户的验证码和手机号 后台代码判断合法有效
		$.post("${pageContext.request.contextPath }/userAction_smsPassword",
				{"checkcode":$("input[name='checkcode']").val(),"telephone":$("input[name='telephone']").val()},function(data){
				if(data=="3"){
					// 0 验证按不存在 1 验证码输入错误 2 手机号错误 3 有效
				    // 弹出窗体 进行新密码修改操作... 如果手机号合法有效 验证码合法有效 才会弹出新密码找回对话框
					$('#confirmpwd').window("open");
				}else if(data=="0"){
					$.messager.alert("错误!","验证码过时,请重新获取验证码","error");
				}else if(data=="1"){
					$.messager.alert("错误!","验证码错误,请重新输入","error");
				}else if(data=="2"){
					$.messager.alert("错误!","请输入正确的手机号!","error");
				}
			});
		});
		// 找回密码对话框 输入新密码  提交更新数据库密码... 
		$("#newpassword").click(function(){
			//  js 校验 不能出现空白字符   \s  空白字符  
			var reg = /\s+/;
			if($("#txtNewPass").val()==''||reg.test($("#txtNewPass").val())){
				$.messager.alert("警告!","请输入有效密码!","warning");
				return;
			}
	        //  密码长度设置
			if($("#txtNewPass").val().length<6||$("#txtNewPass").val().length>10){
				$.messager.alert("警告!","密码必须6-10位!","warning");
				return;
			}
			// 两次密码一致
			if($("#txtNewPass").val()!=$("#txtRePass").val()){
				$.messager.alert("警告!","两次密码必须一致!","warning");
				return;
			}
			//  如果密码合法   更新数据库  
			$.post("${pageContext.request.contextPath}/userAction_gobackpassword",
					{"password":$("input[name='password']").val(),"telephone":$("input[name='telephone']").val()}
					,function(data){
				if(data){
					$.messager.alert("恭喜!","密码找回,请重新登录!","info");
					location.href="${pageContext.request.contextPath}/login.jsp";
				}else{
					$.messager.alert("可惜!","密码找回失败,该手机没有绑定账号","error");
				}
			});
		});
		//  btnCancel  关闭窗口
		$("#btnCancel").click(function(){
			$('#confirmpwd').window("close");
		});
		
	});
</script>
</head>
<body>
	<div
		style="width: 900px; height: 50px; position: absolute; text-align: left; left: 50%; top: 50%; margin-left: -450px;; margin-top: -280px;">
		<img src="${pageContext.request.contextPath }/images/logo.png" style="border-width: 0; margin-left: 0;" />
		<span style="float: right; margin-top: 35px; color: #488ED5;">新BOS系统以宅急送开发的ERP系统为基础，致力于便捷、安全、稳定等方面的客户体验</span>
	</div>
	<div class="main-inner" id="mainCnt"
		style="width: 900px; height: 440px; overflow: hidden; position: absolute; left: 50%; top: 50%; margin-left: -450px; margin-top: -220px; background-image: url('${pageContext.request.contextPath }/images/bg_login.jpg')">
		<div id="loginBlock" class="login"
			style="margin-top: 80px; height: 255px;">
			<div class="loginFunc">
				<div id="lbNormal" class="loginFuncMobile">密码找回</div>
			</div>
			<div class="loginForm">
				<form id="newsmsform" name="loginform" method="post" class="niceform"
					action="#">
					<div id="idInputLine" class="loginFormIpt showPlaceholder"
						style="margin-top: 5px;">
						<input id="loginform:idInput" type="text" name="telephone"
							class="loginFormTdIpt" maxlength="50"/>
						<label for="idInput" class="placeholder" id="idPlaceholder">手机号：</label>
					</div>
					<div class="forgetPwdLine"></div>
					<div id="pwdInputLine" class="loginFormIpt showPlaceholder">
						<input id="loginform:pwdInput" class="loginFormTdIpt" type="text"
							name="checkcode"/>
						<label for="pwdInput" class="placeholder" id="pwdPlaceholder">验证码：</label>
						
					</div>
						<div class="loginFormIpt loginFormIptWiotTh"
						style="margin-top:58px;">
						<a href="javascript:void(0);" id="loginform:j_id19" name="loginform:j_id19">
						<span
							id="go" class="btn btn-login"
							style="margin-top:-36px;margin-right: 155px">发送验证码</span>
						<span
							id="con" class="btn btn-login"
							style="margin-top:-36px;">确认发送</span>
						</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div
		style="width: 900px; height: 50px; position: absolute; text-align: left; left: 50%; top: 50%; margin-left: -450px;; margin-top: 220px;">
		<span style="color: #488ED5;">Powered By www.itcast.cn</span><span
			style="color: #488ED5;margin-left:10px;">推荐浏览器（右键链接-目标另存为）：<a
			href="http://download.firefox.com.cn/releases/full/23.0/zh-CN/Firefox-full-latest.exe">Firefox</a>
		</span><span style="float: right; color: #488ED5;">宅急送BOS系统</span>
	</div>
	<!-- 基于easyui完成输入新密码对话框  modal="true"  表示遮罩相关的对话框 -->
	<div id="confirmpwd" class="easyui-window" title="找回密码"  
	collapsible="false" minimizable="false" modal="true" closed="true" resizable="false"
        maximizable="false" icon="icon-save"  style="width: 400px; height: 180px; padding: 5px;
        background: #fafafa">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>    <!-- 密码  长度限制 6 数字  非空 不能出现空白字符   -->
                        <td>新密码：</td>
                        <td><input id="txtNewPass" type="text" name="password" class="txt01" /></td>
                    </tr>
                    <tr>
                        <td>确认密码：</td>
                        <td><input id="txtRePass" type="text" name="repassword" class="txt01" /></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="newpassword" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" >提交新密码</a> 
                <a id="btnCancel" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)">取消重置</a>
            </div>
        </div>
    </div>
</body>
</html>