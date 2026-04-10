package com.example.fundoo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}
