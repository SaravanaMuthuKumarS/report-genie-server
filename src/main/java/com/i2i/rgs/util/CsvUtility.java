package com.i2i.rgs.util;

import com.i2i.rgs.dto.EmployeeTimesheetResponseDto;
import com.i2i.rgs.helper.RGSException;
import com.i2i.rgs.model.User;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtility {

    // Method to convert CSV InputStream to a List of User objects
    public static List<User> csvToUserList(InputStream is) {
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(bReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<User> userList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                User user = new User();
                user.setId(String.valueOf(Integer.parseInt(csvRecord.get("ID"))));
                user.setName(csvRecord.get("Name"));
                user.setEmail(csvRecord.get("Email"));
                userList.add(user);
            }

            return userList;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }
    }

    // Method to write a list of User objects to CSV
    public static void writeUsersToCsv(List<User> users, OutputStream outputStream) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Name", "Email"))) {
            for (User user : users) {
                csvPrinter.printRecord(user.getId(), user.getName(), user.getEmail());
            }
            csvPrinter.flush();
        }
    }

    public static void writeReportToCsv(List<EmployeeTimesheetResponseDto> timesheetReport, ByteArrayOutputStream outputStream) {
        try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "Name", "Billable", "NonBillable", "Leaves", "TotalHours"))) {
            for (EmployeeTimesheetResponseDto report : timesheetReport) {
                csvPrinter.printRecord(report.getId(), report.getName(), report.getBillable(), report.getNonBillable(), report.getLeaves(), report.getTotalHours());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            throw new RGSException("Error creating CSV");
        }
    }
}