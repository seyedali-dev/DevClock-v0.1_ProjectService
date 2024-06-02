package com.seyed.ali.projectservice.controller;

import com.seyed.ali.projectservice.client.TaskServiceClient;
import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import com.seyed.ali.projectservice.model.payload.Result;
import com.seyed.ali.projectservice.model.payload.TaskDTO;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.converter.ProjectConverter;
import com.seyed.ali.projectservice.util.converter.ProjectDTOToProjectConverter;
import com.seyed.ali.projectservice.util.converter.ProjectToProjectDTOConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Project", description = "API for project operation.")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectDTOToProjectConverter projectDTOToProjectConverter;
    private final ProjectToProjectDTOConverter projectToProjectDTOConverter;
    private final ProjectConverter projectConverter;
    private final TaskServiceClient taskServiceClient;

    @PostMapping
    @Operation(summary = "Create project", description = "Create a project.")
    public ResponseEntity<Result> createProject(@RequestBody ProjectDTO projectDTO) {
        Project project = this.projectDTOToProjectConverter.convert(projectDTO);
        Project createdProject = this.projectService.createProject(project);
        ProjectDTO response = this.projectToProjectDTOConverter.convert(createdProject);

        return ResponseEntity.status(CREATED).body(
                new Result(
                        true,
                        CREATED,
                        "Project created successfully.",
                        response
                ));
    }

    @GetMapping
    @Operation(summary = "Get all projects", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectDTO.class)))
            )
    })
    public ResponseEntity<Result> getProjects() {
        List<ProjectDTO> projectDTOList = this.projectConverter.convertToProjectDTOList(this.projectService.getProjects())
                .stream()
                .peek(projectDTO -> {
                    List<TaskDTO> tasksForProject = this.taskServiceClient.findAllTasksForProject(projectDTO.getProjectId());
                    tasksForProject.forEach(taskDTO -> projectDTO.getTaskDTO().add(taskDTO));
                })
                .toList();

        return ResponseEntity.ok(new Result(
                true,
                OK,
                "List of projects.",
                projectDTOList
        ));
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get a specific project", description = "Fetches a specific project from the database")
    public ResponseEntity<Result> getSpecificProject(@PathVariable String projectId) {
        ProjectDTO projectDTO = this.projectConverter.convertToProjectDTO(this.projectService.getProjectById(projectId));
        return ResponseEntity.ok(new Result(
                true,
                OK,
                "Project retrieved successfully. Project ID: '" + projectId + "'.",
                projectDTO
        ));
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update a project", description = "Updates a specific project in the database")
    public ResponseEntity<Result> updateProject(@Valid @PathVariable String projectId, @RequestBody ProjectDTO projectDTO) {
        Project project = this.projectDTOToProjectConverter.convert(projectDTO);
        Project updatedProject = this.projectService.updateProject(projectId, project);
        ProjectDTO response = this.projectToProjectDTOConverter.convert(updatedProject);

        return ResponseEntity.ok(new Result(
                true,
                OK,
                "Project for Project ID: -> " + projectId + " <- updated successfully.",
                response
        ));
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete a project, tasks & time entries related.", description = "Deletes a specific project from the database and the associated tasks and time entries.")
    public ResponseEntity<Result> deleteProjectAndAssociatedTasksAndTimeEntries(@PathVariable String projectId) {
        this.projectService.deleteProjectAndAssociatedTasksAndTimeEntries(projectId);
        return ResponseEntity.status(NO_CONTENT).body(new Result(
                true,
                NO_CONTENT,
                "Project -> " + projectId + " <-- deleted successfully."
        ));
    }

    @DeleteMapping("/detach/{projectId}")
    @Operation(summary = "Delete & Detach a project from tasks and time entries", description = "Delete & Detaches a specific project from the associated tasks and time entries.")
    public ResponseEntity<Result> deleteAndDetachProject(@PathVariable String projectId) {
        this.projectService.deleteProjectAndDetachFromTasksAndTimeEntries(projectId);
        return ResponseEntity.status(NO_CONTENT).body(new Result(
                true,
                NO_CONTENT,
                "Project -> " + projectId + " <-- deleted successfully."
        ));
    }

}
