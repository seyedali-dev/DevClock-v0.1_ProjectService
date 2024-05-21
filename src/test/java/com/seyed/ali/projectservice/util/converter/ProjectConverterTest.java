package com.seyed.ali.projectservice.util.converter;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@SuppressWarnings("FieldCanBeLocal")
@ExtendWith(MockitoExtension.class)
class ProjectConverterTest {

    private @InjectMocks ProjectConverter projectConverter;
    private @Mock ProjectToProjectDTOConverter projectToProjectDTOConverter;

    private Project project;
    private ProjectDTO projectDTO;
    private List<Project> projectList;

    @BeforeEach
    void setUp() {
        this.project = new Project();
        this.project.setProjectId("1");
        this.project.setProjectName("Test");
        this.project.setProjectDescription("Test");

        this.projectDTO = new ProjectDTO(this.project.getProjectId(), this.project.getProjectName(), this.project.getProjectDescription());

        this.projectList = new ArrayList<>();
        this.projectList.add(this.project);
    }

    @Test
    void convertToProjectDTOList() {
        // given
        when(this.projectToProjectDTOConverter.convert(isA(Project.class))).thenReturn(this.projectDTO);

        // when
        List<ProjectDTO> result = this.projectConverter.convertToProjectDTOList(this.projectList);
        System.out.println(result);

        // then
        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

}