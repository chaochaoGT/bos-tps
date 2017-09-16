<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	function doAdd(){
		//alert("增加...");
		$('#addStaffWindow').window("open");
	}
	
	function doView(){
		//  弹出窗体
		$("#queryStaffWindow").window("open");
	}
	
	function doDelete(){
		alert("删除...");
	}
	
	function doRestore(){
		alert("将取派员还原...");
	}
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center'
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center'
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="1"){
				return "有";
			}else{
				return "无";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data==1){
				return "正常使用"
			}else{
				return "已作废";
			}
		}
	}, {
		field : 'standard',
		title : '取派标准',
		width : 120,
		align : 'center'
	}, {
		field : 'station',
		title : '所谓单位',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [1,2,3],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/staffAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '添加/更新取派员',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false,
	        onBeforeClose:function(){
	        	//  清除表单数据
	        	$("#addStaffForm")[0].reset();
	        	$("#qid").val("");
	        }
	    });
		
		//页面加载完成
		$("#save").click(function(){
			
			if($("#addStaffForm").form("validate")){
				$("#addStaffForm").submit();
			}
			
		});
		//页面加载完成
		$("#query").click(function(){
			var jsondata = {"name":$("#qname").val(),"telephone":$("#qtelephone").val(),
					"station":$("#qstation").val(),"standard":$("#qstandard").combobox("getValue")};
			//  发送条件查询  
			$('#grid').datagrid('load',jsondata);
			
			$("#queryStaffWindow").window("close");
			
		});
		
		
	});

	function doDblClickRow(rowIndex,rowData){
		$("#addStaffWindow").window("open");
		$("#addStaffForm").form("load",rowData);
		// $("#qid").val(rowData.id);
	}
	
	//  自定义手机号校验器
	$.extend($.fn.validatebox.defaults.rules, { 
		//  telephone  代表一个校验器规则
		telephone: { 
		
				validator: function(value, param){ 
				  // value  用户输入数据  param传递的数据
				   var reg = /^1[3|4|5|7|8]\d{9}$/;
				   return reg.test(value); 
				
				}, 
		
		    message: '手机号必须以1|3|4|5|7|8开头的11位数字' 
		
		} ,
		uniquePhone:{
	        validator: function (value) {  
	            var flag;
	            $.ajax({   //ajax的发送
                    url : '${pageContext.request.contextPath}/staffAction_validTelephone',  
                    type : 'POST',                    
                    timeout : 60000,  
                    data:{"telephone" : value,"id":$("#qid").val()},  
                    async: false,    //  同步 +easyui插件 
                    success : function(data, textStatus, jqXHR) {     
                        if (data) {  
                            flag = true;      
                        }else{  
                            flag = false;  
                        }  
                        $("#grid").datagrid("reload");
                    }  
                });  
	            // if(flag){  
	            	// 样式效果
                  //  $("#tel").removeClass('validatebox-invalid');  
               // }  
                return flag;  
	        },  
	        message: '手机号已经存在'  
	    }  

		
		});

	
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="addStaffForm" action="${pageContext.request.contextPath }/staffAction_save" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<tr>
						<td>姓名</td>
						<td>
						<input type="hidden" name="id" id="qid">
						<input type="text" name="name" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" class="easyui-validatebox" data-options="required:true,validType:['telephone','uniquePhone']"></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1"/>是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>  
							<input id="cc" class="easyui-combobox" name="standard"  
							data-options="editable:false,required:'true',valueField:'name',textField:'name',
							url:'${pageContext.request.contextPath }/standardAction_ajaxListName'" />
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	
	<!-- 查询条件窗体制作 -->
	<div class="easyui-window" title="条件查询" id="queryStaffWindow" modal="true" closed="true"
	collapsible="false" minimizable="false" maximizable="false" style="height:300px;width:500px;top:20px;left:200px">
		<div region="south" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="query" icon="icon-search" href="#" class="easyui-linkbutton" plain="true" >查询</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="queryStaffForm">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询取派员信息</td>
					</tr>
					<tr>
						<td>姓名</td>
						<td>
					
						<input type="text" name="name" id="qname"/></td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" id="qtelephone"></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" id="qstation"/></td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>  
							<input id="qstandard" class="easyui-combobox" name="standard"
							data-options="editable:false,valueField:'name',textField:'name',
							url:'${pageContext.request.contextPath }/standardAction_ajaxListName'" />
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
	
</body>
</html>	