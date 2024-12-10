package com.i2i.rgs.controller;

import com.i2i.rgs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUsersToCsv() {
        try {
            byte[] csvData = userService.exportUsersToCsv();

            // Set the headers for the CSV file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=users.csv");
            headers.add("Content-Type", "text/csv");

            // Return the CSV file as a byte array
            return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
