package com.hao.snake;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class BackGround extends Layout{

	public BackGround(int layer) {
//		super(layer, new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB));
		
		this.setLayer(layer);
		try {
			BufferedImage img = ImageIO.read(this.getClass().getResourceAsStream("/Interface/bg45.jpg"));
			BufferedImage temp = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = temp.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.copyArea(0, 0, img.getWidth(), img.getHeight(), img.getWidth(), 0);
			g.copyArea(0, 0, img.getWidth()*2, img.getHeight(), img.getWidth()*2, 0);
			g.copyArea(0, 0, img.getWidth()*4, img.getHeight(), 0, img.getHeight());
			g.copyArea(0, 0, img.getWidth()*4, img.getHeight(), 0, img.getHeight()*2);
			g.dispose();
			this.setImg(temp);
			img = null;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics2D g) {
		g.drawImage(this.getImg(), 0, 0, null);
	}
	
	public static void main(String[] args) {
		BackGround bg = new BackGround(1);
		bg.getImg();
	}
	
}
