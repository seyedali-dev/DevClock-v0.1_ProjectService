package com.seyed.ali.projectservice.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    private String projectId;

    private String projectName;
    private String projectDescription;

}
