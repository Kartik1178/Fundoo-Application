package com.example.fundoo.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderEvent implements Serializable {
    private Long noteId;
    private Long userId;
    private String noteTitle;
    private String userEmail;
    private LocalDateTime reminderTime;
}
