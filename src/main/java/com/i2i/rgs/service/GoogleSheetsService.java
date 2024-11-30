package com.i2i.rgs.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

//@Service
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "ReportGenie";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final Sheets sheetsService;

    public GoogleSheetsService() throws GeneralSecurityException, IOException {
        GoogleCredential credential = GoogleCredential.fromStream(
                        new FileInputStream("src/main/resources/credentials.json"))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        this.sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String createNewSheetAndWriteData(String spreadsheetTitle, List<List<Object>> data) throws IOException {
        // Create a new spreadsheet
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle(spreadsheetTitle));
        Spreadsheet result = sheetsService.spreadsheets().create(spreadsheet).execute();
        String spreadsheetId = result.getSpreadsheetId();

        // Write data to the sheet
        ValueRange body = new ValueRange().setValues(data);
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, "Sheet1", body)
                .setValueInputOption("RAW")
                .execute();

        return spreadsheetId;
    }
}