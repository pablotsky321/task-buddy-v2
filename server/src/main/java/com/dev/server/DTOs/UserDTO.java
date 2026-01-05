package com.dev.server.DTOs;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String id;

    private String completeName;

    @NotBlank(message = "This field must not be empty")
    private String email;

    @NotBlank(message = "This field must not be empty")
    private String password;

}
