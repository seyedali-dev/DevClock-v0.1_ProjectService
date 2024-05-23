package com.seyed.ali.projectservice.util.converter;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProjectDTOToProjectConverter implements Converter<ProjectDTO, Project> {

    @Override
    public Project convert(ProjectDTO source) {
        return new Project(source.getProjectId(), source.getProjectName(), source.getProjectDescription());
    }

}
