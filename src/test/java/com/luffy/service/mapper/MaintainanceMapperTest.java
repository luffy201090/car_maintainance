package com.luffy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintainanceMapperTest {

    private MaintainanceMapper maintainanceMapper;

    @BeforeEach
    public void setUp() {
        maintainanceMapper = new MaintainanceMapperImpl();
    }
}
