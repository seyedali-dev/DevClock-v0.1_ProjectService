package com.seyed.ali.projectservice.controller;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.model.payload.Result;
import com.seyed.ali.projectservice.service.interfaces.ProjectDTO;
import com.seyed.ali.projectservice.service.interfaces.ProjectService;
import com.seyed.ali.projectservice.util.converter.ProjectDTOToProjectConverter;
import com.seyed.ali.projectservice.util.converter.ProjectToProjectDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectDTOToProjectConverter projectDTOToProjectConverter;
    private final ProjectToProjectDTOConverter projectToProjectDTOConverter;

    @PostMapping
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

}
