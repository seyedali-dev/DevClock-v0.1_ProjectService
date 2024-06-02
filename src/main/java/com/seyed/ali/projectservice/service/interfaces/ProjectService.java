package com.seyed.ali.projectservice.service.interfaces;

import com.seyed.ali.projectservice.model.domain.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    List<Project> getProjects();

    Project getProjectById(String projectId);

    Project updateProject(String projectId, Project project);

    void deleteProjectAndAssociatedTasksAndTimeEntries(String projectId);

    void deleteProjectAndDetachFromTasksAndTimeEntries(String projectId);

}
