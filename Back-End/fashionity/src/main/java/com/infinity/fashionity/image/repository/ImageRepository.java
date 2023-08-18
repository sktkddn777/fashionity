package com.infinity.fashionity.image.repository;

import com.infinity.fashionity.image.dto.ImageDTO;

import java.io.File;

public interface ImageRepository {
     ImageDTO imageSave(File file, String fileName);
     void imageRemove(String fileName);
}
