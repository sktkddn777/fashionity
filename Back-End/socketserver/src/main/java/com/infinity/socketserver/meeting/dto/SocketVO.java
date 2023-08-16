package com.infinity.socketserver.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SocketVO {
    // 유저의 이름
    private String userName;

    // 메시지의 내용
    private String content;

    // 메세지를 보낼 곳
    private String roomId;

    //소켓으로 들어온 데이터 유형
    private String type;
}