package com.luffy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintainanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintainanceDTO.class);
        MaintainanceDTO maintainanceDTO1 = new MaintainanceDTO();
        maintainanceDTO1.setId(1L);
        MaintainanceDTO maintainanceDTO2 = new MaintainanceDTO();
        assertThat(maintainanceDTO1).isNotEqualTo(maintainanceDTO2);
        maintainanceDTO2.setId(maintainanceDTO1.getId());
        assertThat(maintainanceDTO1).isEqualTo(maintainanceDTO2);
        maintainanceDTO2.setId(2L);
        assertThat(maintainanceDTO1).isNotEqualTo(maintainanceDTO2);
        maintainanceDTO1.setId(null);
        assertThat(maintainanceDTO1).isNotEqualTo(maintainanceDTO2);
    }
}
