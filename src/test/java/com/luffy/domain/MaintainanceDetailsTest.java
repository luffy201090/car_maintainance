package com.luffy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintainanceDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintainanceDetails.class);
        MaintainanceDetails maintainanceDetails1 = new MaintainanceDetails();
        maintainanceDetails1.setId(1L);
        MaintainanceDetails maintainanceDetails2 = new MaintainanceDetails();
        maintainanceDetails2.setId(maintainanceDetails1.getId());
        assertThat(maintainanceDetails1).isEqualTo(maintainanceDetails2);
        maintainanceDetails2.setId(2L);
        assertThat(maintainanceDetails1).isNotEqualTo(maintainanceDetails2);
        maintainanceDetails1.setId(null);
        assertThat(maintainanceDetails1).isNotEqualTo(maintainanceDetails2);
    }
}
