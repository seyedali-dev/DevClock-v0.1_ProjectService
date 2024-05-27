package com.seyed.ali.projectservice.service;

import com.seyed.ali.projectservice.event.KafkaProducerEvent;
import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    private @InjectMocks ProjectServiceImpl projectService;
    private @Mock ProjectRepository projectRepository;
    private @Mock KafkaProducerEvent kafkaProducerEvent;

    private Project project;

    @BeforeEach
    public void setUp() {
        this.project = new Project();
        this.project.setProjectId("1");
        this.project.setProjectName("Test");
        this.project.setProjectDescription("Test");
    }

    @Test
    public void createProjectTest() {
        // given
        when(this.projectRepository.save(isA(Project.class))).thenReturn(this.project);

        // when
        Project result = this.projectService.createProject(this.project);
        System.out.println(result);

        // then
        assertThat(result).isNotNull();

        verify(this.projectRepository, times(1)).save(isA(Project.class));
    }

    @Test
    public void getProjectsTest() {
        // given
        when(this.projectRepository.findAll()).thenReturn(List.of(this.project));

        // when
        List<Project> result = this.projectService.getProjects();
        System.out.println(result);

        // then
        assertThat(result).isNotNull();

        verify(this.projectRepository, times(1)).findAll();
    }

    @Test
    public void getProjectById_Success() {
        // given
        when(this.projectRepository.findById(isA(String.class))).thenReturn(Optional.of(this.project));

        // when
        Project result = this.projectService.getProjectById(this.project.getProjectId());
        System.out.println(result);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProjectId()).isEqualTo(this.project.getProjectId());

        verify(this.projectRepository, times(1)).findById(isA(String.class));
    }

    @Test
    public void getProjectById_NotFound_Exception() {
        // given
        when(this.projectRepository.findById(isA(String.class))).thenReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> this.projectService.getProjectById(this.project.getProjectId()));
        System.out.println(thrown.getMessage());

        // then
        assertThat(thrown).isNotNull();
        assertThat(thrown).isInstanceOf(ResourceNotFoundException.class);
        assertThat(thrown.getMessage()).isEqualTo("Project with id 1 not found");

        verify(this.projectRepository, times(1)).findById(isA(String.class));
    }

    @Test
    public void updateProject() {
        // given
        Project update = new Project();
        update.setProjectName("Updated name.");
        when(this.projectRepository.findById(isA(String.class))).thenReturn(Optional.of(this.project));
        when(this.projectRepository.save(isA(Project.class))).thenReturn(update);

        // when
        Project result = this.projectService.updateProject(this.project.getProjectId(), update);
        System.out.println(result);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProjectId()).isEqualTo(update.getProjectId());

        verify(this.projectRepository, times(1)).findById(isA(String.class));
        verify(this.projectRepository, times(1)).save(isA(Project.class));
    }

    @Test
    public void deleteTimeEntryTest() {
        // Given
        String id = this.project.getProjectId();
        when(this.projectRepository.findById(id)).thenReturn(Optional.ofNullable(this.project));
        doNothing().when(this.projectRepository).delete(isA(Project.class));
        doNothing().when(this.kafkaProducerEvent).sendMessage(isA(Object.class));

        // When
        this.projectService.deleteProject(id);

        // Then
        verify(this.projectRepository, times(1)).findById(isA(String.class));
        verify(this.projectRepository, times(1)).delete(isA(Project.class));
        verify(this.kafkaProducerEvent, times(1)).sendMessage(isA(Object.class));
    }

}