package com.assettrack.exception;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import org.springframework.dao.DataIntegrityViolationException;import java.util.*;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ApiException.class) ResponseEntity<?> api(ApiException e){return ResponseEntity.status(e.getStatus()).body(Map.of("message",e.getMessage()));}
 @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<?> data(){return ResponseEntity.badRequest().body(Map.of("message","Duplicate or referenced data prevents this operation."));}
 @ExceptionHandler(Exception.class) ResponseEntity<?> other(Exception e){return ResponseEntity.status(500).body(Map.of("message","Unexpected server error."));}
}
