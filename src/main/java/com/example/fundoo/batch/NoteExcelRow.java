package com.example.fundoo.batch;

import lombok.Data;

@Data
public class NoteExcelRow {
    private String title;
    private String description;
    private String ownerEmail;
    private int rowNumber;
}
