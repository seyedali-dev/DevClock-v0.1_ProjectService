package com.seyed.ali.projectservice.service;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    private @InjectMocks ProjectServiceImpl projectService;
    private @Mock ProjectRepository projectRepository;

    private Project project;

    @BeforeEach
    public void setUp() {
        this.project = new Project();
        this.project.setProjectId("1");
        this.project.setProjectName("Test");
        this.project.setProjectDescription("Test");
    }

    @Test
    public void createProject() {
        // given
        when(this.projectRepository.save(isA(Project.class))).thenReturn(this.project);

        // when
        Project result = this.projectService.createProject(this.project);
        System.out.println(result);

        // then
        assertThat(result).isNotNull();

        verify(this.projectRepository, times(1)).save(isA(Project.class));
    }

}