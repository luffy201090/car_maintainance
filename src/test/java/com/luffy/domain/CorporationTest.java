package com.luffy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorporationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Corporation.class);
        Corporation corporation1 = new Corporation();
        corporation1.setId(1L);
        Corporation corporation2 = new Corporation();
        corporation2.setId(corporation1.getId());
        assertThat(corporation1).isEqualTo(corporation2);
        corporation2.setId(2L);
        assertThat(corporation1).isNotEqualTo(corporation2);
        corporation1.setId(null);
        assertThat(corporation1).isNotEqualTo(corporation2);
    }
}
