package com.seyed.ali.projectservice.controller;

import com.seyed.ali.projectservice.model.payload.ProjectDTO;
import com.seyed.ali.projectservice.model.payload.Result;
import com.seyed.ali.projectservice.service.interfaces.ProjectClientService;
import com.seyed.ali.projectservice.util.converter.ProjectConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

/**
 * This controller is used for microservice intercommunication.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project/client")
@SecurityRequirement(name = "Keycloak")
@Tag(name = "Project Client", description = "API for intercommunication of microservices.")
public class ProjectClientController {

    private final ProjectClientService projectClientService;
    private final ProjectConverter projectConverter;

    @GetMapping("/{projectId}")
    @Operation(summary = "Validate Project Existence", description = "Validates whether the specified `projectID` is valid or not.", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Result.class))
            )
    })
    public Result validateProjectsExistence(@PathVariable("projectId") String projectId) {
        return new Result(
                true,
                OK,
                "The project with id " + projectId + " exists and is valid.",
                this.projectClientService.validateProjectsExistence(projectId)
        );
    }

    @GetMapping("/projects")
    @Operation(summary = "Get Project By Criteria", description = "Fetches the project based on the provided criteria(ProjectID or ProjectName)", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ProjectDTO.class))
            )
    })
    public ProjectDTO getProjectByNameOrId(@RequestParam("identifier") String identifier) {
        return this.projectConverter.convertToProjectDTO(this.projectClientService.getProjectByNameOrId(identifier));
    }

}
