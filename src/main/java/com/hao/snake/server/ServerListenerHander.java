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
	
	private JSONObject result = new JSONObject();

	@Override
	public void playJoin(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out, FoodManager foodManager, Skin skin, int port) {
		System.out.println(port);
		Snake snake = new Snake();
		snake.setSkin(skin);
		snakesMap.put(port, snake);
		synchronized (snakesMap) {
			String snakesStr = JSONObject.toJSONString(snakesMap);
			result.put("type", MsgType.INIT );
			result.put("snakes", snakesStr);
			result.put("foodManager", foodManager);
			out.println(result.toJSONString());
		}
		out.flush();
		result.clear();
	}

	@Override
	public void getSnake(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out) {
		synchronized (snakesMap) {
			result.put("type", MsgType.GET_SNAKE);
			result.put("snakes", JSONObject.toJSONString(snakesMap));
			out.println(result.toJSONString());
		}
		out.flush();
		result.clear();
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
	public void eatFood(int index, FoodManager foodManager, Snake snake) {
		try {
			List<Food> foods = foodManager.getFoods();
			Food food = foods.get(index);
			LinkedList<Node> body = snake.getBody();
			Node head = body.peekFirst();
			Node tail = body.peekLast();
			int foodSize = food.getSize()/2;
			while(foodSize>0){
				synchronized (body) {
					body.addLast(new Node(head.getX(),head.getY(),tail.getAngle()));
				}
				foodSize--;
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
		result = new JSONObject();
		result.put("type", MsgType.UPDATE_FOOD);
		result.put("foodManager", JSONObject.toJSONString(foodManager));
		for (Entry<Integer, PrintWriter> entry : outPutMap.entrySet()) {
			PrintWriter writer = entry.getValue();
			writer.println(result.toJSONString());
			writer.flush();
		}
		result.clear();
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
