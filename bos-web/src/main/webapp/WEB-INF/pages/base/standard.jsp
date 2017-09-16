<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>取派标准</title>
		<!-- 导入jquery核心类库 -->
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
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派标准信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [1,2,3],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/standardAction_pageQuery",
					idField : 'id',
					columns : columns,
					//  双击事件 编码说明
					onDblClickRow:function(rowIndex, rowData){
						// 双击事件添加  弹窗  将rowData回显div 表单form 里面
						$('#addStandardWindow').window("open");
						//  将当前记录 数据 回显到form表单里面  easyui  提供form 表单方法 自动将json格式数据加载form表单中
						// 注意: json格式字符串 key  必须和form表单 input  name 属性值相同
						$('#addStandardForm').form('load',rowData); 
					}
				});
				// 添加取派员窗口
// 				$('#addStaffWindow').window({
// 			        title: '取派员操作',
// 			        width: 600,
// 			        modal: true,
// 			        shadow: true,
// 			        closed: true,
// 			        height: 400,
// 			        resizable:false,
// 			        onBeforeClose:function(){
// 			        	//   清除form 表单数据 尤其  隐藏id 一定要清除  reset   jquery --->Dom
// 			        	  $("#addStaffForm")[0].reset();//  text  
// 			        	 $("#tel").removeClass('validatebox-invalid');  
// 			             $("#id").val("");  //  一定将隐藏id 值清除 // hidden
// 			        }
// 			    });
				
				// 页面加载完成函数
				$("#save").click(function(){
					//  表单提交 需要校验 表单参数是否合法 ! 使用easyui 校验器返回值 非法 false 合法 true
					var flag = $("#addStandardForm").form("validate");//  表单校验非法 false
					if(flag){
					  $("#addStandardForm").submit();
					  //  当前窗口关闭
					  $('#addStandardWindow').window("close");
					  
					}
				});
				
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					$('#addStandardWindow').window("open");
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					alert('修改');
				}
			},{
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : function(){
					// 1: 判断用户必须选中至少一行 
					var arr = $("#grid").datagrid("getSelections");//  获取选中行记录 集合
					if(arr==null||arr.length==0){
						$.messager.alert("警告!","至少选中一行","warning");
					}else{
						//  将所有选中行  作废  后台 获取选中行  id  批量修改  update xxx  set  deltag = 0 where id = ?
					   //  发送ajax请求 传递所有选中行Id  	  数组两个  push 数组添加    join 数组转换字符串 		
						   var ids= new Array();	
					       for(var i =0;i<arr.length;i++){
								ids.push(arr[i].id);
							}
					       //  字符串
					       var idsString = ids.join(",");
					       // ajax发送后台
					       $.post("${pageContext.request.contextPath}/standardAction_delBatch",{"ids":idsString},function(data){
					    	   if(data){
					    		   $.messager.alert("恭喜!","作废成功!","info");
					    		   //  页面及时更新最新的数据库数据   调用 datagrid  reload方法即可
					    		   $("#grid").datagrid("clearChecked");//  清除之前选择项的√
					    		   $("#grid").datagrid("reload");//  再次向url地址发送请求  重新分页查询
					    		   
					    	   }else{
					    		   $.messager.alert("可惜!","系统维护....","warning");
					    	   }
					       });
							
					}
				}
			},{
				id : 'button-restore',
				text : '还原',
				iconCls : 'icon-save',
				handler : function(){
					alert('还原');
				}
			}];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true
			},{
				field : 'name',
				title : '标准名称',
				width : 120,
				align : 'center'
			}, {
				field : 'minweight',
				title : '最小重量',
				width : 120,
				align : 'center'
			}, {
				field : 'maxweight',
				title : '最大重量',
				width : 120,
				align : 'center'
			}, {
				field : 'minlength',
				title : '最小长度',
				width : 120,
				align : 'center'
			}, {
				field : 'maxlength',
				title : '最大长度',
				width : 120,
				align : 'center'
			}, {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operationtime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'operatorcompany',
				title : '操作单位',
				width : 120,
				align : 'center'
			}, {
				field : 'deltag',
				title : '是否作废',
				width : 120,
				align : 'center',
				formatter: function(value,row,index){
					// value  当前列显示值  1  0   row  当前列json对象  
			         if (value==1){
			            return "已启用";
			         } else {
			            return "已作废";
			         }  			
			     }
			}  
			
			] ];
			//  关闭窗体 执行该函数  将表单数据清空 
			function clearformdata(){
				//  清空表单数据 尤其影藏域数据  form Dom对象  reset方法 能否清除隐藏域   手动清除隐藏域 val("")
				  //  方式一 : 传统方式        reset 
				         $("#addStandardForm")[0].reset();//   
			             $("#id").val("");  //  一定将隐藏id 值清除 // hidden
			             $("#deltag").val("");  //  一定将隐藏deltag 值清除 // hidden
			       // 方式二: easyui  提供form表单      
			          //   $("#addStandardForm").form("clear");  简化开发
				         return true;//  true 点击关闭 窗口关闭 
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		
	<!-- 添加收派标准的窗体  -->
	<div class="easyui-window" title="收派标准添加或者修改" id="addStandardWindow" modal="true" closed="true"
	collapsible="false" minimizable="false" maximizable="false" style="width:600px;height:400px;top:20px;left:200px"
	data-options="onBeforeClose:clearformdata"
	>
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >更新</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
		<form id="addStandardForm" method="post" 
		action="${pageContext.request.contextPath }/standardAction_save">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派标准信息</td>
					</tr>
					<tr>
						<td>收派名称</td>
						<td>
						<!-- 影藏域  判断后台save方法执行 是 update/save  -->
						<input type="hidden" name="id" id="id">
						<input type="hidden" name="deltag" id="deltag">
						<input type="text" name="name" class="easyui-validatebox" 
						data-options="required:true"/></td>
					</tr>
					<tr>
						<td>最小重量</td>
						<td>
						   <input id="minweight" type="text" name="minweight" class="easyui-numberbox"
						   data-options="min:1,precision:0,suffix:'KG',required:true"
						   />
						   </td>
					</tr>
					<tr>
						<td>最大重量</td>
						<td><input type="text" name="maxweight"
						class="easyui-numberbox"
						   data-options="max:500,precision:0,suffix:'KG',required:true"
						/></td>
					</tr>
					<tr>
						<td>最小长度</td>
						<td><input type="text" name="minlength"
						class="easyui-numberbox"
						   data-options="min:10,precision:0,suffix:'CM',required:true"
						/></td>
					</tr>
					<tr>
						<td>最大长度</td>
						<td><input type="text" name="maxlength"
						class="easyui-numberbox"
						   data-options="max:1000,precision:0,suffix:'CM',required:true"
						/></td>
					</tr>
					</table>
			</form>
		
		</div>
	</div>
		
		
	</body>

</html>