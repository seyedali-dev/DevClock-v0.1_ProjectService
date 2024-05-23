package com.seyed.ali.projectservice.client;

import com.seyed.ali.projectservice.exceptions.ResourceNotFoundException;
import com.seyed.ali.projectservice.model.enums.TaskStatus;
import com.seyed.ali.projectservice.model.payload.Result;
import com.seyed.ali.projectservice.model.payload.TaskDTO;
import com.seyed.ali.projectservice.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * This class is responsible for communicating with the Task Service.
 */
@SuppressWarnings("FieldCanBeLocal")
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskServiceClient {

    private final String taskServiceBaseURL = "http://localhost:8084/api/v1"; // TODO: remember to change the host and port when dockerizing the application

    private final RestClient restClient;
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    /**
     * Retrieves all tasks for a given project.
     *
     * @param projectId The ID of the project.
     * @return A list of TaskDTO objects representing the tasks for the project.
     */
    public List<TaskDTO> findAllTasksForProject(String projectId) {
        // Extract the JWT from the security context
        String jwt = this.keycloakSecurityUtil.extractTokenFromSecurityContext();

        // Make the HTTP request
        ResponseEntity<Result> result = makeHttpCallToTaskService(projectId, jwt);

        // Convert the response into a list of TaskDTO objects
        return this.extractTaskDTOListFromResponse(Objects.requireNonNull(result));
    }

    /**
     * Makes a HTTP Rest call to `Task-Service`, to find all the tasks associated with the project.
     *
     * @param projectId The ID of the project.
     * @param jwt       The JWT token.
     * @return A Response Entity of Result object.
     * @throws ResourceNotFoundException If the project is not found.
     */
    private ResponseEntity<Result> makeHttpCallToTaskService(String projectId, String jwt) {
        return this.restClient
                .get().uri(this.taskServiceBaseURL + "/task/" + projectId)
                .header("Authorization", "Bearer " + jwt)
                .retrieve()
                .onStatus(NOT_FOUND::equals,
                        (request, response) -> {
                            throw new ResourceNotFoundException("Project with ProjectID '" + projectId + "' not found");
                        })
                .toEntity(Result.class);
    }

    /**
     * Converts the response from the Task Service into a list of TaskDTO objects.
     *
     * @param response The response from the Task Service.
     * @return A list of TaskDTO objects.
     */
    @SuppressWarnings("unchecked")
    private List<TaskDTO> extractTaskDTOListFromResponse(ResponseEntity<Result> response) {
        List<TaskDTO> taskDTOList = new ArrayList<>();
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) Objects.requireNonNull(response.getBody()).getData();

        // Iterate over the dataList and create TaskDTO objects
        for (Map<String, Object> data : dataList) {
            // Extract task fields from the map
            String taskId = (String) data.get("taskId");
            String taskName = (String) data.get("taskName");
            String taskDescription = (String) data.get("taskDescription");
            TaskStatus taskStatus = TaskStatus.valueOf((String) data.get("taskStatus")); // Assuming TaskStatus is an enum
            Integer taskOrder = (Integer) data.get("taskOrder");
            String projectId = (String) data.get("projectId");

            // Create a new TaskDTO object
            TaskDTO taskDTO = new TaskDTO(taskId, taskName, taskDescription, taskStatus, taskOrder, projectId);

            // Add the TaskDTO object to the list
            taskDTOList.add(taskDTO);
        }

        return taskDTOList;
    }

}
