package edu.example.wayfarer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GeocodingTaskException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
}
