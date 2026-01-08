package com.dev.server.controllers;

import com.dev.server.DTOs.TaskDTO;
import com.dev.server.exceptions.BadTimeException;
import com.dev.server.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/mytasks")
    public ResponseEntity<?> myTasks(){
        try{
            return new ResponseEntity<>(taskService.myTasks(), HttpStatus.ACCEPTED);
        }catch (ClassNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("There was an unexpected error", HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskDTO taskDTO, BindingResult result){
        try{
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(
                        result.getAllErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .toList()
                );
            }
            return new ResponseEntity<>(taskService.createTask(taskDTO), HttpStatus.ACCEPTED);
        }catch (ClassNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (BadTimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>("There was an unexpected error", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@RequestBody @Valid TaskDTO taskDTO, @PathVariable("id") String id ,BindingResult result){
        try{
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(
                        result.getAllErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .toList()
                );
            }
            return new ResponseEntity<>(taskService.updateTask(taskDTO, id), HttpStatus.ACCEPTED);
        }catch (ClassNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (BadTimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>("There was an unexpected error", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") String id){
        try{
            return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.ACCEPTED);
        }catch (ClassNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("There was an unexpected error", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> taskDetails(@PathVariable("id") String id){
        try{
            return new ResponseEntity<>(taskService.taskDetails(id), HttpStatus.ACCEPTED);
        }catch (ClassNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("There was an unexpected error", HttpStatus.BAD_REQUEST);
        }
    }


}
