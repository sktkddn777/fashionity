package com.infinity.fashionity.alarm.controller;

import com.infinity.fashionity.alarm.dto.AlarmDTO;
import com.infinity.fashionity.alarm.service.AlarmService;
import com.infinity.fashionity.security.dto.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<List<AlarmDTO>> getAlarm(
            @AuthenticationPrincipal JwtAuthentication auth
            ){
        Long seq = auth == null ? null : auth.getSeq();

        return new ResponseEntity<>(alarmService.findAll(seq), HttpStatus.OK);
    }

    @PostMapping("/{alarmSeq}")
    public ResponseEntity<Boolean> readAlarm(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("alarmSeq") Long alarmSeq
    ){
        return new ResponseEntity<>(alarmService.readAlarm(auth == null ? null : auth.getSeq(),alarmSeq),HttpStatus.OK);
    }

    @DeleteMapping("/{alarmSeq}")
    public ResponseEntity<Boolean> deleteAlarm(
            @AuthenticationPrincipal JwtAuthentication auth,
            @PathVariable("alarmSeq") Long alarmSeq
    ){
        return new ResponseEntity<>(alarmService.deleteAlarm(auth == null ? null : auth.getSeq(),alarmSeq),HttpStatus.OK);
    }
}
