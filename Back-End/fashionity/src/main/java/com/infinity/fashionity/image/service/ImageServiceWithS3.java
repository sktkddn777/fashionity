package com.infinity.fashionity.image.service;

import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageDeleteDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;
import com.infinity.fashionity.image.exception.ImageException;
import com.infinity.fashionity.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * TODO :
 * - 임시 이미지 파일 저장 중 에러발생 시 이전에 저장했던 임시 이미지파일들 모두 삭제하기 구현
 * - 생성날짜별로 폴더를 만들어서 관리하도록 path설정
 * */
@Service
@RequiredArgsConstructor
public class ImageServiceWithS3 implements ImageService{
    private final ImageRepository imageRepository;

    /**
     * 랜덤한 이름을 발급해주는 메서드
     * */
    public String generateRandomName(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();

        StringBuffer sb = new StringBuffer();
        String fullName = sb.append(year)
                .append('/')
                .append(month)
                .append('/')
                .append(day)
                .append('/')
                .append(UUID.randomUUID().toString())
                .toString();

        return fullName;
    }

    /**
     * MultipartFile을 File객체로 변환하는 메서드
     * */
    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(System.getProperty("user.dir").concat("/").concat(StringUtils.randomSting(30)));//해당 이름으로 파일을 만듬

        try {
            if (file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {//해당 파일에 outputStream을 이용하여 이미지 데이터 작성
                    fos.write(multipartFile.getBytes());
                }
                return Optional.of(file);
            } else {
                return Optional.empty();
            }
        }catch(IOException e){
            throw new ImageException(ErrorCode.IMAGE_SAVE_EXCEPTION);
        }
    }

    private void deleteFile(File file){
        if(file.exists()){
            file.delete();
        }
    }

    private ImageDTO uploadFile(File file){
        String fileName = generateRandomName();
        return imageRepository.imageSave(file, fileName);
    }

    /**
     * 이미지 저장하는 메서드
     * 파일 타입을 확인(이미지 파일들만 accept)
     * 파일 압축 -> 우선순위 낮음
     *
     *
     * */
    @Override
    @Transactional
    public ImageSaveDTO.Response save(ImageSaveDTO.Request dto) {
        List<MultipartFile> multipartFiles = dto.getImages();

        //이미지파일이 아닌것이 들어오면 THROW
        multipartFiles.stream()
                .filter(file->!file.getContentType().toLowerCase().startsWith("image"))
                .findAny()
                .ifPresent((f)->new ImageException(ErrorCode.INVALID_IMAGE_TYPE));

        //해당 MultipartFile들을 File들로 임시 파일 생성
        List<File> tempFiles = multipartFiles.stream()
                .map(this::convertMultipartFileToFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        //임시파일들을 s3저장소로 업로드
        List<ImageDTO> images = tempFiles.stream()
                .map(this::uploadFile)
                .collect(Collectors.toList());

        //저장된 임시 파일들 삭제
        tempFiles.stream()
                .forEach(this::deleteFile);

        return ImageSaveDTO.Response.builder()
                .imageInfos(images)
                .build();
    }

    /**
     * 이미지를 삭제하는 메서드
     * */
    @Override
    @Transactional
    public ImageDeleteDTO.Response delete(ImageDeleteDTO.Request dto) {
        List<ImageDTO> images = dto.getImages();
        images.stream()
                .map(image->image.getFileName())
                .forEach(imageRepository::imageRemove);
        return ImageDeleteDTO.Response.builder()
                .success(true)
                .build();
    }
}
