package com.i2i.rgs.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private int code;
    private String errorMessage;
    private String status;

    public static ResponseEntity<ErrorResponse> setErrorResponse(String errorMessage, HttpStatusCode httpStatusCode) {
        return ResponseEntity.status(httpStatusCode).body(ErrorResponse.builder()
                .success(false)
                .code(httpStatusCode.value())
                .errorMessage(errorMessage)
                .status("ERROR")
                .build());
    }
}
