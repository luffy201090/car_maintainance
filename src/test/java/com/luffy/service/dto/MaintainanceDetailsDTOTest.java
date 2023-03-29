package com.luffy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MaintainanceDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaintainanceDetailsDTO.class);
        MaintainanceDetailsDTO maintainanceDetailsDTO1 = new MaintainanceDetailsDTO();
        maintainanceDetailsDTO1.setId(1L);
        MaintainanceDetailsDTO maintainanceDetailsDTO2 = new MaintainanceDetailsDTO();
        assertThat(maintainanceDetailsDTO1).isNotEqualTo(maintainanceDetailsDTO2);
        maintainanceDetailsDTO2.setId(maintainanceDetailsDTO1.getId());
        assertThat(maintainanceDetailsDTO1).isEqualTo(maintainanceDetailsDTO2);
        maintainanceDetailsDTO2.setId(2L);
        assertThat(maintainanceDetailsDTO1).isNotEqualTo(maintainanceDetailsDTO2);
        maintainanceDetailsDTO1.setId(null);
        assertThat(maintainanceDetailsDTO1).isNotEqualTo(maintainanceDetailsDTO2);
    }
}
