package com.discounttracker.deploy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** 2026-10-29 무중단 배포와 롤백 시험용. 배포 한 번을 일으키려고 넣었고 시험 뒤 되돌린다(mono #188). */
class DeployProbeTest {

    @Test
    void probe() {
        assertTrue(true);
    }
}
