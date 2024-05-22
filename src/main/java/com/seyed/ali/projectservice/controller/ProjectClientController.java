package com.seyed.ali.projectservice.controller;

import com.seyed.ali.projectservice.model.payload.Result;
import com.seyed.ali.projectservice.service.interfaces.ProjectClientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

/**
 * This controller is used for microservice intercommunication.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/client")
@SecurityRequirement(name = "Keycloak")
public class ProjectClientController {

    private final ProjectClientService projectClientService;

    @GetMapping("/{projectId}")
    public Result validateProjectsExistence(@PathVariable("projectId") String projectId) {
        return new Result(
                true,
                OK,
                "The project with id " + projectId + " exists and is valid.",
                this.projectClientService.validateProjectsExistence(projectId)
        );
    }

}
