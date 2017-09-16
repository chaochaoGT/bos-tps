<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加业务受理单</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$("body").css({visibility:"visible"});
		load(0,province);//  页面加载立刻执行   传递value  0    ,目标对象  province
		// 对save按钮条件 提交表单
		$('#save').click(function(){
			// 对form 进行校验
			//if($('#noticebillForm').form('validate')){
	// 提交新单 将省市区信息中文信息发送到后台   获取省市区文本值 以隐藏域的形式提交给后台
		   var nprovincetext=document.getElementById("province").selectedOptions[0].text;
	       var ncitytext = document.getElementById("city").selectedOptions[0].text
	       var ndistricttext = document.getElementById("district").selectedOptions[0].text;
	       // 给隐藏域赋值
		   $("#nprovince").val(nprovincetext);
		   $("#ncity").val(ncitytext);
		   $("#ndistrict").val(ndistricttext);
		   $('#noticebillForm').submit();
		//	}
		});
		// 页面加载完成 
		$("#telephone").blur(function(){
			  if(this.value==""){
				  alert("手机号必须书写");
				  return ;
			  }else{
				  $.post("${pageContext.request.contextPath}/noticeBillAction_findCustomerByTelephone",{"telephone":this.value},function(data){
					    // data  {customer}
					    if(data==null){
					    	//  新客户 提示
					    	$("#tel_sp").html("<font color='green'>新客户</font>");
					        $("#customerId").val("");
					        $("#customerId").hide();//  hide 隐藏
					    	$("#customerName").val("");
					    }else{
					    	// 老客户  回显信息
					    	$("#tel_sp").html("<font color='blue'>老用户</font>");
					    	 $("#customerId").show();// 
					    	$("#customerId").val(data.id);
					    	$("#customerId").attr("readonly","readonly");
					    	$("#customerName").val(data.name);
					    }
				  });
			  }
		});
		
	});
	//  三级联动 省市区数据获取  target表示目标生成数据的下拉框
	function load(pid, target) {
		target.length = 1; // 清空目标下拉框
		district.length = 1; // 同时清空县数ju
		if (pid=="none")return;
		//  加载  省市区 所有实现方法
		$.ajax({
			url: "${pageContext.request.contextPath}/loadCityAction_load",
			type:"POST",
			data:{"pid" : pid},  
			success: function(result) {
				$(result).each(function(){
					var opt = document.createElement("option");
					opt.value = this.id;
					opt.innerHTML = this.name;
					$(target).append(opt);
				});
			}
		});
	}
</script>
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="north" style="height:31px;overflow:hidden;" split="false"
		border="false">
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton"
				plain="true">新单</a>
			<a id="edit" icon="icon-edit" href="#" class="easyui-linkbutton"
				plain="true">工单操作</a>	
		</div>
	</div>
	<div region="center" style="overflow:auto;padding:5px;" border="false">
		<form id="noticebillForm" action="${pageContext.request.contextPath }/noticeBillAction_save" method="post">
			<table class="table-edit" width="95%" align="center">
				<tr class="title">
					<td colspan="4">客户信息</td>
				</tr>
				<tr>
					<td>来电号码:</td>
					<td>
					<!-- 表单提交将省市区中文信息发送给后台 -->
					 <input type="hidden" name="nprovince"  id="nprovince">
					 <input type="hidden" name="ncity"  id="ncity">
					 <input type="hidden" name="ndistrict"  id="ndistrict">
					<input type="text" class="easyui-validatebox" name="telephone"
						required="true" id="telephone" />
						 <span id="tel_sp"></span>
						</td>
					<td>客户编号:</td>
					<td><input id="customerId" type="text"  name="customerId"/></td>
				</tr>
				<tr>
					<td>客户姓名:</td>
					<td>
					
					<input id="customerName" type="text" class="easyui-validatebox" name="customerName"
						required="true" /></td>
				</tr>
				<tr class="title">
					<td colspan="4">货物信息</td>
				</tr>
				<tr>
					<td>品名:</td>
					<td><input type="text" class="easyui-validatebox" name="product"
						required="true" /></td>
					<td>件数:</td>
					<td><input type="text" class="easyui-numberbox" name="num"
						required="true" /></td>
				</tr>
				<tr>
					<td>重量:</td>
					<td><input type="text" class="easyui-numberbox" name="weight"
						required="true" /></td>
					<td>体积:</td>
					<td><input type="text" class="easyui-validatebox" name="volume"
						required="true" /></td>
				</tr>
				<tr>
					<td>取件地址</td>
					<td colspan="3">
					 省&nbsp;
					 <select name="province" id="province" onchange="load(value,city);">
					  <option value="none">--请选择--</option>
					 </select>&nbsp;
					 市&nbsp;
					 <select name="city" id="city" onchange="load(value,district);">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;
					 区&nbsp;
					 <select name="district" id="district">
					   <option value="none">--请选择--</option>
					 </select>&nbsp;详细地址
					<input type="text" class="easyui-validatebox" name="pickaddress" required="true" size="75"/>
				  </td>
				</tr>

				<tr>
					<td>到达城市:</td>
					<td><input type="text" class="easyui-validatebox" name="arrivecity"
						required="true" /></td>
					<td>预约取件时间:</td>
					<td><input type="text" class="easyui-datebox" name="pickdate"
						data-options="required:true, editable:false" /></td>
				</tr>
				<tr>
					<td>备注:</td>
					<td colspan="3"><textarea rows="5" cols="80" type="text" class="easyui-validatebox" name="remark"
						required="true" ></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>