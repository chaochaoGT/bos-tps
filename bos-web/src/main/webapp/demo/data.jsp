<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"></script>
   <script type="text/javascript">
      $(function(){
    	  
    	  $('#dg').datagrid({ 

    		  url:'data.json', //  json格式数据 [{},{},{}]
    		  pagination:true,  //  显示分页栏
    		  pageList:[5,10,15],//  每页显示记录数
    		  fit:true,//  填充父容器
    		  toolbar: [{  		  //  工具栏
    			  iconCls: 'icon-add',  	
    			  text:'添加',
    			  handler: function(){
    				  $.messager.show({  	
    					  title:'heima04',  	
    					  msg:"<a href='http://www.taobao.com'>淘宝热卖...</a>",  	
    					  timeout:10000,  	
    					  showType:'slide'  
    					});
    			  }  	
    			  },'-',{  		
    			  iconCls: 'icon-help',  		
    			  handler: function(){
    				  $.messager.alert('你好!','恭喜你注册成功!','warning'); 
    				  
    			  }  	
    			  }]  ,

    		  columns:[[ 

    		  {field:'code',title:'xxx',width:200}, 

    		  {field:'name',title:'商品名称',width:200}, 
    		  {field:'price',title:'商品价格',width:200}, 


    		  ]] 

    		  }); 

      })
   </script>
</head>
<body>
    <!-- 数据来自数据库的信息  页面 利用datagrid  组件 发送ajax请求  后台抓数据  填充 datagrid组件中 -->
    
    <table id="dg"></table>
</body>

</html>