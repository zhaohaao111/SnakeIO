package com.hao.snake;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public abstract class Layout {
	
	private BufferedImage img;
	
	private int layer;
	
	public Layout(){
	}

	public Layout(int layer, BufferedImage img) {
		this.layer = layer;
		this.img = img;
	}
	
	public abstract void paint(Graphics2D g);

	public BufferedImage getImg() {
		return img;
	}

	
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	
	public int getLayer() {
		return layer;
	}

	
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
}
