package com.hao.snake.client;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.alibaba.fastjson.JSONObject;
import com.hao.snake.BackGround;
import com.hao.snake.Config;
import com.hao.snake.FoodManager;
import com.hao.snake.Music;
import com.hao.snake.Node;
import com.hao.snake.Skin;
import com.hao.snake.Snake;
import com.hao.snake.common.MsgType;
import com.hao.snake.listener.ClientListener;



public class ClientApp extends JPanel implements MouseMotionListener, Runnable{
	
	private static final long serialVersionUID = 1L;
	private BackGround backGround;
	private Snake snake;
	private ConcurrentHashMap<Integer, Snake> snakes;
	private long paintSpeed;
	private int port;
	private FoodManager foodManager;
	private Integer mouseX = null;
	private Integer mouseY = null;
	public static int DX = 0;//绘图坐标的偏移量
	public static int DY = 0;//绘图坐标的偏移量
	private ClientListener clientListener;	
	private PrintWriter out;
	
	BufferedImage tempImg = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB);
	public ClientApp(int port, ConcurrentHashMap<Integer, Snake> snakes, PrintWriter out, FoodManager foodManager, ClientListener clientListener){
		this.backGround = new BackGround(1);
		System.out.println(port);
		this.snakes = snakes;
		this.port = port;
		this.out = out;
		this.snake = snakes.get(port);
		this.paintSpeed = 1000/30;
		this.foodManager = foodManager;
		this.clientListener = clientListener;
		Music.play(Config.BGM_URL, true);
	}
	
	public void start() {
		JFrame frame = new JFrame();
		frame.add(this);
		frame.setSize(Config.WINDOWS_WIDTH, Config.WINDOWS_HEIGHT + Config.WINDOWS_TITLE_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addMouseMotionListener(this);
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while(true){                                                             
			try {
				clientListener.beforePaint(out);
				Thread.sleep(paintSpeed/2);
				repaint();
				Thread.sleep(paintSpeed/2);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void paint(Graphics g) {
		double paintX = 0;
		double paintY = 0;
		Graphics2D tg = tempImg.createGraphics();
		backGround.paint(tg);
		for (Entry<Integer, Snake> entry : snakes.entrySet()) {
			Snake temp = entry.getValue();
			temp.paint(tg);
		}
		foodManager.paint(tg);
		tg.dispose();
		
		Node head = snake.getBody().getFirst();
		
		double headX = head.getX();
		double headY = head.getY();
		
		double x = headX - Config.WINDOWS_WIDTH/2;
		double y = headY - Config.WINDOWS_HEIGHT/2;
		
		double maxX = Config.WIDTH - Config.WINDOWS_WIDTH;
		double maxY = Config.HEIGHT - Config.WINDOWS_HEIGHT;
		
		paintX = x>0?x:0;
		paintY = y>0?y:0;
		ClientApp.DX = (int) (paintX>maxX?maxX:paintX);
		ClientApp.DY = (int) (paintY>maxY?maxY:paintY);
		g.drawImage(tempImg.getSubimage(ClientApp.DX, ClientApp.DY, Config.WINDOWS_WIDTH, Config.WINDOWS_HEIGHT), 0, 0, null);
		g.dispose();
		if(mouseX != null && mouseY != null){
			snake.setDir(mouseX, mouseY);
			mouseX = null;
			mouseY = null;
		}
//		snake.move(3);
		int index = snake.eat(foodManager);
		if(index != -1){
			clientListener.eatFood(index, out);
		}
		clientListener.afterPaint(out, snake);
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX()+ClientApp.DX;
		mouseY = e.getY()+ClientApp.DY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	
	public ConcurrentHashMap<Integer, Snake> getSnakes() {
		return snakes;
	}

	public void setSnakes(ConcurrentHashMap<Integer, Snake> snakes) {
		this.snakes = snakes;
		this.snake = snakes.get(port);
	}

	
	public FoodManager getFoodManager() {
		return foodManager;
	}

	
	public void setFoodManager(FoodManager foodManager) {
		this.foodManager = foodManager;
	}


}
