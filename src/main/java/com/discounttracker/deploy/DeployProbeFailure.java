package com.discounttracker.deploy;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** 자동 복구 시험용(되돌릴 예정). 본 인스턴스 포트(8088)에서만 시작에 실패한다. 시험과 임시 인스턴스는 영향이 없다. */
@Component
class DeployProbeFailure {
    @Value("${server.port:0}")
    private int port;

    @PostConstruct
    void check() {
        if (port == 8088) throw new IllegalStateException("deploy probe: primary instance must fail to start");
    }
}
