package com.seyed.ali.projectservice.service;

import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import com.seyed.ali.projectservice.service.interfaces.ProjectClientService;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectClientServiceImpl implements ProjectClientService {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateProjectsExistence(String projectId) {
        Project project = this.projectService.getProjectById(projectId);
        return project != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project getProjectByNameOrId(String identifier) throws ResourceNotFoundException {
        return this.projectRepository.findByProjectIdOrProjectName(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with the provided identifier: " + identifier));
    }

}
