package pack.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration 
// Spring의 설정 클래스임을 명시
@EnableWebSocketMessageBroker 
// 웹 소켓을 메시지 브로커와 함께 사용하도록 설정
// STOMP 프로토콜을 사용하여 메시지 전송 및 수신
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	// WebSocketMessageBrokerConfigurer : Spring에서 웹 소켓 메시지 브로커를 설정하는 데 사용하는 인터페이스
	// 웹 소켓과 STOMP 프로토콜을 사용하는 메시징 기능을 세부적으로 설정 가능

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// registerStompEndpoints : 클라이언트가 웹 소켓 연결을 시작할 수 있는 엔드포인트 등록
		
		registry.addEndpoint("/ws") 
				// 클라이언트가 웹 소켓 연결을 요청할 때 사용하는 경로 지정
				.setAllowedOriginPatterns("*") 
				// 모든 출처에서의 요청 허용
				// 보통 보안 강화 목적으로 실제 도메인으로 제한함
				.withSockJS();
				// SockJS : 웹 소켓을 지원하지 않는 브라우저에서도 사용 가능하게 함
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// configureMessageBroker : 메시지 브로커의 설정 정의
		// 메시지 브로커 : 서버에서 클라이언트로 메시지를 전송
		
		registry.enableSimpleBroker("/sub");
		// 메시지 브로커 활성화 -> /sub 로 시작하는 경로 사용
		// 여기서의 경로는 클라이언트가 구독할 수 있는 경로
		
		registry.setApplicationDestinationPrefixes("/pub");
		// 클라이언트가 서버로 메시지를 보낼 때 사용할 접두사
		// 클라이언트는 /pub으로 시작하는 경로로 메시지를 전송하고, 서버에서 처리함
	}
}
