/**
 * Copyright(C) 2026 Luvina Software Company
 *
 * GlobalExceptionHandler.java, 17/08/2026 thanhvinh
 */
package com.luvina.la.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.luvina.la.constant.Constants;
import com.luvina.la.payload.response.ErrorResponse;
import com.luvina.la.payload.response.MessageResponse;

/**
 * Xử lý tập trung các exception trong toàn bộ ứng dụng, trả về format lỗi chuẩn.
 *
 * @author thanhvinh
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý ngoại lệ nghiệp vụ AppException → trả HTTP 500, code 500, message lỗi tương ứng.
     *
     * @param ex ngoại lệ AppException xảy ra.
     * @return ResponseEntity chứa ErrorResponse.
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        MessageResponse message = new MessageResponse(ex.getCode(), ex.getParams());
        ErrorResponse body = new ErrorResponse(Constants.CODE_ERROR, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * Bắt mọi exception chưa được xử lý → trả code 500, message ER023.
     *
     * @param ex exception xảy ra.
     * @return ResponseEntity chứa ErrorResponse.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        MessageResponse message = new MessageResponse(Constants.ER023, new ArrayList<>());
        ErrorResponse body = new ErrorResponse(Constants.CODE_ERROR, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
