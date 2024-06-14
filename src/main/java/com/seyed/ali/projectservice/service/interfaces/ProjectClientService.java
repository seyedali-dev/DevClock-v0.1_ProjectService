package com.seyed.ali.projectservice.service.interfaces;

import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.model.domain.Project;

public interface ProjectClientService {

    /**
     * Checks whether the provided project is valid or not.
     *
     * @param projectId the project to check validation.
     * @return {@code true} if the project is valid,
     * otherwise return {@code false}.
     */
    boolean validateProjectsExistence(String projectId);

    /**
     * Fetches the project based on the provided criteria(ProjectID or ProjectName).
     *
     * @param identifier Either "ProjectID" or "ProjectName"
     * @return Found ProjectDTO object.
     * @throws ResourceNotFoundException If the project is not found.
     */
    Project getProjectByNameOrId(String identifier) throws ResourceNotFoundException;

}
