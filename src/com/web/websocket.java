package com.web;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.Gson;

import entity.Msg;


@ServerEndpoint("/websocket")
public class websocket {//服务器只要写了注解，就会自动匹配对应的信息执行方法，可以有多个OnMessage注解
	//map用处：登录进来后前端发送：XX用户已上线。接受消息后转发并将用户的key：name 和value：session存入map中
	private static Map<String, Session> map = new HashMap<String, Session>();
	//set：一上线就存入session中
    private static Set<Session> sessions = new HashSet<Session>();
    private byte[] b;//传图片用的
    Gson gson = new Gson();
    
    @OnOpen
    public void open(Session session){//当客户端建立连接时
    	sessions.add(session);
    }
    
    public void sendOnline() {//发送在线人数的方法
    	List<String> list =new ArrayList<String>();//创建一个获取在线人数名字的集合
    	Set<String> set = map.keySet();
    	for(String str:set) {
    		list.add(str);
    		System.out.println(str);
    	}
    	String str = gson.toJson(list);//转成json格式
    	Msg m = new Msg();
    	m.setType(2);
    	m.setMsg(str);//当成消息发送出去
    	String msg = gson.toJson(m);
    	broadcast(sessions, msg);//发送在线人数
    }
    
    public void broadcast(Set<Session> sessions,String msg){//广播、发送全部
        for(Iterator<Session> iter = sessions.iterator();iter.hasNext();){
            Session session = (Session) iter.next();
            try {
            		session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void siliao(Session session,String msg) {//私聊调用的方法
    	try {
			session.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @OnMessage
    public void onMessage(Session session,String msg,boolean last){//当收到消息时
    	//1.0升级版	2.0版本
    	Msg m = gson.fromJson(msg, Msg.class);
    	String str = gson.toJson(m);
    	if(m.getType()==0) {
    		websocket.map.put(m.getName(), session);
    		sendOnline();
    		broadcast(websocket.sessions,str);//广播
    	} else if(m.getType()==1) {// 用户发送消息
    		if(m.getToName().equals("")) {//如果toname字段为空就发送全部人消息
    			broadcast(websocket.sessions,str);//广播
    		} else {
    			Session s = map.get(m.getToName());//如果toname有字段就从map中获取session
    			siliao(s, str);//私聊的对象
    			siliao(session, str);//自己页面显示
    		}
    	}
    	//	1.0版本
//    	String smsg = msg;
//    	System.out.println("接收的消息是："+msg);
//    	if(smsg.startsWith("#1111")) {//系统广播
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//    		String[] name = message[1].split("进入聊天室");
//    		map.put(name[0], session);
//            broadcast(websocket.sessions,message[0]+"说："+message[1]);//广播
//    	} else if (msg.startsWith("#2222")) {//点击名字私聊
//    		smsg = smsg.substring(5);
//            broadcast(websocket.sessions,"@2222"+smsg);//广播
//    	} else if(msg.startsWith("#3333")) {
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//            broadcast(websocket.sessions,message[0]+"说："+message[1]);//广播
//    	} else if (msg.startsWith("#4444")) {
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//    		Session ses = map.get(message[0]);
//    		siliao(ses,message[0]+"与您私聊："+message[1] );
//    	} else {
//    		broadcast(websocket.sessions,smsg);//广播
//    	}
    	
    
    }
    
    @OnMessage
    public void onMessage(Session session,byte[] inputStream,boolean last){
    	//读取图片1.0版本   2.0版本：同时存入msg对象中 使用下面方法转换并发送
    	//读取流格式的方法，last为判断最后是否最后一次   last为后端websocket写的，不是我们传的。
    	
    	if(!last) {//每次传输的自接数组最大为8192
    		b = ArrayUtils.addAll(b, inputStream); //commons-lang3-3.1 jar包   用于将两个数组合并成一个
    	} else {//最后一次传输完后last变为true并且再执行一次，否则会有最后一次没传到
    		b = ArrayUtils.addAll(b, inputStream);//最后一次
    		ByteBuffer bf = ByteBuffer.wrap(b);
    		broadcast(sessions, bf);
    		b = null;
    	}
    	
    }
    
    public void broadcast(Set<Session> sessions,ByteBuffer msg){//广播、发送全部   字节缓冲流
        for(Iterator<Session> iter = sessions.iterator();iter.hasNext();){
            Session session = (Session) iter.next();
            try {
            		session.getBasicRemote().sendBinary(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    @OnClose
    public void onClose(Session session){//注解：关闭的方法
    	sessions.remove(session);
    	String name = "";
    	Set<String> set = map.keySet();
    	for(String str:set) {
    		if(session.equals(map.get(str))){
    			name = str;
    			map.remove(str);
    		}
    	}
    	sendOnline();
    	Date date = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //格式化为日期/时间字符串
        String cc=sdf.format(date);
    	Msg m = new Msg();
    	m.setType(0);
    	m.setMsg(cc+"</br>"+name+"用户下线了");
    	String msg = gson.toJson(m);
    	broadcast(sessions, msg);
        System.out.println("连接关闭了。。。");
    }
    
}