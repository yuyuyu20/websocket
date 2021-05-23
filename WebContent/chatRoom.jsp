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
 <meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>网络聊天室</title>
<script type="text/javascript" src="js/jquery-3.2.1.js"></script>
<script type="text/javascript" src="js/wangEditor.min.js"></script><!-- 富文本导包 -->
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<style type="text/css">
    #contains{
        background-color: pink;
        width: 1000px;
        height: 700px;
        margin: auto;
    }
    #username{
        background-color: powderblue;
        width: 1000px;
        height: 30px;
    }
    #Inchat{
        background-color: rgb(5, 130, 146);
        width: 1000px;
        height: 30px;
    }
    #left{
        background-color: salmon;
        width: 700px;
        height: 640px;
        float: left;
        position: relative;
    }
    #content{
        background-color: silver;
        width: 700px;
        height: 400px;
        /*display: none;*/
    }
    #right{
        background-color: rgb(107, 3, 3);
        width: 300px;
        height: 640px;
        float: right;
    }
    #hyList{
        height: 270px;
        overflow-y: scroll;
        background: antiquewhite;
    }
    #xtList{
        height: 270px;
        overflow-y: scroll;
        background: antiquewhite;
    }
    #input{
        margin-bottom: 0px;
        position: absolute;
        bottom: 0px;
    }
    #mes_left{
        float: left;
        border: 2px aqua;
        max-width: 490px;
    }
    #mes_right{
        float: right;
        border: 2px aqua;
        max-width: 490px;
    }
    #hy{
        display: block;
        width: 300px;
    }
    textarea {
        resize: none;
        border: none;
        outline: none
    }
    
</style>
</head>
  
  <body>
<div id = "contains">
    <div id="username"><h3 style="text-align: center;">用户：${user.name } <span>-在线</span></h3></div>
    <div id="Inchat"></div>
    <div id="left">
        <div id="content" >
        
        </div>
        <div id="input">
            <textarea type="text" id="input_text" style="width: 695px;height: 200px;"></textarea>
            <button id="submit" style="float: right;">发送</button>
            <input type="file" id="img" style="width:200px;	 heigh:30px";/>
            <button id="sendimg" style="float: left;" onclick="sendImg()">发送图片</button>
            <button id="quxiao" style="float: left;" onclick="quxiao()">取消私聊</button>
        </div>
   </div>
    <div id="right">
        <p id="hy" style="text-align: center;">好友列表</p>
        <div>
        <ul id="hyList"></ul>
        </div>
        <p id="xt" style="text-align: center">系统消息</p>
        <div id="xtList">

        </div>
    </div>
</div>
<input type="hidden" id="siliao" value=""/>


