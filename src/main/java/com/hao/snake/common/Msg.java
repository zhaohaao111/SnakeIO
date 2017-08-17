package com.hao.snake.common;


public class Msg {
	private String json;
	private MsgType type;
	
	
	
	public Msg(String json, MsgType type) {
		super();
		this.json = json;
		this.type = type;
	}


	
	public String getJson() {
		return json;
	}


	
	public void setJson(String json) {
		this.json = json;
	}


	
	public MsgType getType() {
		return type;
	}


	
	public void setType(MsgType type) {
		this.type = type;
	}
	
	
}
