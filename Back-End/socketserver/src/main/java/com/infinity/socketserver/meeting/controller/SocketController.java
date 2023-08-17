package com.infinity.socketserver.meeting.controller;

import com.infinity.socketserver.meeting.dto.SocketVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // /receive를 메시지를 받을 endpoint로 설정
    @MessageMapping("/chatting/receive/**")

    public SocketVO SocketHandler(SocketVO socketVO) {
        String userName = socketVO.getUserName();
        System.out.println("보낸 사람 : " + socketVO.getUserName());

        String content = socketVO.getContent();
        System.out.println("받은 문자 : " + socketVO.getContent());

        String roomId = socketVO.getRoomId();
        System.out.println("현재 세션 : " + socketVO.getRoomId());

        String type = socketVO.getType();
        System.out.println("받은 타입 : " + socketVO.getType());

        System.out.println();

        SocketVO result = new SocketVO(userName, content, roomId, type);

        sendToRoom(roomId,result);
        return result;
    }

    // 특정 roomId에 메시지를 전송하는 메서드
    private void sendToRoom(String roomId, SocketVO message) {
        // roomId를 포함한 토픽 주소로 메시지 전송
        String topic = "/chatting/send/" + roomId;
        messagingTemplate.convertAndSend(topic, message);
    }



}