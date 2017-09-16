<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理定区/调度排班</title>
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
		$('#addDecidedzoneWindow').window("open");
	}
	
	function doEdit(){
		alert("修改...");
	}
	
	function doDelete(){
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
		       $.post("${pageContext.request.contextPath}/decidedZoneAction_delBatch",{"ids":idsString},function(data){
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
	
	function doSearch(){
		$('#searchWindow').window("open");
	}
	
	function doAssociations(){
		// 1: 首先必须选中一个定区 才可以弹窗
		var arr = $("#grid").datagrid("getSelections");
			if(arr==null||arr.length==0){
				$.messager.alert("警告!","至少选中一行","warning");
				return ;
			}else{
		       //  立刻发送ajax请求  2次
				//  立刻发送两次ajax 请求   noassociationSelect   associationSelect
				$('#customerWindow').window('open');
		       // remove  empty
		         $("#noassociationSelect").empty();
		         $("#associationSelect").empty();
					$.post("${pageContext.request.contextPath}/decidedZoneAction_findnoassociationcustomers",function(data){
						//  data  List<Customer>  左边下拉框  [{},{},{}]
						  $(data).each(function(){
							  $("#noassociationSelect").append("<option value='"+this.id+"'>"+this.name+"</option>");
						  });
					});
					
					$.post("${pageContext.request.contextPath}/decidedZoneAction_findassociationcustomers",{"id":arr[0].id},function(data){
						//  data  List<Customer>  右边
						  $(data).each(function(){
							  $("#associationSelect").append("<option value='"+this.id+"'>"+this.name+"</option>");
						  });
					});
				
			}
	}
	
	//工具栏
	var toolbar = [ {
		id : 'button-search',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doSearch
	}, {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	}, {
		id : 'button-edit',	
		text : '修改',
		iconCls : 'icon-edit',
		handler : doEdit
	},{
		id : 'button-delete',
		text : '删除',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-association',
		text : '关联客户',
		iconCls : 'icon-sum',
		handler : doAssociations
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox:true,
		title : '定区编号',
		width : 120,
		align : 'center'
	},{
		field : 'name',
		title : '定区名称',
		width : 120,
		align : 'center'
	}, {
		field : 'staff.name',
		title : '负责人',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.name;
		}
	}, {
		field : 'staff.telephone',
		title : '联系电话',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.telephone;
		}
	}, {
		field : 'staff.station',
		title : '所属公司',
		width : 120,
		align : 'center',
		formatter : function(data,row ,index){
			return row.staff.station;
		}
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 收派标准数据表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			pageList: [3,5,10],
			pagination : true,
			toolbar : toolbar,
			url : "${pageContext.request.contextPath}/decidedZoneAction_pageQuery",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow,
			singleSelect:true
		});
		
		// 添加、修改定区
		$('#addDecidedzoneWindow').window({
	        title: '添加修改定区',
	        width: 600,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		// 查询定区
		$('#searchWindow').window({
	        title: '查询定区',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		$("#save").click(function(){
			if($("#addDecidedZoneForm").form("validate")){
				$("#addDecidedZoneForm").submit();
				$('#addDecidedzoneWindow').window("close");
			}
		});
		$("#btn").click(function(){
			alert("执行查询...");
		});
		
		//  页面加载完成 
		$("#toRight").click(function(){
			// 左--->右
			$("#associationSelect").append($("#noassociationSelect option:selected"));
			
		});
		$("#toLeft").click(function(){
			//  右边--->左边
			$("#noassociationSelect").append($("#associationSelect option:selected"));
		});
		
		$("#associationBtn").click(function(){
			//  提交表单 将右边客户id和当前选中定区id 随着表单一起提交action  --->service-->WebClient --->crm
			var arr = $("#grid").datagrid("getSelections");//  定区id 
			$("#customerDecidedZoneId").val(arr[0].id);// 表单提交 定区Id提交
			// 右边select里面所有option  添加属性 selected=selected
			$("#associationSelect option").attr("selected","selected");
			
			// 3: 提交表单 关闭窗口
			   $("#customerForm").submit();
			   
			   $('#searchWindow').window("close");


		});
		
		
	});

	function doDblClickRow(){
		alert("双击表格数据...");
		$('#association_subarea').datagrid( {
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			url : "json/association_subarea.json",
			columns : [ [{
				field : 'id',
				title : '分拣编号',
				width : 120,
				align : 'center'
			},{
				field : 'province',
				title : '省',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.province;
				}
			}, {
				field : 'city',
				title : '市',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.city;
				}
			}, {
				field : 'district',
				title : '区',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.region.district;
				}
			}, {
				field : 'addresskey',
				title : '关键字',
				width : 120,
				align : 'center'
			}, {
				field : 'startnum',
				title : '起始号',
				width : 100,
				align : 'center'
			}, {
				field : 'endnum',
				title : '终止号',
				width : 100,
				align : 'center'
			} , {
				field : 'single',
				title : '单双号',
				width : 100,
				align : 'center'
			} , {
				field : 'position',
				title : '位置',
				width : 200,
				align : 'center'
			} ] ]
		});
		$('#association_customer').datagrid( {
			fit : true,
			border : true,
			rownumbers : true,
			striped : true,
			url : "json/association_customer.json",
			columns : [[{
				field : 'id',
				title : '客户编号',
				width : 120,
				align : 'center'
			},{
				field : 'name',
				title : '客户名称',
				width : 120,
				align : 'center'
			}, {
				field : 'station',
				title : '所属单位',
				width : 120,
				align : 'center'
			}]]
		});
		
	}
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">
	<div region="center" border="false">
    	<table id="grid"></table>
	</div>
	<div region="south" border="false" style="height:150px">
		<div id="tabs" fit="true" class="easyui-tabs">
			<div title="关联分区" id="subArea"
				style="width:100%;height:100%;overflow:hidden">
				<table id="association_subarea"></table>
			</div>	
			<div title="关联客户" id="customers"
				style="width:100%;height:100%;overflow:hidden">
				<table id="association_customer"></table>
			</div>	
		</div>
	</div>
	
	<!-- 添加 修改分区 -->
	<div class="easyui-window" title="定区添加修改" id="addDecidedzoneWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="addDecidedZoneForm" action="${pageContext.request.contextPath }/decidedZoneAction_save" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">定区信息</td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>定区名称</td>
						<td><input type="text" name="name" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>选择负责人</td>
						<td>
							<input class="easyui-combobox" name="staff.id"  
    							data-options="valueField:'id',textField:'name',
    							url:'${pageContext.request.contextPath }/staffAction_ajaxListInUse'" />  
						</td>
					</tr>
					<tr height="300">
						<td valign="top">关联分区</td>
						<td>
							<table id="subareaGrid"  class="easyui-datagrid" 
							border="false" style="width:300px;height:300px" 
							data-options="url:'${pageContext.request.contextPath }/subareaAction_noassociation',fitColumns:true,singleSelect:false">
								<thead>  
							        <tr>  
							            <th data-options="field:'sid',width:30,checkbox:true">编号</th>  
							            <th data-options="field:'addresskey',width:150">关键字</th>  
							            <th data-options="field:'position',width:200,align:'right'">位置</th>  
							        </tr>  
							    </thead> 
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- 查询定区 -->
	<div class="easyui-window" title="查询定区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div style="overflow:auto;padding:5px;" border="false">
			<form>
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">查询条件</td>
					</tr>
					<tr>
						<td>定区编码</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>所属单位</td>
						<td><input type="text" name="staff.station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td>是否关联分区</td>
						<td><input type="text" name="subareaName" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<!-- 关联客户窗口 -->
	<div class="easyui-window" title="关联客户窗口" id="customerWindow" collapsible="false" closed="true" minimizable="false" maximizable="false" style="top:20px;left:200px;width: 400px;height: 300px;">
		<div style="overflow:auto;padding:5px;" border="false">
			<form id="customerForm" 
			action="${pageContext.request.contextPath }/decidedZoneAction_assigncustomerstodecidedzone" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="3">关联客户</td>
					</tr>
					<tr>
						<td>
						   <!-- 表单提交定区id提交后台 -->
							<input type="hidden" name="id" id="customerDecidedZoneId" />
							<select id="noassociationSelect" multiple="multiple" size="10"></select>
						</td>
						<td>
							<input type="button" value="》》" id="toRight"><br/>
							<input type="button" value="《《" id="toLeft">
						</td>
						<td>
						   <!-- name action 只能获取选中的option数据  -->
							<select id="associationSelect" name="customerIds" multiple="multiple" size="10"></select>
						</td>
					</tr>
					<tr>
						<td colspan="3"><a id="associationBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">关联客户</a> </td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>