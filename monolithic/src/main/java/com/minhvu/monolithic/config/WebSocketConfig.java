package com.minhvu.monolithic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("Registering WebSocket endpoint");
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Cho phép mọi nguồn gốc
                .withSockJS(); // Hỗ trợ giao thức SockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        System.out.println("Configuring WebSocket broker");
        registry.setApplicationDestinationPrefixes("/app"); // Tiền tố cho client gửi tin nhắn
        registry.enableSimpleBroker("/chatroom", "/user");  // Tiền tố cho broker gửi lại tin nhắn
        registry.setUserDestinationPrefix("/user");         // Cấu hình tin nhắn đích danh
    }
}
