package com.seyed.ali.projectservice.repository;

import com.seyed.ali.projectservice.model.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {
}