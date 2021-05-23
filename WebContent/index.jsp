<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>模板</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--> 

  </head>
  
  <body>
 		<div>
 		<form action="Login" method="post">
 		<table>
 		<tr>
 		<td>账号</td>
 		<td><input type="text" id="user_acc" name="user_acc" placeholder="输入账号" /></td>
 		</tr>
 		<tr>
 		<td>密码</td>
 		<td><input type="password" id="user_pwd" name="user_pwd" placeholder="输入密码" /></td>
 		</tr>
 		<tr>
 		<td>用户名</td>
 		<td><input type="text" id="user_name" name="user_name" placeholder="输入用户名" /></td>
 		</tr>
 		<tr>
 		</table>
 		<input type="submit" value="提交"/>
 		</form>
 	
 		</div>
  
  </body>
</html>
