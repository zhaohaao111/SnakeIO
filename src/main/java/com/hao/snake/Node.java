package com.hao.snake;

import java.io.Serializable;


public class Node implements Serializable{
	private static final long serialVersionUID = -186790183120066697L;
	private double x;
	private double y;
	private double angle;//角度
	
	public Node() {
	}

	public Node(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle*Math.PI/180;
	}
	
	public Node copy(Node node){
		this.x=node.getX();
		this.y=node.getY();
		this.angle=node.getAngle();
		return this;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	
	public double getY() {
		return y;
	}

	
	public void setY(double y) {
		this.y = y;
	}

	
	public double getAngle() {
		return angle;
	}

	
	public void setAngle(double angle) {
		this.angle = angle;
	}

	@Override
	public String toString() {
		return "Node [x=" + x + ", y=" + y + ", angle=" + angle + "]";
	}

	
	
}
