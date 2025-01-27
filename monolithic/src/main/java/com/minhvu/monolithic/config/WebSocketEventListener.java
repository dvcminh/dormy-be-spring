//package com.minhvu.monolithic.config;
//
//import com.minhvu.monolithic.repository.UserRepository;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.GenericMessage;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Data
//public class WebSocketEventListener {
//    private Set<OnlineUserDto> onlineUsrs;
//
//    @Autowired
//    private SimpMessageSendingOperations messagingTemplate;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//
//        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        @SuppressWarnings("rawtypes")
//        GenericMessage connectHeader = (GenericMessage) stompAccessor
//                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
//        // to the server
//        @SuppressWarnings("unchecked")
//        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
//                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//
//        String login = nativeHeaders.get("username").get(0); (1)
//        String sessionId = stompAccessor.getSessionId();
//        if(this.onlineUsrs==null){
//            this.onlineUsrs = new HashSet<>();
//        }
//        Optional<UserEntity> usr = userRepository.findByUsername(login);
//        if(usr.isPresent()){
//            OnlineUserDto onl = MapperUtils.mapperObject(usr.get(), OnlineUserDto.class);
//            onl.setSessionId(sessionId);
//            this.onlineUsrs.add(onl);
//        }
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String sessionId = stompAccessor.getSessionId();
//        OnlineUserDto offlineUsr = this.onlineUsrs
//                .stream()
//                .filter((a)->a.getSessionId().equals(sessionId))
//                .collect(Collectors.toList()).get(0);
//        this.onlineUsrs.remove(offlineUsr);
//    }
//
//}