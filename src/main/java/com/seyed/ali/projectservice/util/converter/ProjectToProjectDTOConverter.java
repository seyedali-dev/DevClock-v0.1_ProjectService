package com.seyed.ali.projectservice.util.converter;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.service.interfaces.ProjectDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectToProjectDTOConverter implements Converter<Project, ProjectDTO> {

    @Override
    public ProjectDTO convert(Project source) {
        return new ProjectDTO(source.getProjectId(), source.getProjectName(), source.getProjectDescription());
    }

}
