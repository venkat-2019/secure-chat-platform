package com.chatapp.auth;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.security.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request.getUsername(), request.getEmail(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequest request) {
        try {
            authService.login(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(request.getEmail());
            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            data.put("email", request.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", data));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}
