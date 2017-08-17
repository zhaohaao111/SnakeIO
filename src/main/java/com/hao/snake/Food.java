package com.hao.snake;

import java.io.Serializable;

public class Food implements Serializable{
	private static final long serialVersionUID = 182049338994836767L;
	private int x;
	private int y;
	private int size;
	
	public Food(){}
	
	public Food(int x, int y, int size) {
		super();
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
