package com.luffy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintainanceDetailsMapperTest {

    private MaintainanceDetailsMapper maintainanceDetailsMapper;

    @BeforeEach
    public void setUp() {
        maintainanceDetailsMapper = new MaintainanceDetailsMapperImpl();
    }
}
