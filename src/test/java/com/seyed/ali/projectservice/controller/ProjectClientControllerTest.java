package com.seyed.ali.projectservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyed.ali.projectservice.client.TaskServiceClient;
import com.seyed.ali.projectservice.config.EurekaClientTestConfiguration;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import com.seyed.ali.projectservice.service.interfaces.ProjectClientService;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.KeycloakSecurityUtil;
import com.seyed.ali.projectservice.util.converter.ProjectConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@WebMvcTest(ProjectClientController.class) /* since this is not in integration test, rather controller unit test */
@EnableConfigurationProperties /* to use application.yml-test file */
@ActiveProfiles("test")
@AutoConfigureMockMvc/* calling the api itself */
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
class ProjectClientControllerTest {

    private final String baseUrl = "/api/v1/project/client";
    private @MockBean ProjectClientService projectClientService;
    private @MockBean ProjectConverter projectConverter;
    private @MockBean KeycloakSecurityUtil KeycloakSecurityUtil;

    private @Autowired ObjectMapper objectMapper;
    private @Autowired MockMvc mockMvc;

    @Test
    void validateProjectsExistenceTest() {
        // given

        // when

        // then
    }

    @Test
    void getProjectByNameOrIdTest() throws Exception {
        // given
        String projectId = "1";
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectId);
        projectDTO.setProjectName("Project 1");
        when(this.projectConverter.convertToProjectDTO(isA(Project.class))).thenReturn(projectDTO);

        // when
        ResultActions response = this.mockMvc.perform(
                get(this.baseUrl + "/projects?identifier=" + projectId)
                        .accept(APPLICATION_JSON)
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
        );

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId", is(projectDTO.getProjectId())))
                .andExpect(jsonPath("$.projectName", is(projectDTO.getProjectName())))
        ;
    }

}