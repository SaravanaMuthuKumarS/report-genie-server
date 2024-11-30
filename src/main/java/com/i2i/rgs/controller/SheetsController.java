package com.i2i.rgs.controller;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.i2i.rgs.service.GoogleSheetsService;

import lombok.RequiredArgsConstructor;

//@RestController
@RequestMapping("/sheets")
@RequiredArgsConstructor
public class SheetsController {
    private final GoogleSheetsService googleSheetsService;


    @PostMapping("/create")
    public String createSheetAndWriteData(@RequestBody List<List<Object>> data) {
        try {
            String spreadsheetId = googleSheetsService.createNewSheetAndWriteData("New Spreadsheet", data);
            return "Spreadsheet created successfully with ID: " + spreadsheetId;
        } catch (Exception e) {
            return "Error creating spreadsheet: " + e.getMessage();
        }
    }
}
