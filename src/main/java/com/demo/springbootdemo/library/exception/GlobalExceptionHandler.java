package com.demo.springbootdemo.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 【職責】將常見例外轉成一致的 JSON 錯誤回應。
 * <p>【技巧】以 {@code @ControllerAdvice} 與多個 {@code @ExceptionHandler} 依型別映射 HTTP 狀態。
 * <p>【概念】集中例外轉換可讓 Controller 保持成功路徑，並確保錯誤 JSON 格式一致。
 * <p>【邊界】不決定業務層何時拋出何種例外，僅格式化已拋出的例外。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 【職責】將 Bean Validation 失敗轉為 400，並附欄位級錯誤明細。
     * <p>【技巧】遍歷 {@link FieldError} 組成 details Map。
     * <p>【概念】結構化欄位錯誤比單一字串更利於前端逐欄提示。
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", System.currentTimeMillis());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Bad Request");
        errors.put("message", "參數校驗失敗");
        
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            details.put(fieldName, errorMessage);
        });
        errors.put("details", details);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * 【職責】將 {@link BookNotFoundException} 轉為 404。
     * <p>【技巧】組裝含 timestamp／status／message 的錯誤 Map。
     * <p>【概念】資源不存在用 404，可與驗證失敗的 400 清楚區分。
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookNotFoundException(BookNotFoundException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", System.currentTimeMillis());
        errorMap.put("status", HttpStatus.NOT_FOUND.value());
        errorMap.put("error", "Not Found");
        errorMap.put("message", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

    /**
     * 【職責】將未另有專屬處理器的 {@link RuntimeException} 轉為 400。
     * <p>【技巧】捕捉較廣的執行期例外作為業務拒絕的通用出口。
     * <p>【概念】示範用兜底；正式系統應更細分例外型別，避免把系統錯誤誤標成 400。
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", System.currentTimeMillis());
        errorMap.put("status", HttpStatus.BAD_REQUEST.value());
        errorMap.put("error", "Bad Request");
        errorMap.put("message", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    /**
     * 【職責】將找不到靜態／對應資源的請求轉為 404。
     * <p>【技巧】處理 Spring MVC 的 {@code NoResourceFoundException}。
     * <p>【概念】區分「業務資源不存在」與「URL／靜態檔不存在」，兩者都是 404 但訊息不同。
     */
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(org.springframework.web.servlet.resource.NoResourceFoundException e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", System.currentTimeMillis());
        errorMap.put("status", HttpStatus.NOT_FOUND.value());
        errorMap.put("error", "Not Found");
        errorMap.put("message", "Requested URL not found.");
        errorMap.put("details", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap);
    }

    /**
     * 【職責】兜底處理其餘未預期例外，回 500 且不把堆疊當主訊息回給客戶端。
     * <p>【技巧】通用 {@link Exception} 處理器放在最後，細節放 details。
     * <p>【概念】對外隱藏內部細節可降低資訊洩漏；完整堆疊應只留在伺服器日誌。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", System.currentTimeMillis());
        errorMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorMap.put("error", "Internal Server Error");
        errorMap.put("message", "伺服器發生非預期錯誤，請連繫管理員。");
        errorMap.put("details", e.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
    }
}
