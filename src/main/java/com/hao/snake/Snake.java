package com.hao.snake;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Snake  extends Layout implements Serializable{
	
	private static final long serialVersionUID = -8894654319523582919L;
	private LinkedList<Node> body;
	private boolean isAlive;
	private int defaultLength;
	private double bodyWidth;
	private Skin skin;
	private AffineTransform trans;
	private Random r = new Random();
	DecimalFormat df = new DecimalFormat("#.000");
	
	public Snake(){
		this.isAlive = true;
		this.setLayer(2);
		defaultLength = Config.DEFAULT_LENGTH;
		skin = Config.DEFAULT_SKIN;
		bodyWidth = Config.DEFAULT_BODY_WIDTH;
		trans = new AffineTransform();
		this.body = new LinkedList<Node>();
		int startX = r.nextInt(Config.WIDTH);
		int startY = r.nextInt(Config.HEIGHT);
		for (int i = 1; i < defaultLength; i++) {
			body.add(new Node(startX, startY+i, 0));
		}
	}
	
	
	public void setDir(int x ,int y){
		Node head = body.peekFirst();
		double hx = head.getX()+bodyWidth;
		double hy = head.getY()+bodyWidth+Config.WINDOWS_TITLE_HEIGHT;//30为窗口的额外高度
		double x1= x - hx;
		double y1 = y - hy;
		double a = Double.valueOf(df.format(Math.atan2(y1,x1)+1.5707963267948966));
		head.setAngle(a);
	}
	
	public void move(int times){
		while(times>0){
			Node head = body.peekFirst();
			Node tail = body.pollLast();
			tail.copy(head);
			tail.setX(Double.valueOf(df.format(head.getX()+Math.sin(head.getAngle()))));
			tail.setY(Double.valueOf(df.format(head.getY()-Math.cos(head.getAngle()))));
			body.addFirst(tail);
			times--;
		}
	}
	
	public static void main(String[] args) {
		
	}
	
	public int eat(FoodManager foodManager){
		Node head = body.peekFirst();
		List<Food> foods = foodManager.getFoods();
		double headR = bodyWidth/2;
		double headX = head.getX()+headR;
		double headY = head.getY()+headR;
		for (int i = 0; i < foods.size(); i++) {
			Food food = foods.get(i);
			int foodR = food.getSize()/2;
			int foodX = food.getX()+foodR;
			int foodY = food.getY()+foodR;
			double aX = Math.pow(headX-foodX, 2);
			double aY = Math.pow(headY-foodY, 2);
			double aR = Math.pow(headR+foodR, 2);
			if(aX+aY<=aR){
				Music.play(Config.EAT_URL, false);
				Node tail = body.peekLast();
				int foodSize = food.getSize()/2;
				while(foodSize>0){
					synchronized (body) {
						body.addLast(new Node(head.getX(),head.getY(),tail.getAngle()));
					}
					foodSize--;
				}
				foods.remove(i);
				return i;
			}
		}
		return -1;
	}

	@Override
	public void paint(Graphics2D g) {
		BufferedImage img = null;
		for (int i = body.size()-1; i >=0 ; i--) {
			if(i%(bodyWidth/2)==0 || i==body.size()-1){
				Node node = body.get(i);
				if(i==0){
					img = skin.getHeadImage();
				}else if(i==body.size()-1){
					img = skin.getTailImage();
				}else {
					img = skin.getBodyImage();
				}
				trans.setToTranslation(node.getX(), node.getY());
				trans.scale(bodyWidth/img.getWidth(), bodyWidth/img.getHeight());
				trans.rotate(node.getAngle(), img.getWidth()/2, img.getHeight()/2);
				g.drawImage(img, trans, null);
			}
		}
	}
	
	public LinkedList<Node> getBody() {
		return body;
	}

	
	public void setBody(LinkedList<Node> body) {
		this.body = body;
	}

	
	public boolean isAlive() {
		return isAlive;
	}

	
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	
	public Skin getSkin() {
		return skin;
	}

	
	public void setSkin(Skin skin) {
		this.skin = skin;
	}
	
	
}
