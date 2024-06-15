package com.seyed.ali.projectservice.service;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.converter.ProjectDTOToProjectConverter;
import com.seyed.ali.projectservice.util.converter.ProjectToProjectDTOConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
public class ProjectClientServiceImplTest {

    private @InjectMocks ProjectClientServiceImpl projectClientService;
    private @Mock ProjectService projectService;
    private @Mock ProjectRepository projectRepository;

    @Test
    public void getProjectByNameOrIdTest() {
        // given
        String projectId = "1";
        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectName("Project 1");

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(projectId);
        projectDTO.setProjectName("Project 1");
        when(this.projectRepository.findByProjectIdOrProjectName(isA(String.class))).thenReturn(Optional.of(project));

        // when
        Project result = this.projectClientService.getProjectByNameOrId(projectId);
        System.out.println(result);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProjectId()).isEqualTo(projectId);
        assertThat(result.getProjectName()).isEqualTo(projectDTO.getProjectName());
    }

}