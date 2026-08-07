package com.discounttracker.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * 처리되지 않은 예외를 로그로 남기고 응답 모양을 고정한다.
 *
 * <p>여기가 없을 때는 예외가 Spring 기본 처리로 500만 나가고 **어디에도
 * 기록이 남지 않았다.** 실제로 그 상태에서 두 번 당했다:
 *
 * <ul>
 *   <li>2026-08-03 — 트래커가 API보다 먼저 새 필드(badge)를 실어 배포해
 *       Jackson이 {@code UnrecognizedPropertyException}을 냈다.
 *       {@code /api/reload}가 500을 내는 것만 워크플로 exit code로 알았고,
 *       원인은 로컬에서 재현해서야 찾았다.</li>
 *   <li>2026-08-04 — 사람이 편집한 export.json에 {@code "soldOut": null}이
 *       들어와 reload 전체가 깨졌다. 화면이 안 나와서 알았다.</li>
 * </ul>
 *
 * <p>스택트레이스를 남기는 게 이 클래스의 전부다. 응답 본문에는 예외
 * 메시지를 넣지 않는다 — 내부 경로·파일명이 새어 나갈 이유가 없다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> onUnhandled(Exception e, WebRequest request) {
        log.error("처리되지 않은 예외 — {}", request.getDescription(false), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "internal_error"));
    }
}
