package entity;

import java.util.Arrays;

import javax.websocket.Session;

public class Msg {
private String name;
private String toName;
private Session session;
private int type;//0系统 1用户广播  2发送在线用户
private String msg;
private byte[] bts;

@Override
public String toString() {
	return "Msg [name=" + name + ", toName=" + toName + ", session=" + session + ", type=" + type + ", msg=" + msg
			+ ", bts=" + Arrays.toString(bts) + "]";
}
public Msg(String name, String toName, Session session, int type, String msg, byte[] bts) {
	super();
	this.name = name;
	this.toName = toName;
	this.session = session;
	this.type = type;
	this.msg = msg;
	this.bts = bts;
}
public byte[] getBts() {
	return bts;
}
public void setBts(byte[] bts) {
	this.bts = bts;
}
public String getMsg() {
	return msg;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getToName() {
	return toName;
}
public void setToName(String toName) {
	this.toName = toName;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public Session getSession() {
	return session;
}
public void setSession(Session session) {
	this.session = session;
}
public int getType() {
	return type;
}
public void setType(int type) {
	this.type = type;
}
public Msg() {
	super();
}


}
