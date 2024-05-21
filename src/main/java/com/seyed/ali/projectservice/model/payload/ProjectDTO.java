package com.seyed.ali.projectservice.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DTO for {@link com.seyed.ali.projectservice.model.domain.Project}
 */
public record ProjectDTO(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Unique identifier for the project", example = "12345")
        String projectId,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "The project name", example = "Microservices-Springboot")
        String projectName,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "The project name", example = "Learning microservices is really exciting and HARD ;)")
        String projectDescription
) implements Serializable {
}