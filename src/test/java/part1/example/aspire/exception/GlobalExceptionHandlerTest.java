package part1.example.aspire.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleArgumentExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "Invalid field");

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleArgumentExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("fieldName", "Invalid field");
        assertEquals(expectedErrors, response.getBody());
    }


    @Test
    public void testStackOverflowError() {
        StackOverflowError ex = new StackOverflowError("Stack overflow occurred");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.stackOverflowError(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("message", "Stack overflow occurred");
        assertEquals(expectedErrors, response.getBody());
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new Exception("An error occurred");

        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleArgumentExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("message", "An error occurred");
        assertEquals(expectedErrors, response.getBody());
    }
}
