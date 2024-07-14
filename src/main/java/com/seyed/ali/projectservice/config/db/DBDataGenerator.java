package com.seyed.ali.projectservice.config.db;

import com.seyed.ali.projectservice.model.domain.Project;
import com.seyed.ali.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DBDataGenerator {

    private final ProjectRepository projectRepository;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            this.projectRepository.saveAll(this.generateProjects(2));
        };
    }

    public List<Project> generateProjects(int count) {
        List<Project> projects = new ArrayList<>();
        int idCounter = 0;
        for (int i = 0; i < count; i++) {
            Project project = new Project();
            project.setProjectId("" + ++idCounter);
            project.setProjectName("Project " + (i + 1));
            project.setProjectDescription("Description for Project " + (i + 1));
            projects.add(project);
        }
        return projects;
    }

}
