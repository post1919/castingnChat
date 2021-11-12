package castingn.boot.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * @Class Name : WebSocketConfig.java
 * @Description : WebSocketConfig
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2021.03.02  차성순         최초 생성
 *
 *  @author 개발팀 차성순
 *  @since 2021.03.02
 *  @version 1.0
 *  @see 
 *  @비고 : 메시지 브로커는 특정 주제를 구독 한 연결된 모든 클라이언트에게 메시지를 broadcast
 *  송신 호스트가 전송한 데이터가 네트워크에 연결된 모든 호스트에 전송되는 방식을 의미
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


	/**
	 * 클라이언트가 웹 소켓 서버에 연결
	 * 
	 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS().setClientLibraryUrl("https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.4.0/sockjs.min.js");
    }

    /**
     * 클라이언트에서 다른 클라이언트로 메시지 라우팅 브로커
     * 
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/chat"); // "/chat" 시작되는 메시지가 message-handling methods으로 라우팅 되어야 함.
        
        // topic : 암시적으로 1:N 전파를 의미, /queue 암시적으로 1:1 전파를 의미
        registry.enableSimpleBroker("/topic"); // "/topic" 시작되는 메시지가 메시지 브로커로 라우팅 되어야 함(해당 주제에 가입한 모든 클라이언트에게 메시지를 방송).
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(10485760); // 10MB (default : 64 * 1024)
        registration.setSendTimeLimit(10 * 10000); // default : 10 * 10000
        registration.setSendBufferSizeLimit(3* 512 * 1024); // default : 512 * 1024

    }


}