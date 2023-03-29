package com.luffy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarMapperTest {

    private CarMapper carMapper;

    @BeforeEach
    public void setUp() {
        carMapper = new CarMapperImpl();
    }
}
