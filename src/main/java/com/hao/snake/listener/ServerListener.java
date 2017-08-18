package com.hao.snake.listener;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import com.hao.snake.FoodManager;
import com.hao.snake.Node;
import com.hao.snake.Skin;
import com.hao.snake.Snake;


public interface ServerListener {
	
	void playJoin(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out, FoodManager foodManager, Skin skin, int port);
	
	void getSnake(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out);
	
	void updateSnake(Node head, Snake snake);
	
	void getHead(ConcurrentHashMap<Integer, Snake> snakesMap, PrintWriter out);
	
	void eatFood(int index, FoodManager foodManager, ConcurrentHashMap<Integer, Snake> snakesMap, int port);
	
	void updateFoods(ConcurrentHashMap<Integer, PrintWriter> outPutMap, FoodManager foodManager);
	
	void playLogOut(int port, ConcurrentHashMap<Integer, Snake> snakesMap, ConcurrentHashMap<Integer, PrintWriter> outPutMap);
	
}
