package com.seyed.ali.projectservice.repository;

import com.seyed.ali.projectservice.model.domain.Project;
import jakarta.ws.rs.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("""
            SELECT p FROM Project p
            WHERE p.projectId=:identifier OR lower(p.projectName) = lower(:identifier)
            """)
    Optional<Project> findByProjectIdOrProjectName(@Param("identifier") String identifier);

}