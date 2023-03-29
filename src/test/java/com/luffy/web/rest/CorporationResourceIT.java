package com.luffy.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luffy.IntegrationTest;
import com.luffy.domain.Corporation;
import com.luffy.repository.CorporationRepository;
import com.luffy.service.criteria.CorporationCriteria;
import com.luffy.service.dto.CorporationDTO;
import com.luffy.service.mapper.CorporationMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CorporationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorporationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corporations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CorporationRepository corporationRepository;

    @Autowired
    private CorporationMapper corporationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorporationMockMvc;

    private Corporation corporation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporation createEntity(EntityManager em) {
        Corporation corporation = new Corporation().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return corporation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corporation createUpdatedEntity(EntityManager em) {
        Corporation corporation = new Corporation().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return corporation;
    }

    @BeforeEach
    public void initTest() {
        corporation = createEntity(em);
    }

    @Test
    @Transactional
    void createCorporation() throws Exception {
        int databaseSizeBeforeCreate = corporationRepository.findAll().size();
        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);
        restCorporationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeCreate + 1);
        Corporation testCorporation = corporationList.get(corporationList.size() - 1);
        assertThat(testCorporation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCorporation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCorporationWithExistingId() throws Exception {
        // Create the Corporation with an existing ID
        corporation.setId(1L);
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        int databaseSizeBeforeCreate = corporationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorporationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = corporationRepository.findAll().size();
        // set the field null
        corporation.setName(null);

        // Create the Corporation, which fails.
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        restCorporationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorporations() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCorporation() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get the corporation
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL_ID, corporation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(corporation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getCorporationsByIdFiltering() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        Long id = corporation.getId();

        defaultCorporationShouldBeFound("id.equals=" + id);
        defaultCorporationShouldNotBeFound("id.notEquals=" + id);

        defaultCorporationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCorporationShouldNotBeFound("id.greaterThan=" + id);

        defaultCorporationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCorporationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorporationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where name equals to DEFAULT_NAME
        defaultCorporationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the corporationList where name equals to UPDATED_NAME
        defaultCorporationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCorporationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the corporationList where name equals to UPDATED_NAME
        defaultCorporationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where name is not null
        defaultCorporationShouldBeFound("name.specified=true");

        // Get all the corporationList where name is null
        defaultCorporationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporationsByNameContainsSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where name contains DEFAULT_NAME
        defaultCorporationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the corporationList where name contains UPDATED_NAME
        defaultCorporationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where name does not contain DEFAULT_NAME
        defaultCorporationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the corporationList where name does not contain UPDATED_NAME
        defaultCorporationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCorporationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where description equals to DEFAULT_DESCRIPTION
        defaultCorporationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the corporationList where description equals to UPDATED_DESCRIPTION
        defaultCorporationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCorporationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the corporationList where description equals to UPDATED_DESCRIPTION
        defaultCorporationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where description is not null
        defaultCorporationShouldBeFound("description.specified=true");

        // Get all the corporationList where description is null
        defaultCorporationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCorporationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where description contains DEFAULT_DESCRIPTION
        defaultCorporationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the corporationList where description contains UPDATED_DESCRIPTION
        defaultCorporationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCorporationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        // Get all the corporationList where description does not contain DEFAULT_DESCRIPTION
        defaultCorporationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the corporationList where description does not contain UPDATED_DESCRIPTION
        defaultCorporationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorporationShouldBeFound(String filter) throws Exception {
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corporation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorporationShouldNotBeFound(String filter) throws Exception {
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorporationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorporation() throws Exception {
        // Get the corporation
        restCorporationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorporation() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();

        // Update the corporation
        Corporation updatedCorporation = corporationRepository.findById(corporation.getId()).get();
        // Disconnect from session so that the updates on updatedCorporation are not directly saved in db
        em.detach(updatedCorporation);
        updatedCorporation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CorporationDTO corporationDTO = corporationMapper.toDto(updatedCorporation);

        restCorporationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corporationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
        Corporation testCorporation = corporationList.get(corporationList.size() - 1);
        assertThat(testCorporation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCorporation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, corporationDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorporationWithPatch() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();

        // Update the corporation using partial update
        Corporation partialUpdatedCorporation = new Corporation();
        partialUpdatedCorporation.setId(corporation.getId());

        partialUpdatedCorporation.description(UPDATED_DESCRIPTION);

        restCorporationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporation))
            )
            .andExpect(status().isOk());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
        Corporation testCorporation = corporationList.get(corporationList.size() - 1);
        assertThat(testCorporation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCorporation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCorporationWithPatch() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();

        // Update the corporation using partial update
        Corporation partialUpdatedCorporation = new Corporation();
        partialUpdatedCorporation.setId(corporation.getId());

        partialUpdatedCorporation.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restCorporationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorporation.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorporation))
            )
            .andExpect(status().isOk());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
        Corporation testCorporation = corporationList.get(corporationList.size() - 1);
        assertThat(testCorporation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCorporation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, corporationDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorporation() throws Exception {
        int databaseSizeBeforeUpdate = corporationRepository.findAll().size();
        corporation.setId(count.incrementAndGet());

        // Create the Corporation
        CorporationDTO corporationDTO = corporationMapper.toDto(corporation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorporationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(corporationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Corporation in the database
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorporation() throws Exception {
        // Initialize the database
        corporationRepository.saveAndFlush(corporation);

        int databaseSizeBeforeDelete = corporationRepository.findAll().size();

        // Delete the corporation
        restCorporationMockMvc
            .perform(delete(ENTITY_API_URL_ID, corporation.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Corporation> corporationList = corporationRepository.findAll();
        assertThat(corporationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
