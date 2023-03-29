package com.luffy.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrandMapperTest {

    private BrandMapper brandMapper;

    @BeforeEach
    public void setUp() {
        brandMapper = new BrandMapperImpl();
    }
}
