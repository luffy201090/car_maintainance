package com.luffy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorporationMapperTest {

    private CorporationMapper corporationMapper;

    @BeforeEach
    public void setUp() {
        corporationMapper = new CorporationMapperImpl();
    }
}
