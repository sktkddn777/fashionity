package com.infinity.fashionity.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.infinity.fashionity.alarm.entity.AlarmType;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlarmDTO {
    @JsonProperty(value="alarm_seq")
    private Long alarmSeq;
    private String imageUrl;
    @JsonProperty(value="publisher_nickname")
    private String publisherNickname;

    @JsonProperty(value="post_seq")
    private Long postSeq;
    private String title;
    private String content;

    private AlarmType type;

    @JsonProperty(value="is_readed")
    private Boolean isReaded;
}
