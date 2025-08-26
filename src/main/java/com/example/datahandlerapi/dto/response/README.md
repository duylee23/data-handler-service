# Standardized API Response Format

## Overview
All API endpoints in this application now use a standardized response format to ensure consistency across the entire API.

## Response Structure

### Success Response
```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { /* actual response data */ },
  "timestamp": "2025-08-25T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Operation failed",
  "error": "Detailed error information",
  "timestamp": "2025-08-25T10:30:00"
}
```

## Fields

- **success** (boolean): Indicates whether the operation was successful
- **message** (string): Human-readable message describing the result
- **data** (T): The actual response data (only present on success)
- **error** (string): Detailed error information (only present on failure)
- **timestamp** (LocalDateTime): When the response was generated

## Usage

### Using ApiResponse Factory Methods

```java
// Success with data
return ResponseEntity.ok(ApiResponse.success("User created successfully", userDto));

// Success without data
return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));

// Error with details
return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    .body(ApiResponse.error("Validation failed", "Username already exists"));

// Error without details
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    .body(ApiResponse.error("Internal server error"));
```

### Using ResponseUtil Helper Class

```java
// Success responses
return ResponseUtil.ok("User retrieved successfully", userDto);
return ResponseUtil.created("User created successfully", userDto);

// Error responses
return ResponseUtil.badRequest("Invalid input", "Username is required");
return ResponseUtil.unauthorized("Authentication failed");
return ResponseUtil.notFound("User not found");
return ResponseUtil.internalServerError("Database connection failed");
```

## HTTP Status Codes

The HTTP status code in the response header corresponds to the operation result:

- **200 OK**: Successful operation
- **201 Created**: Resource created successfully
- **400 Bad Request**: Client error (validation, malformed request)
- **401 Unauthorized**: Authentication required or failed
- **403 Forbidden**: Access denied
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

## Global Exception Handling

The `GlobalExceptionHandler` automatically catches exceptions and wraps them in the standardized format:

- `EntityNotFoundException` → 404 with standardized error response
- `IllegalArgumentException` → 400 with standardized error response
- `AuthenticationException` → 401 with standardized error response
- `AccessDeniedException` → 403 with standardized error response
- `MethodArgumentNotValidException` → 400 with validation error details
- All other exceptions → 500 with generic error message

## Best Practices

1. **Consistent Messages**: Use clear, user-friendly messages
2. **Appropriate HTTP Status**: Match the HTTP status code with the response content
3. **Error Details**: Include helpful error details for debugging (but not sensitive information)
4. **Use Helpers**: Prefer `ResponseUtil` methods for common response types
5. **Don't Duplicate**: Let the global exception handler catch and wrap exceptions

## Examples

### User Controller
```java
@GetMapping("/list")
public ResponseEntity<ApiResponse<List<UserDTO>>> listUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return ResponseUtil.ok("User list retrieved successfully", users);
}

@PostMapping("/add")
public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO dto) {
    UserDTO createdUser = userService.createUser(dto);
    return ResponseUtil.created("User created successfully", createdUser);
}
```

### Authentication Controller
```java
@PostMapping("/login")
public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
    try {
        AuthResponse authResponse = authService.authenticate(request);
        return ResponseUtil.ok("Login successful", authResponse);
    } catch (BadCredentialsException e) {
        return ResponseUtil.unauthorized("Invalid credentials", "Authentication failed");
    }
}
```