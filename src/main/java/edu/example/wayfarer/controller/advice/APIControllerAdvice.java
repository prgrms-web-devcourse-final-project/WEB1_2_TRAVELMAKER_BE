package edu.example.wayfarer.controller.advice;

import edu.example.wayfarer.exception.MarkerTaskException;
import edu.example.wayfarer.exception.ScheduleItemTaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(MarkerTaskException.class)
    public ResponseEntity<String> handleMarkerTaskException(MarkerTaskException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(ScheduleItemTaskException.class)
    public ResponseEntity<String> handleScheduleItemTaskException(ScheduleItemTaskException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }
}
