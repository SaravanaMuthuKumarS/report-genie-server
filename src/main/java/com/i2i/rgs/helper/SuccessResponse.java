package com.i2i.rgs.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private boolean success;
    private int code;
    private String message;
    private String status;
    private Object response;

    public static ResponseEntity<SuccessResponse> setSuccessResponseOk(String successMessage, Object object) {
        return setSuccessResponse(successMessage, HttpStatus.OK, object);
    }

    public static ResponseEntity<SuccessResponse> setSuccessResponseCreated(String successMessage, Object object) {
        return setSuccessResponse(successMessage, HttpStatus.CREATED, object);
    }

    public static ResponseEntity<SuccessResponse> setSuccessResponseNoContent(String message) {
        return setSuccessResponse(message, HttpStatus.NO_CONTENT, null);
    }

    private static ResponseEntity<SuccessResponse> setSuccessResponse(String successMessage, HttpStatusCode httpStatusCode, Object object) {
        return ResponseEntity.status(httpStatusCode).body(SuccessResponse.builder()
                .success(true)
                .code(httpStatusCode.value())
                .message(successMessage)
                .response(object)
                .status("SUCCESS")
                .build());
    }
}
