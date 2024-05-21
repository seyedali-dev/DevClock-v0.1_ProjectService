package com.seyed.ali.projectservice.service.interfaces;

import java.io.Serializable;

/**
 * DTO for {@link com.seyed.ali.projectservice.model.domain.Project}
 */
public record ProjectDTO(String projectId,
                         String projectName,
                         String projectDescription) implements Serializable {
}