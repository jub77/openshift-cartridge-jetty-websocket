package org.ajeesh.app;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class TestWebSocket {

	private int i = 1;
	private Timer timer;
	private Session session;

	protected String getMyJsonTicker() {
		return "{\"hello\": \"world " + i++ + "\"}";
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("Close: " + reason);
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		System.out.println("Error: " + t.getMessage());
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
		this.timer = new Timer();
		System.out.println("Connect: " + session.getRemoteAddress().getAddress());

		try {
			session.getRemote().sendString("Hello Webbrowser");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
	}

	@OnWebSocketMessage
	public void onMessage(String data) {
		if (data.indexOf("disconnect") >= 0) {
			session.close(new CloseStatus(0, "closed"));
			timer.cancel();
		} else {
			sendMessage();
		}
	}

	private void sendMessage() {
		if (session == null || !session.isOpen()) {
			System.out.println("Connection is closed!!");
			return;
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					System.out.println("Running task");
					session.getRemote().sendString(getMyJsonTicker());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, new Date(), 5000);
	}
}
