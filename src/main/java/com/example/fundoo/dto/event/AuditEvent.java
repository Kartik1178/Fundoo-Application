package com.example.fundoo.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEvent implements Serializable {
    private Long userId;
    private String action;
    private String resourceType;
    private Long resourceId;
    private LocalDateTime timestamp;
}
