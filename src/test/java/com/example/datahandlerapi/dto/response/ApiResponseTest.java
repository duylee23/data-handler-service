package com.example.datahandlerapi.dto.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ApiResponseTest {

    @Test
    public void testSuccessResponseWithData() {
        String message = "Operation successful";
        String data = "test data";
        
        ApiResponse<String> response = ApiResponse.success(message, data);
        
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testSuccessResponseWithoutData() {
        String message = "Operation successful";
        
        ApiResponse<Void> response = ApiResponse.success(message);
        
        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testErrorResponseWithDetails() {
        String message = "Operation failed";
        String error = "Detailed error information";
        
        ApiResponse<Void> response = ApiResponse.error(message, error);
        
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(error, response.getError());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testErrorResponseWithoutDetails() {
        String message = "Operation failed";
        
        ApiResponse<Void> response = ApiResponse.error(message);
        
        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getError());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    public void testTimestampIsRecent() {
        ApiResponse<String> response = ApiResponse.success("test");
        LocalDateTime now = LocalDateTime.now();
        
        // Timestamp should be within the last second
        assertTrue(response.getTimestamp().isAfter(now.minusSeconds(1)));
        assertTrue(response.getTimestamp().isBefore(now.plusSeconds(1)));
    }
}