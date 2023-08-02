package com.infinity.fashionity.image.service;

import com.infinity.fashionity.image.dto.ImageDeleteDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;

public interface ImageService {
    ImageSaveDTO.Response save(ImageSaveDTO.Request dto);
    ImageDeleteDTO.Response delete(ImageDeleteDTO.Request dto);
}
