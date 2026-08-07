package com.discounttracker.web;

import com.discounttracker.comparison.BrandComparisonService;
import com.discounttracker.offer.OfferRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 처리되지 않은 예외가 500 + 고정된 본문으로 나가는지 고정한다.
 *
 * <p>핸들러가 하는 일의 본질은 로그를 남기는 것이라 눈에 안 보인다 —
 * 테스트가 없으면 다음 리팩터링에서 조용히 사라져도 아무도 모른다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired MockMvc mvc;
    @MockBean BrandComparisonService service;
    @MockBean OfferRepository offers;

    @Test
    void unhandledExceptionBecomes500WithoutLeakingTheMessage() throws Exception {
        given(service.compare()).willThrow(new IllegalStateException("export.json 읽기 실패: /home/ubuntu/..."));

        mvc.perform(get("/api/brands"))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath("$.error").value("internal_error"))
           // 내부 경로·파일명이 응답으로 새면 안 된다
           .andExpect(jsonPath("$.message").doesNotExist());
    }
}