<script type="text/javascript">
			var input = document.getElementById("input_text")//用户输入框
			var button = document.getElementById("submit")//按钮
			var div = document.getElementById("content")//显示厅
			var friend = document.getElementById("hyList")//好友在线列表
			var xtlist = document.getElementById("xtList")//系统消息
			
			//参数1 websocket的服务地址	创建websocket
			//var host = window.location.host;  获取本地localhost
			var socket = new WebSocket('ws://192.168.10.104:8080/websocket/websocket')//设置本地的localhost
			
			socket.addEventListener('open',function(){//开启时的消息
			var obj = new Object();
			var d = new Date();//当前时间
			obj.name="${user.name}"
			obj.type=0;
			obj.msg=d.toLocaleString( ) +"</br>${user.name}用户上线了"
			var str = JSON.stringify(obj);
			socket.send(str);
			div.innerHTML = "连接服务成功</br>使用方式：点击右边好友列表选择私聊用户，不选中则是发送全体。好友列表不会显示自己。"
				+"</br>自己发的消息会显示在右侧。私聊原理：发送消息时把siliao隐藏域内的值一起发送出去，点击好友名字时给隐藏域赋值该名字</br>"
			})
			
			button.addEventListener("click",function(){
				var obj = new Object();
				var value = input.value//获取输入框内容
				obj.msg = value//发的信息
				obj.type=1 //类型
				obj.name="${user.name}"//发送人
				obj.toName=document.getElementById("siliao").value
				var msg = JSON.stringify(obj)//转JSON格式
				input.value=""
				socket.send(msg)
			})
			
			socket.addEventListener('message',function(e){
				if("string"==typeof(e.data)){
					var msg = JSON.parse(e.data)
					if(msg.type==0){//系统通知
						xtlist.innerHTML+=msg.msg+"</br>";
					} else if (msg.type==1){//用户发送聊天的type
						if(msg.toName==""){//如果msg的给用户发送的字段为空则是广播群聊
							if(msg.name=="${user.name}"){
								var d = new Date();//当前时间
								var str ="<span id='mes_right'>用户"+ msg.name+"在"+d.toLocaleString()+"对所有人说：</span></br>"//mes_right 显示在右边
								div.innerHTML+=str+"<span id='mes_right'>"+ msg.msg +"</span></br>";
							} else {
								var d = new Date();//当前时间
								var str ="用户"+ msg.name+"在"+d.toLocaleString()+"对所有人说：</br>"
								div.innerHTML+=str+msg.msg+"</br>";
							}
						} else {//不为空就是有私聊的对象
							if(msg.name=="${user.name}"){//如果是自己
								var d = new Date();//当前时间
								var str ="<span id='mes_right'>用户"+ msg.name+"在"+d.toLocaleString()+"对"+msg.toName+"说：</span></br>"
								div.innerHTML+=str+"<span id='mes_right'>"+ msg.msg +"</span></br>";
							} else {//不是自己
								var d = new Date();//当前时间
								var str ="用户"+ msg.name+"在"+d.toLocaleString()+"对你说：</br>"
								div.innerHTML+=str+msg.msg+"</br>";
							}
						}
						
					} else if(msg.type==2){//发送在线人数的type
						var str = ""
						var list = JSON.parse(msg.msg)
						for(var i in list){
							if(list[i]!="${user.name}"){
							str+="<li><a href='javascript:void(0);' onclick=myname(this)>"+list[i]+"</a></li>"
							}
						}
						friend.innerHTML = str
					}
				} else {
					var reader = new FileReader();//new一个js里的对象  读成字节流
					reader.onload = function loaded(evt){
						if(evt.target.readyState == reader.DONE){
							var url = evt.target.result;
							div.innerHTML+="<img style='max-height:150px;max-width=120px;vertical-align:middle' src='"+url+"'></img></br>" //vertical-align:middle居中显示
						}
					}
					reader.readAsDataURL(e.data);//把对象读成二进制格式
				}
				
				
				
				
				//if(e.data.startsWith("服务器")){
				//	var str = e.data.substring(9);
				//	var str = str.split("进入聊天室");
				//	var str = str[0];
				//	console.log(str)
				//	friend.innerHTML+="<li><a href='javascript:void(0);' onclick=myname(this)>"+str+"</a></li>"
				//	console.log(e.data)
				//	div.innerHTML=e.data
				//} else if (e.data.startsWith("@2222")){
				//	var str = e.data.substring(5);
				//	div.innerHTML+=str;
				//}else {
				
				//console.log(e.data)
				//div.innerHTML+=e.data+"</br>"
			//}
			})
			
			socket.addEventListener('close',function(){
				div.innerHTML = "服务断开连接"
			})
			
			
			function myname(name){
				document.getElementById("siliao").value=name.innerHTML;
				var obj = new Object();
				obj.name="${user.name}"
				obj.toName=name.innerHTML
				obj.type=0;
				obj.msg=obj.name+"用户，您正在和"+name.innerHTML+"用户私聊"
				var str = JSON.stringify(obj);
				socket.send(str);
  			}
			function quxiao(){
				document.getElementById("siliao").value=""
			}
			function sendImg(){
				var imgCont = $("#img")[0].files[0]//js对象变成jq对象  全选文件
				if(imgCont){
						var reader = new FileReader();//new一个js里的对象  读成字节流
						reader.readAsArrayBuffer(imgCont);//把对象读成二进制格式
						reader.onload = function loaded(evt){
							socket.send(evt.target.result)//读出来的二进制流发送到后台   
						}
						$("#img").val("");
				}else {
					$("#img").val("");
					return;
				}
			}
			
		</script>

  
  </body>

<script src="https://eqcn.ajz.miesnfu.com/wp-content/plugins/wp-3d-pony/live2dw/lib/L2Dwidget.min.js"></script>
<!--小帅哥： https://unpkg.com/live2d-widget-model-chitose@1.0.5/assets/chitose.model.json-->
<!--萌娘：https://unpkg.com/live2d-widget-model-shizuku@1.0.5/assets/shizuku.model.json-->
<!--小可爱（女）：https://unpkg.com/live2d-widget-model-koharu@1.0.5/assets/koharu.model.json-->
<!--小可爱（男）：https://unpkg.com/live2d-widget-model-haruto@1.0.5/assets/haruto.model.json-->
<!--初音：https://unpkg.com/live2d-widget-model-miku@1.0.5/assets/miku.model.json-->
<!-- 上边的不同链接显示的是不同的小人，这个可以根据需要来选择 下边的初始化部分，可以修改宽高来修改小人的大小，或者是鼠标移动到小人上的透明度，也可以修改小人在页面出现的位置。 -->
<script>
    /*https://unpkg.com/live2d-widget-model-shizuku@1.0.5/assets/shizuku.model.json*/
    L2Dwidget.init({ "model": { jsonPath:
                "https://unpkg.com/live2d-widget-model-shizuku@1.0.5/assets/shizuku.model.json",
            "scale": 1 }, "display": { "position": "right", "width": 200, "height": 340,
            "hOffset": 0, "vOffset": -20 }, "mobile": { "show": true, "scale": 0.5 },
        "react": { "opacityDefault": 0.8, "opacityOnHover": 0.1 } });
</script>
  
</html>

<!-- 富文本 -->
<script type="text/javascript">
	const E = window.wangEditor
	const editor = new E('#input_text')
	//或者 const editor = new E( document.getElementById('div1') )
	editor.create()
</script>
