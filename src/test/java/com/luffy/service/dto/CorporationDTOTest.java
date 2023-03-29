package com.luffy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.luffy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorporationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorporationDTO.class);
        CorporationDTO corporationDTO1 = new CorporationDTO();
        corporationDTO1.setId(1L);
        CorporationDTO corporationDTO2 = new CorporationDTO();
        assertThat(corporationDTO1).isNotEqualTo(corporationDTO2);
        corporationDTO2.setId(corporationDTO1.getId());
        assertThat(corporationDTO1).isEqualTo(corporationDTO2);
        corporationDTO2.setId(2L);
        assertThat(corporationDTO1).isNotEqualTo(corporationDTO2);
        corporationDTO1.setId(null);
        assertThat(corporationDTO1).isNotEqualTo(corporationDTO2);
    }
}
