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
public class websocket {//������ֻҪд��ע�⣬�ͻ��Զ�ƥ���Ӧ����Ϣִ�з����������ж��OnMessageע��
	//map�ô�����¼������ǰ�˷��ͣ�XX�û������ߡ�������Ϣ��ת�������û���key��name ��value��session����map��
	private static Map<String, Session> map = new HashMap<String, Session>();
	//set��һ���߾ʹ���session��
    private static Set<Session> sessions = new HashSet<Session>();
    private byte[] b;//��ͼƬ�õ�
    Gson gson = new Gson();
    
    @OnOpen
    public void open(Session session){//���ͻ��˽�������ʱ
    	sessions.add(session);
    }
    
    public void sendOnline() {//�������������ķ���
    	List<String> list =new ArrayList<String>();//����һ����ȡ�����������ֵļ���
    	Set<String> set = map.keySet();
    	for(String str:set) {
    		list.add(str);
    		System.out.println(str);
    	}
    	String str = gson.toJson(list);//ת��json��ʽ
    	Msg m = new Msg();
    	m.setType(2);
    	m.setMsg(str);//������Ϣ���ͳ�ȥ
    	String msg = gson.toJson(m);
    	broadcast(sessions, msg);//������������
    }
    
    public void broadcast(Set<Session> sessions,String msg){//�㲥������ȫ��
        for(Iterator<Session> iter = sessions.iterator();iter.hasNext();){
            Session session = (Session) iter.next();
            try {
            		session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void siliao(Session session,String msg) {//˽�ĵ��õķ���
    	try {
			session.getBasicRemote().sendText(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @OnMessage
    public void onMessage(Session session,String msg,boolean last){//���յ���Ϣʱ
    	//1.0������	2.0�汾
    	Msg m = gson.fromJson(msg, Msg.class);
    	String str = gson.toJson(m);
    	if(m.getType()==0) {
    		websocket.map.put(m.getName(), session);
    		sendOnline();
    		broadcast(websocket.sessions,str);//�㲥
    	} else if(m.getType()==1) {// �û�������Ϣ
    		if(m.getToName().equals("")) {//���toname�ֶ�Ϊ�վͷ���ȫ������Ϣ
    			broadcast(websocket.sessions,str);//�㲥
    		} else {
    			Session s = map.get(m.getToName());//���toname���ֶξʹ�map�л�ȡsession
    			siliao(s, str);//˽�ĵĶ���
    			siliao(session, str);//�Լ�ҳ����ʾ
    		}
    	}
    	//	1.0�汾
//    	String smsg = msg;
//    	System.out.println("���յ���Ϣ�ǣ�"+msg);
//    	if(smsg.startsWith("#1111")) {//ϵͳ�㲥
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//    		String[] name = message[1].split("����������");
//    		map.put(name[0], session);
//            broadcast(websocket.sessions,message[0]+"˵��"+message[1]);//�㲥
//    	} else if (msg.startsWith("#2222")) {//�������˽��
//    		smsg = smsg.substring(5);
//            broadcast(websocket.sessions,"@2222"+smsg);//�㲥
//    	} else if(msg.startsWith("#3333")) {
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//            broadcast(websocket.sessions,message[0]+"˵��"+message[1]);//�㲥
//    	} else if (msg.startsWith("#4444")) {
//    		smsg = smsg.substring(5);
//    		String[] message = smsg.split("---///");
//    		Session ses = map.get(message[0]);
//    		siliao(ses,message[0]+"����˽�ģ�"+message[1] );
//    	} else {
//    		broadcast(websocket.sessions,smsg);//�㲥
//    	}
    	
    
    }
    
    @OnMessage
    public void onMessage(Session session,byte[] inputStream,boolean last){
    	//��ȡͼƬ1.0�汾   2.0�汾��ͬʱ����msg������ ʹ�����淽��ת��������
    	//��ȡ����ʽ�ķ�����lastΪ�ж�����Ƿ����һ��   lastΪ���websocketд�ģ��������Ǵ��ġ�
    	
    	if(!last) {//ÿ�δ�����Խ��������Ϊ8192
    		b = ArrayUtils.addAll(b, inputStream); //commons-lang3-3.1 jar��   ���ڽ���������ϲ���һ��
    	} else {//���һ�δ������last��Ϊtrue������ִ��һ�Σ�����������һ��û����
    		b = ArrayUtils.addAll(b, inputStream);//���һ��
    		ByteBuffer bf = ByteBuffer.wrap(b);
    		broadcast(sessions, bf);
    		b = null;
    	}
    	
    }
    
    public void broadcast(Set<Session> sessions,ByteBuffer msg){//�㲥������ȫ��   �ֽڻ�����
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
    public void onClose(Session session){//ע�⣺�رյķ���
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
        //��ʽ��Ϊ����/ʱ���ַ���
        String cc=sdf.format(date);
    	Msg m = new Msg();
    	m.setType(0);
    	m.setMsg(cc+"</br>"+name+"�û�������");
    	String msg = gson.toJson(m);
    	broadcast(sessions, msg);
        System.out.println("���ӹر��ˡ�����");
    }
    
}