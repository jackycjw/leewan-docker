package com.leewan.ws;

import java.io.IOException;
import org.slf4j.Logger;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint("/machine")
@Component
public class MachineWebSocket {

	private Session session;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@OnOpen
    public void onOpen(Session session) {
		this.session = session;
	}
	
	public void sendMessage(String message) {
        try {
        	if(this.session != null) {
        		this.session.getBasicRemote().sendText(message);
        	}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
    }
}
