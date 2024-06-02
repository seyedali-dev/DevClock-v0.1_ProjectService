package com.seyed.ali.projectservice.service;

import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.event.ProjectEventProducer;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.converter.ProjectConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectEventProducer projectEventProducer;
    private final ProjectConverter projectConverter;

    @Override
    public Project createProject(Project project) {
        project.setProjectId(UUID.randomUUID().toString());
        return this.projectRepository.save(project);
    }

    @Override
    public List<Project> getProjects() {
        return this.projectRepository.findAll();
    }

    @Override
    public Project getProjectById(String projectId) {
        return this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + projectId + " not found"));
    }

    @Override
    public Project updateProject(String projectId, Project project) {
        Project foundProject = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project with id " + projectId + " not found"));

        if (project.getProjectName() != null)
            foundProject.setProjectName(project.getProjectName());
        if (project.getProjectDescription() != null)
            foundProject.setProjectDescription(project.getProjectDescription());

        return this.projectRepository.save(foundProject);
    }

    @Override
    public void deleteProject(String projectId) {
        Project project = this.getProjectById(projectId);
        this.projectEventProducer.sendMessage(this.projectConverter.convertToProjectDTO(project));
        this.projectRepository.delete(project);
    }

}
