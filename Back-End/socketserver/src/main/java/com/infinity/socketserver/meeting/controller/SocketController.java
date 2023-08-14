package com.infinity.socketserver.meeting.controller;

import com.infinity.socketserver.meeting.dto.SocketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    // messagingTemplate 주입
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // /receive를 메시지를 받을 endpoint로 설정합니다.
    @MessageMapping("/chatting/receive/**")

    // /send로 메시지를 반환합니다.
//    @SendTo("/chatting/send/**")

    // SocketHandler는 1) /receive에서 메시지를 받고, /send로 메시지를 보내줍니다.
    // 정의한 SocketVO를 1) 인자값, 2) 반환값으로 사용합니다.
    public SocketVO SocketHandler(SocketVO socketVO) {
        // vo에서 getter로 userName을 가져옵니다.
        String userName = socketVO.getUserName();
        System.out.println("보낸 사람 : " + socketVO.getUserName());

        // vo에서 setter로 content를 가져옵니다.
        String content = socketVO.getContent();
        System.out.println("받은 문자 : " + socketVO.getContent());

        String roomId = socketVO.getRoomId();
        System.out.println("현재 세션 : " + socketVO.getRoomId());

        String type = socketVO.getType();
        System.out.println("받은 타입 : " + socketVO.getType());

        System.out.println();

        // 생성자로 반환값을 생성합니다.
        SocketVO result = new SocketVO(userName, content, roomId, type);

        sendToRoom(roomId,result);
        // 반환
        return result;
    }
    // 특정 roomId에 메시지를 전송하는 메서드
    private void sendToRoom(String roomId, SocketVO message) {
        // roomId를 포함한 토픽 주소로 메시지 전송
        String topic = "/chatting/send/" + roomId;
//        System.out.println();
        messagingTemplate.convertAndSend(topic, message);
    }



}