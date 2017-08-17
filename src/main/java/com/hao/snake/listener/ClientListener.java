package com.hao.snake.listener;

import java.io.PrintWriter;

import com.hao.snake.Skin;
import com.hao.snake.Snake;


public interface ClientListener {
	
	void playJoin(Skin skin, PrintWriter out);
	
	void eatFood(int index, PrintWriter out);
	
	void beforePaint(PrintWriter out);
	
	void afterPaint(PrintWriter out, Snake snake);
	
}
