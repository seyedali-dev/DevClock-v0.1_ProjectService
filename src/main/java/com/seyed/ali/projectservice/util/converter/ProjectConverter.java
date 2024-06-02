package com.seyed.ali.projectservice.util.converter;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectConverter {

    private final ProjectToProjectDTOConverter projectToProjectDTOConverter;

    public List<ProjectDTO> convertToProjectDTOList(List<Project> projects) {
        List<ProjectDTO> projectDTOList = new ArrayList<>();
        for (Project project : projects) {
            ProjectDTO projectDTO = this.projectToProjectDTOConverter.convert(project);
            projectDTOList.add(projectDTO);
        }
        return projectDTOList;
    }

    public ProjectDTO convertToProjectDTO(Project projectId) {
        return this.projectToProjectDTOConverter.convert(projectId);
    }

}
