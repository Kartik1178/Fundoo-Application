package com.example.fundoo.batch;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoteExcelItemReader implements ItemReader<NoteExcelRow> {

    private static final Logger log = LoggerFactory.getLogger(NoteExcelItemReader.class);

    private final String filePath;
    private List<NoteExcelRow> rows;
    private int currentIndex = 0;

    public NoteExcelItemReader(String filePath) {
        this.filePath = filePath;
    }

    private void initialize() throws Exception {
        rows = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();

        int rowNum = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (rowNum == 0) { rowNum++; continue; } // skip header

            NoteExcelRow noteRow = new NoteExcelRow();
            noteRow.setRowNumber(rowNum);
            noteRow.setTitle(getCellValue(row, 0));
            noteRow.setDescription(getCellValue(row, 1));
            noteRow.setOwnerEmail(getCellValue(row, 2));
            rows.add(noteRow);
            rowNum++;
        }
        workbook.close();
        fis.close();
        log.info("Excel reader loaded {} rows from {}", rows.size(), filePath);
    }

    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    @Override
    public NoteExcelRow read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (rows == null) initialize();
        if (currentIndex < rows.size()) return rows.get(currentIndex++);
        return null;
    }
}
