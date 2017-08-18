package com.hao.snake.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.hao.snake.FoodManager;
import com.hao.snake.Node;
import com.hao.snake.Skin;
import com.hao.snake.Snake;
import com.hao.snake.common.MsgType;
import com.hao.snake.listener.ServerListener;

public class SnakeServer {
	
	private ConcurrentHashMap<Integer, Snake> snakesMap = new ConcurrentHashMap<Integer, Snake>();
	
	private ConcurrentHashMap<Integer, PrintWriter> clientsOut = new ConcurrentHashMap<Integer, PrintWriter>();
	
	private FoodManager foodManager;
	
	private ServerListener serverListener;
	
	public static final int PORT = 12345;//监听的端口号     
	
	public static void main(String[] args) {
		SnakeServer server = new SnakeServer(new ServerListenerHander());
		server.init();
	}
	
	public SnakeServer(ServerListener serverListener) {
		super();
		this.serverListener = serverListener;
	}



	public void init() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			foodManager = new FoodManager();
			foodManager.init(null);
			while (true) {
				Socket client = serverSocket.accept();
				new HandlerThread(client);
			}
		}
		catch (Exception e) {
			System.out.println("服务器异常: " + e.getMessage());
		}
	}
	
	private class HandlerThread implements Runnable {
		
		private Socket socket;
		
		private boolean isAlive;
		
		private int port;
		
		Snake snake = null;
		
		public HandlerThread(Socket client) {
			port = client.getPort();
			socket = client;
			isAlive = true;
			new Thread(this).start();
		}
		
		int times = 0;
		
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				OutputStream outputStream = socket.getOutputStream();
				PrintWriter out = new PrintWriter(outputStream, true);
				clientsOut.put(port, out);
				while (isAlive) {
					String jsonStr = in.readLine();
					JSONObject json = JSONObject.parseObject(jsonStr);
					String type = json.getString("type");
					if(type == null){
						continue;
					}
					switch (type) {
						case MsgType.INIT:
							serverListener.playJoin(snakesMap, out, foodManager, Skin.getSkin(json.getInteger("skinIndex")), port);
							snake = snakesMap.get(port);
							break;
						case MsgType.GET_SNAKE:
							serverListener.getSnake(snakesMap, out);
							break;
						case MsgType.UPDATE_SNAKE:
							Node temp = JSONObject.parseObject(json.getString("head"), Node.class);
							serverListener.updateSnake(temp, snake);
							break;
						case MsgType.GET_HEADS:
							times++;
							if(times>=30){
								serverListener.getSnake(snakesMap, out);
								times = 0;
								break;
							}
							serverListener.getHead(snakesMap, out);
							break;
						case MsgType.EAT_FOOD:
							int index = json.getIntValue("index");
							serverListener.eatFood(index, foodManager, snakesMap, port);
							serverListener.updateFoods(clientsOut, foodManager);
							serverListener.getSnake(snakesMap, out);
							times = 0;
							break;
					}
				}
			}
			catch (SocketException e) {
				isAlive = false;
				snakesMap.remove(port);
			}
			catch (Exception e) {
				isAlive = false;
				e.printStackTrace();
			}
			finally {
				isAlive = false;
				if (socket != null) {
					try {
						socket.close();
					}
					catch (Exception e) {
						socket = null;
						System.out.println("服务端 finally 异常:" + e.getMessage());
					}
				}
			}
		}
	}
}
