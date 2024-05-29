package com.seyed.ali.projectservice.event;

import com.seyed.ali.projectservice.model.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEvent {

    private String message;
    private Project project;

}
