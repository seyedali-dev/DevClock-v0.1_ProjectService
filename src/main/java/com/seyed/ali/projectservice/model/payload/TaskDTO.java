package com.seyed.ali.projectservice.model.payload;

import com.seyed.ali.projectservice.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record TaskDTO(

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Unique identifier for the task", example = "12345")
        String taskId,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "The task name", example = "Learn Security in Microservices")
        String taskName,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "The task description", example = "We can use keycloak for ease of use in microservice security!")
        String taskDescription,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "The status of task", example = "COMPLETED | IN_PROGRESS | DROPPED | PLANNED_FOR_FUTURE")
        TaskStatus taskStatus,

        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "The order of the task", example = "1")
        Integer taskOrder,

        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Unique identifier for the project that the task needs to be associated with", example = "12345")
        String projectId

) implements Serializable {
}