package edu.example.wayfarer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class GeocodingTaskException extends RuntimeException {

    private HttpStatus status;

    public GeocodingTaskException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}