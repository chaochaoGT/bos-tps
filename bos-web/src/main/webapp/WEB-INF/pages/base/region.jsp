<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>区域设置</title>
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
<script
	src="${pageContext.request.contextPath }/js/ocp/jquery.ocupload-min.js"
	type="text/javascript"></script>
<script type="text/javascript">
  var editRow;
  var flag= false;//  控制取消 保存 编辑 打开
  var cancelflag= false; // 控制删除
	function doAdd(){
		$('#addRegionWindow').window("open");
	}
	
	function doExport(){
		//  导出 区域数据 pdf格式 
		location.href="${pageContext.request.contextPath}/regionAction_export"
	}
	
	function doExport1(){
		//  导出 区域数据 pdf格式 
		location.href="${pageContext.request.contextPath}/regionAction_export1"
	}
	
	function doView(){
		alert("修改...");
	}
	
	function doSave(){
		// 关闭编辑  提交数据
		$("#grid").datagrid("endEdit",editRow);
		 flag = false;//  不可以打开其他行
	}
	
	function doDelete(){
		alert("删除...");
	}
	function doAddLine(){
		// 添加行...
		if(!flag){
			// false 可以添加一行
		$('#grid').datagrid('insertRow',{
		  	index: 0,	// index start with 0
		  	row: {}
		  });  
		$("#grid").datagrid("beginEdit",0);
			flag = true;// 打开第一行编辑 flag = true 不可以打开其他行
		}
		editRow = 0;
		cancelflag = true;// 点击取消可以删除当前行
	}
	
	function doCancel(){
		    $("#grid").datagrid("cancelEdit",editRow);
		  flag = false;
		if(cancelflag){
		  // 删除行 
		    $("#grid").datagrid("deleteRow",editRow);
		  cancelflag= false;	
		}

	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doView
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	}, {
		id : 'button-import',
		text : '导入',
		iconCls : 'icon-redo'
	}, {
		id : 'button-export',
		text : 'Itext导出',
		iconCls : 'icon-redo',
		handler : doExport
	}, {
		id : 'button-export',
		text : 'Jasper导出',
		iconCls : 'icon-redo',
		handler : doExport1
	}
	, {
		id : 'button-import',
		text : '保存',
		iconCls : 'icon-save',
		handler : doSave
	}, {
		id : 'button-import',
		text : '取消',
		iconCls : 'icon-cancel',
		handler:doCancel
	}, {
		id : 'button-addLine',
		text : '添加一行',
		iconCls : 'icon-add',
		handler:doAddLine
	}
	];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'province',
		title : '省',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'city',
		title : '市',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'district',
		title : '区',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'postcode',
		title : '邮编',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'shortcode',
		title : '简码',
		width : 120,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	}, {
		field : 'citycode',
		title : '城市编码',
		width : 200,
		align : 'center',
		editor:{
			type:'validatebox',
			options:{
				required:true
			}
		}
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			pageList: [5,10,15],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/regionAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,
			onAfterEdit:function(rowIndex, rowData, changes){
				//  事件在 endEdit方法执行之后
// 				$.messager.alert("iii",rowData.city,"info");
				  $.post("",rowData,function(data){
					  //  全字段的修改...{region:'甘肃省'}--->  regionAction_saveOrupdate--->dao save方法 全字段的更新
					  if(data){
						  $.messager.alert("","","");
					  }
					  
				  })
			}
		});
		
		// 添加、修改区域窗口
		$('#addRegionWindow').window({
	        title: '添加修改区域',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 添加导入 一键上传功能
		$("#button-import").upload({
			   name: 'upload',
		        action: '${pageContext.request.contextPath}/regionAction_oneclickupload',
		        enctype: 'multipart/form-data',
		        autoSubmit: false,//  表单不要一选中文件就立刻提交action
		        onComplete: function(data) {
		        	//  服务器成功 页面给予用户提示...  true false 字符类型 不是Bool true/ false
		        	if(eval("("+data+")")){
		        		$.messager.alert("恭喜!","导入成功!","info");
		        	}else{
		        		$.messager.alert("遗憾!","服务器正在更新.....","error");
		        	}
		        },
		        onSelect: function() {
		        	//  1:  选中文件立刻会执行该函数
		        	//  this.filename()  获取文件名  excel  
		        	var reg = /^(.+\.xls|.+\.xlsx)$/
		        	if(reg.test(this.filename())){
		        		//  提交表单
		        		this.submit();
		        	}else{
		        		// 不提交表单 给予用户提示
		        		$.messager.alert("警告!","请选择excel文件","warning");
		        	}
		        	
		        }
		});
		
	});

	function doDblClickRow(rowIndex,rowData){
		  // 双击打开编辑 ...  添加列属性  editable
		  if(!flag){
			  editRow = rowIndex;
			  $("#grid").datagrid("beginEdit",rowIndex);
			  flag = true;
			  cancelflag= false;	
		  }
		
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="区域添加修改" id="addRegionWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form>
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">区域信息</td>
					</tr>
					<tr>
						<td>省</td>
						<td><input type="text" name="province" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>市</td>
						<td><input type="text" name="city" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>区</td>
						<td><input type="text" name="district" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>邮编</td>
						<td><input type="text" name="postcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>简码</td>
						<td><input type="text" name="shortcode" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>城市编码</td>
						<td><input type="text" name="citycode" class="easyui-validatebox" required="true"/></td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>