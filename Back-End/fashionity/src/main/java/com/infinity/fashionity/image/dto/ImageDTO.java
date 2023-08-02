package com.infinity.fashionity.image.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {
    private String fileName;//파일의 이름
    private String fileUrl;//접근 url
}
