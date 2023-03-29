package com.luffy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintainanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Maintainance.class);
        Maintainance maintainance1 = new Maintainance();
        maintainance1.setId(1L);
        Maintainance maintainance2 = new Maintainance();
        maintainance2.setId(maintainance1.getId());
        assertThat(maintainance1).isEqualTo(maintainance2);
        maintainance2.setId(2L);
        assertThat(maintainance1).isNotEqualTo(maintainance2);
        maintainance1.setId(null);
        assertThat(maintainance1).isNotEqualTo(maintainance2);
    }
}
