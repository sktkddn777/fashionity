package com.infinity.fashionity.image.repository;

import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;

import java.io.File;

public interface ImageRepository {
    //파일을 받아 저장. 저장 후 path return
     ImageDTO imageSave(File file, String fileName);
     void imageRemove(String fileName);
}
