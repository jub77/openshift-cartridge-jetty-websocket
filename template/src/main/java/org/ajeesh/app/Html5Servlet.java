package org.ajeesh.app;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class Html5Servlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void configure(WebSocketServletFactory f) {
		f.getPolicy().setIdleTimeout(10000);
		f.register(TestWebSocket.class);
	}
}
