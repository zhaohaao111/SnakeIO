package com.hao.snake.client;

import java.io.PrintWriter;

import com.alibaba.fastjson.JSONObject;
import com.hao.snake.Node;
import com.hao.snake.Skin;
import com.hao.snake.Snake;
import com.hao.snake.common.MsgType;
import com.hao.snake.listener.ClientListener;


public class ClientListenerHander implements ClientListener {
	
	JSONObject result = new JSONObject();

	@Override
	public void playJoin(Skin skin, PrintWriter out) {
		synchronized (result) {
			result.put("type", MsgType.INIT);
			result.put("skinIndex", skin.getIndex());
	    	out.println(result.toJSONString());
	    	out.flush();
	    	result.clear();
		}
		
	}

	@Override
	public void eatFood(int index, PrintWriter out) {
		synchronized (result) {
			result.put("type", MsgType.EAT_FOOD);
			result.put("index", index);
	    	out.println(result.toJSONString());
	    	out.flush();
	    	result.clear();
		}
		
	}

	@Override
	public void beforePaint(PrintWriter out) {
		synchronized (result) {
			result.put("type", MsgType.GET_SNAKE);
			out.println(result.toJSONString());
			out.flush();
			result.clear();
		}
	}

	@Override
	public void afterPaint(PrintWriter out, Snake snake) {
		synchronized (result) {
			result.put("type", MsgType.UPDATE_SNAKE);
			Node head = snake.getBody().getFirst();
			result.put("head", JSONObject.toJSONString(head));
			out.println(result.toJSONString());
			out.flush();
			result.clear();
		}
	}

	
}
