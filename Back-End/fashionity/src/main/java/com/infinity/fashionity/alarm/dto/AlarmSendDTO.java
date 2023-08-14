package com.infinity.fashionity.alarm.dto;

import com.infinity.fashionity.alarm.entity.AlarmType;
import lombok.*;

public class AlarmSendDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Request{
        //알림을 저장하는 타입
        private AlarmType type;
        //알림을 받을 주인
        private Long ownerSeq;
        //게시글 seq
        private Long postSeq;
        //댓글 seq
        private Long commentSeq;
        //이벤트를 발행하는 사람
        private Long publisherSeq;
    }
}
