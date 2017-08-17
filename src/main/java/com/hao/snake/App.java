package com.hao.snake;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;



public class App extends JPanel implements MouseMotionListener, Runnable{
	
	private static final long serialVersionUID = 1L;
	private BackGround backGround;
	private Snake snake;
	private long paintSpeed;
	private long actionSpeed;
	private FoodManager foodManager;
	private Thread actionThread;
	private Integer mouseX = null;
	private Integer mouseY = null;
	public static int DX = 0;//绘图坐标的偏移量
	public static int DY = 0;//绘图坐标的偏移量
	BufferedImage tempImg = new BufferedImage(Config.WIDTH, Config.HEIGHT, BufferedImage.TYPE_INT_RGB);
	public App(){
		backGround = new BackGround(1);
		snake = new Snake();
		snake.setSkin(Config.DEFAULT_SKIN);
		paintSpeed = 1000/40;
		actionSpeed = paintSpeed;
		foodManager = new FoodManager();
		foodManager.init(snake);
		Music.play(Config.BGM_URL, true);
		actionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(snake.isAlive()){
					try {
						
						Thread.sleep(actionSpeed);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
//		actionThread.start();
	}
	
	public static void main(String[] args) {
		App app = new App();
		JFrame frame = new JFrame();
		frame.add(app);
		frame.setSize(Config.WINDOWS_WIDTH, Config.WINDOWS_HEIGHT + Config.WINDOWS_TITLE_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addMouseMotionListener(app);
		Thread t = new Thread(app);
		t.start();
	}
	
	@Override
	public void run() {
		while(snake.isAlive()){                                                             
			repaint();
			try {
				Thread.sleep(paintSpeed);
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
		Node head = snake.getBody().getFirst();
		
		double headX = head.getX();
		double headY = head.getY();
		
		double x = headX - Config.WINDOWS_WIDTH/2;
		double y = headY - Config.WINDOWS_HEIGHT/2;
		
		double maxX = Config.WIDTH - Config.WINDOWS_WIDTH;
		double maxY = Config.HEIGHT - Config.WINDOWS_HEIGHT;
		
		paintX = x>0?x:0;
		paintY = y>0?y:0;
		App.DX = (int) (paintX>maxX?maxX:paintX);
		App.DY = (int) (paintY>maxY?maxY:paintY);
		Graphics2D tg = tempImg.createGraphics();
//		tg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		backGround.paint(tg);
		snake.paint(tg);
		foodManager.paint(tg);
		tg.dispose();
		g.drawImage(tempImg.getSubimage(App.DX, App.DY, Config.WINDOWS_WIDTH, Config.WINDOWS_HEIGHT), 0, 0, null);
		g.dispose();
		
		if(mouseX != null && mouseY != null){
			snake.setDir(mouseX, mouseY);
			mouseX = null;
			mouseY = null;
		}
		snake.move(3);
		snake.eat(foodManager);
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX()+App.DX;
		mouseY = e.getY()+App.DY;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}


}
