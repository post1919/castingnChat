package castingn.boot.listner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import castingn.com.chat.ChatVo;
import castingn.com.chat.service.ChatService;

@Component
public class WebSocketEventListner {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListner.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private ChatService chatService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		logger.info("==========> new web socket connection");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) throws Exception{

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");
	    String path = (String) headerAccessor.getSessionAttributes().get("path");
	    String uPk = (String) headerAccessor.getSessionAttributes().get("uPk");

	    if(uPk != null) {
	    	
	    	Map<String, Object> param = new HashMap<String, Object>();

	    	logger.info("==========> User Disconnected : " + uPk);

	        ChatVo chatVo = new ChatVo();
	        chatVo.setUserStatus(chatVo.getUserStatus().LEAVE);
	        chatVo.setUserNm(username);

	        chatVo.setContent(chatVo.getUserNm() + " 님이 채팅방에서 퇴장하셨습니다.");

			param.put("content", chatVo.getContent());
			param.put("chatStat", chatVo.getUserStatus()); // 채팅유형(JOIN: 채팅방참여, LEAVE:채팅방퇴장, CHAT:일반채팅, FILE:파일전송)
			param.put("uPk", uPk);
			param.put("cmPk", path);
			param.put("msgType", "leave_user");
			param.put("filePath", null);
			param.put("cdOrgFileNm", null);
			param.put("cdNewFileNm", null);

			chatService.insertChatDetail(param); // 채팅내용 등록

	        headerAccessor.getSessionAttributes().put("username", chatVo.getUserNm());
	        headerAccessor.getSessionAttributes().put("path", path);

	        messagingTemplate.convertAndSend("/topic/" + path, chatVo);
	    }
	}
	
    
}
