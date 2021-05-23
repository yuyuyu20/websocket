package entity;

import javax.websocket.Session;

public class Msg {
private String name;
private String toName;
private Session session;
private int type;//0系统 1用户广播
private String msg;

public Msg(String name, String toName, Session session, int type, String msg) {
	super();
	this.name = name;
	this.toName = toName;
	this.session = session;
	this.type = type;
	this.msg = msg;
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
