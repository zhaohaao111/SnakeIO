package com.hao.snake;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public enum Skin {
	SKIN_1(1, "/Interface/Skin/skin_5_head.png", "/Interface/Skin/skin_5_body.png", "/Interface/Skin/skin_5_tail.png"),
	SKIN_2(2, "/Interface/Skin/skin_3_head.png", "/Interface/Skin/skin_3_body.png", "/Interface/Skin/skin_3_tail.png"),
	SKIN_3(3, "/Interface/Skin/skin_5_head.png", "/Interface/Skin/skin_5_body.png", "/Interface/Skin/skin_5_tail.png"),
	SKIN_4(4,"/Interface/Skin/skin_1_head.png", "/Interface/Skin/skin_1_body.png", "/Interface/Skin/skin_1_tail.png"),
	SKIN_5(5,"/Interface/Skin/skin_4_head.png", "/Interface/Skin/skin_4_body2.png", "/Interface/Skin/skin_12_wreck.png"),
	SKIN_6(6,"/Interface/Skin/skin_11_head.png", "/Interface/Skin/skin_11_body.png", "/Interface/Skin/skin_11_body.png"),
	SKIN_7(7,"/Interface/Skin/skin_18_head.png", "/Interface/Skin/skin_18_body2.png", "/Interface/Skin/skin_18_body1.png"),
	SKIN_8(8,"/Interface/Skin/skin_12_head.png", "/Interface/Skin/skin_12_body.png", "/Interface/Skin/skin_12_body.png"),
	;
	private BufferedImage headImage;
	private BufferedImage bodyImage;
	private BufferedImage tailImage;
	private int index;
	private Skin(int index, String headUrl, String bodyUrl, String tailUrl) {
		try {
			this.index = index;
			this.headImage = ImageIO.read(this.getClass().getResourceAsStream(headUrl));
			this.bodyImage = ImageIO.read(this.getClass().getResourceAsStream(bodyUrl));
			this.tailImage = ImageIO.read(this.getClass().getResourceAsStream(tailUrl));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Skin getSkin(int index){
		for (Skin skin : values()) {
			if(skin.getIndex()==index){
				return skin;
			}
		}
		return null;
	}
	
	public BufferedImage getHeadImage() {
		return headImage;
	}
	
	public void setHeadImage(BufferedImage headImage) {
		this.headImage = headImage;
	}
	
	public BufferedImage getBodyImage() {
		return bodyImage;
	}
	
	public void setBodyImage(BufferedImage bodyImage) {
		this.bodyImage = bodyImage;
	}
	
	public BufferedImage getTailImage() {
		return tailImage;
	}
	
	public void setTailImage(BufferedImage tailImage) {
		this.tailImage = tailImage;
	}

	
	public int getIndex() {
		return index;
	}

	
	public void setIndex(int index) {
		this.index = index;
	}


}
