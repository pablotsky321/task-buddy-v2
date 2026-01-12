package com.dev.server.services;

import com.dev.server.DTOs.TaskDTO;
import com.dev.server.entities.TaskEntity;
import com.dev.server.entities.UserEntity;
import com.dev.server.enums.TaskState;
import com.dev.server.exceptions.BadTimeException;
import com.dev.server.repositories.TaskRepository;
import com.dev.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user(String email) throws ClassNotFoundException{
        return userRepository.findByEmail(email).orElseThrow(()->new ClassNotFoundException("Usaurio con este email no fue encontrado"));
    }

    private TaskEntity task(String id) throws ClassNotFoundException {
        return taskRepository.findById(UUID.fromString(id)).orElseThrow(()->new ClassNotFoundException("Task not found"));
    }

    private TaskDTO buildTaskDTO(TaskEntity task){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId().toString());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setState(task.getState());
        taskDTO.setCreatedAt(task.getCreatedAt());
        taskDTO.setUpdatedAt(task.getUpdatedAt());
        taskDTO.setFinishAt(task.getFinishAt());
        taskDTO.setCompletedAt(task.getCompletedAt());
        return taskDTO;
    }

    private List<TaskDTO> buildTaskDTOList(List<TaskEntity> taskEntities){
        List<TaskDTO> list = new ArrayList<>();
        for (TaskEntity task:taskEntities){
            list.add(buildTaskDTO(task));
        }
        return list;
    }

    public List<TaskDTO> myTasks() throws ClassNotFoundException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.buildTaskDTOList(this.taskRepository.findByUser(this.user(email)));
    }

    public TaskDTO createTask(TaskDTO taskDTO) throws ClassNotFoundException, BadTimeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = this.user(email);
        TaskEntity task = new TaskEntity();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        if(taskDTO.getFinishAt().compareTo(Instant.now()) == 0 || taskDTO.getFinishAt().compareTo(Instant.now()) < 0) throw new BadTimeException("finished_at must be greater than now");
        task.setFinishAt(taskDTO.getFinishAt());
        task.setUser(user);
        return buildTaskDTO(this.taskRepository.save(task));
    }

    public TaskDTO updateTask(TaskDTO taskDTO, String id) throws ClassNotFoundException, BadTimeException {
        TaskEntity task = task(id);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        if(taskDTO.getFinishAt().compareTo(task.getCreatedAt()) == 0 || taskDTO.getFinishAt().compareTo(task.getCreatedAt()) < 0) throw new BadTimeException("finished_at must be greater than now");
        task.setFinishAt(taskDTO.getFinishAt());
        return buildTaskDTO(this.taskRepository.save(task));
    }

    public TaskDTO taskDetails(String id) throws ClassNotFoundException {
        return this.buildTaskDTO(this.task(id));
    }

    public String deleteTask(String id) throws ClassNotFoundException {
        this.taskRepository.delete(task(id));
        return "task deleted";
    }

    public String completeTask(String id) throws ClassNotFoundException {
        TaskEntity task = task(id);
        task.setCompletedAt(Instant.now());
        task.setState(TaskState.completed);
        this.taskRepository.save(task);
        return "task completed";
    }

}
