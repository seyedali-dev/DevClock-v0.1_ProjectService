package com.seyed.ali.projectservice.service.interfaces;

public interface ProjectClientService {

    /**
     * Checks whether the provided project is valid or not.
     *
     * @param projectId the project to check validation.
     * @return {@code true} if the project is valid,
     * otherwise return {@code false}.
     */
    boolean validateProjectsExistence(String projectId);

}
