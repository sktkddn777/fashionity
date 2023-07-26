package com.infinity.fashionity.consultants.controller;

import com.infinity.fashionity.consultants.dto.ConsultantDTO;
import com.infinity.fashionity.consultants.service.ConsultantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consultants")
public class ConsultantController {

    private final ConsultantService consultantService;
    // 유저는 이따가 유저쪽으로 넘기기
    // [유저] 전체 컨설턴트 목록 조회
    @GetMapping("")
    public ResponseEntity<List<ConsultantDTO.Response>> allConsultants(){
        List<ConsultantDTO.Response> response = consultantService.allConsultants();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
