package com.hao.snake.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hao.snake.FoodManager;
import com.hao.snake.Skin;
import com.hao.snake.Snake;
import com.hao.snake.common.MsgType;
import com.hao.snake.listener.ClientListener;


public class SnakeClient {
	public static final String IP_ADDR = "127.0.0.1";//服务器地址  这里要改成服务器的ip  
    public static final int PORT = 12345;//服务器端口号    
    OutputStream outputStream;
    PrintWriter out;
    int port;
    ClientApp app;
      
    public SnakeClient(final Skin skin, final ClientListener clientListener){
    	try {
			Socket socket = new Socket(IP_ADDR, PORT);
			port = socket.getLocalPort();
			final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = socket.getOutputStream();
			out = new PrintWriter(outputStream,true);
			Thread t= new Thread(new Runnable() {
				@Override
				public void run() {
					clientListener.playJoin(skin, out);
					while(true){
						try {
							String jsonStr =in.readLine();
		            		JSONObject json = JSONObject.parseObject(jsonStr);
		            		String type = json.getString("type");
		            		if(type == null){
								continue;
							}
		            		switch(type){
		            			case MsgType.INIT:
		            				ConcurrentHashMap<Integer, Snake> snakes = JSONObject.parseObject(json.getString("snakes"), new TypeReference<ConcurrentHashMap<Integer, Snake>>(){});
		            				FoodManager foodManager = JSONObject.parseObject(json.getString("foodManager"), FoodManager.class);
		            				app = new ClientApp(port, snakes, out, foodManager, clientListener);
		            				app.start();
		            				break;
		            			case MsgType.GET_SNAKE:
		            				snakes = JSONObject.parseObject(json.getString("snakes"), new TypeReference<ConcurrentHashMap<Integer, Snake>>(){});
		            				if(snakes != null && snakes.size()>0){
		            					app.setSnakes(snakes);
		            				}else{
		            					System.out.println("获取蛇异常");
		            				}
		            				break;
		            			case MsgType.UPDATE_FOOD:
		            				foodManager = JSONObject.parseObject(json.getString("foodManager"), FoodManager.class);
		            				app.setFoodManager(foodManager);
		            				break;
		            		}
						}
						catch (IOException e) {
							break;
						}
					}
				}
			});
			t.start();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws Exception {
    	SnakeClient client = new SnakeClient(Skin.SKIN_2, new ClientListenerHander());
	}
      
}
