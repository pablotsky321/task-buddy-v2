package com.dev.server.repositories;

import com.dev.server.entities.TaskEntity;
import com.dev.server.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findByUser(UserEntity user);

}
