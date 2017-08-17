package com.hao.snake.server;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.hao.snake.Food;
import com.hao.snake.FoodManager;
import com.hao.snake.Node;
import com.hao.snake.Skin;
import com.hao.snake.Snake;
import com.hao.snake.common.MsgType;
import com.hao.snake.listener.ServerListener;


public class ServerListenerHander implements ServerListener{
	

	@Override
	public void playJoin(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out, FoodManager foodManager, Skin skin, int port) {
		JSONObject result = new JSONObject();
		System.out.println(port);
		Snake snake = new Snake();
		snake.setSkin(skin);
		snakesMap.put(port, snake);
		ConcurrentHashMap<Integer, Snake> temp = new ConcurrentHashMap<Integer, Snake>();
		temp.putAll(snakesMap);
		String snakesStr = JSONObject.toJSONString(temp);
		result.put("type", MsgType.INIT );
		result.put("snakes", snakesStr);
		result.put("foodManager", foodManager);
		out.println(result.toJSONString());
		out.flush();
		result.clear();
		result = null;
		temp.clear();
		temp = null;
	}

	@Override
	public void getSnake(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out) {
		JSONObject result = new JSONObject();
		ConcurrentHashMap<Integer, Snake> temp = new ConcurrentHashMap<Integer, Snake>();
		temp.putAll(snakesMap);
		result.put("type", MsgType.GET_SNAKE);
		result.put("snakes", JSONObject.toJSONString(temp));
		out.println(result.toJSONString());
		out.flush();
		result.clear();
		result = null;
		temp.clear();
		temp = null;
	}

	@Override
	public void updateSnake(Node head, Snake snake) {
		Node snakeHead = snake.getBody().peekFirst();
		snakeHead.setAngle(head.getAngle());
		snake.move(3);
		snakeHead.copy(head);
		head = null;
	}

	@Override
	public void eatFood(int index, FoodManager foodManager, ConcurrentHashMap<Integer, Snake> snakesMap, int port) {
		try {
			Snake snake = snakesMap.get(port);
			List<Food> foods = foodManager.getFoods();
			Food food = foods.get(index);
			LinkedList<Node> body = snake.getBody();
			Node head = body.peekFirst();
			Node tail = body.peekLast();
			int foodSize = food.getSize()/2;
			synchronized (snakesMap) {
				while(foodSize>0){
					body.addLast(new Node(head.getX(),head.getY(),tail.getAngle()));
					foodSize--;
				}
			}
			foods.remove(index);
			foodManager.createFood(snake);
		}
		catch (Exception e) {
			System.out.println("服务端处理吃食物异常！");
		}
	}

	@Override
	public void updateFoods(ConcurrentHashMap<Integer, PrintWriter> outPutMap, FoodManager foodManager) {
		JSONObject result = new JSONObject();
		result = new JSONObject();
		result.put("type", MsgType.UPDATE_FOOD);
		result.put("foodManager", JSONObject.toJSONString(foodManager));
		for (Entry<Integer, PrintWriter> entry : outPutMap.entrySet()) {
			PrintWriter writer = entry.getValue();
			writer.println(result.toJSONString());
			writer.flush();
		}
		result.clear();
		result = null;
	}

	@Override
	public void playLogOut(int port, ConcurrentHashMap<Integer, Snake> snakesMap, ConcurrentHashMap<Integer, PrintWriter> outPutMap) {
		Snake snake = snakesMap.remove(port);
		snake = null;
		PrintWriter out = outPutMap.remove(port);
		out.close();
		out = null;
	}
	
}
