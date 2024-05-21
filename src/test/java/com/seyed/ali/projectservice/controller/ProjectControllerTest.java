package com.seyed.ali.projectservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seyed.ali.projectservice.config.EurekaClientTestConfiguration;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.service.interfaces.ProjectDTO;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.converter.KeycloakJwtAuthorityConverter;
import com.seyed.ali.projectservice.util.converter.ProjectDTOToProjectConverter;
import com.seyed.ali.projectservice.util.converter.ProjectToProjectDTOConverter;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@WebMvcTest(ProjectController.class) /* since this is not in integration test, rather controller unit test */
@EnableConfigurationProperties /* to use application.yml-test file */
@ActiveProfiles("test")
@AutoConfigureMockMvc/* calling the api itself */
@ContextConfiguration(classes = {EurekaClientTestConfiguration.class}) /* to call the configuration in the test (for service-registry configs) */
class ProjectControllerTest {

    private final String baseUrl = "/api/v1/project";
    private @MockBean ProjectService projectService;
    private @MockBean ProjectDTOToProjectConverter projectDTOToProjectConverter;
    private @MockBean ProjectToProjectDTOConverter projectToProjectDTOConverter;
    private @MockBean KeycloakJwtAuthorityConverter keycloakJwtAuthorityConverter;
    private @Autowired ObjectMapper objectMapper;
    private @Autowired MockMvc mockMvc;
    private Project project;
    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp() {
        this.project = new Project();
        this.project.setProjectId("1");
        this.project.setProjectName("Test Project");
        this.project.setProjectDescription("Test Description");

        this.projectDTO = new ProjectDTO(this.project.getProjectId(), this.project.getProjectName(), this.project.getProjectDescription());
    }

    @Test
    public void createProjectTest() throws Exception {
        // given
        when(this.projectDTOToProjectConverter.convert(isA(ProjectDTO.class))).thenReturn(this.project);
        when(this.projectService.createProject(isA(Project.class))).thenReturn(this.project);
        when(this.projectToProjectDTOConverter.convert(isA(Project.class))).thenReturn(this.projectDTO);

        String json = this.objectMapper.writeValueAsString(this.projectDTO);

        // when
        ResultActions response = this.mockMvc.perform(
                post(this.baseUrl)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .with(jwt().authorities(new SimpleGrantedAuthority("some_authority")))
                        .content(json)
        );

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag", is(true)))
                .andExpect(jsonPath("$.httpStatus", is("CREATED")))
                .andExpect(jsonPath("$.message", is("Project created successfully.")))
                .andExpect(jsonPath("$.data.projectId", is(this.projectDTO.projectId())))
                .andExpect(jsonPath("$.data.projectName", is(this.projectDTO.projectName())))
                .andExpect(jsonPath("$.data.projectDescription", is(this.projectDTO.projectDescription())))
        ;
    }

}