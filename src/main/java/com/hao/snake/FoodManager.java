package com.hao.snake;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoodManager extends Layout implements Serializable{
	private static final long serialVersionUID = 1872523945393095171L;
	private Random r = new Random();
	private List<Food> foods = new ArrayList<Food>();
	
	public FoodManager() {
		super(3, null);
	}
	
	public List<Food> getFoods() {
		return foods;
	}
	
	public void init(Snake snake){
		for (int i = 0; i < Config.DEFAULT_FOODS; i++) {
			createFood(snake);
		}
	}
	
	public void createFood(Snake snake){
		foods.add(new Food(r.nextInt(Config.WIDTH), r.nextInt(Config.HEIGHT), r.nextInt(Config.DEFAULT_BODY_WIDTH/2)+4));
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.RED);
		for (int i = 0; i < foods.size(); i++) {
			Food food = foods.get(i);
			g.fillOval(food.getX()+food.getSize()/2, food.getY()+food.getSize()/2, food.getSize()/2, food.getSize()/2);
		}
	}
}
