package com.chatapp.user;

import com.chatapp.api.dto.ApiResponse;
import com.chatapp.api.dto.UserDTO;
import com.chatapp.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET user
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO dto = convertToDTO(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved successfully", dto));
    }

    // UPDATE username
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody User request
    ) {
        User user = userService.updateUsername(id, request.getUsername());
        UserDTO dto = convertToDTO(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Username updated successfully", dto));
    }

    // UPDATE status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        User user = userService.updateStatus(id, status);
        UserDTO dto = convertToDTO(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Status updated successfully", dto));
    }

    // GET status
    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> getStatus(@PathVariable Long id) {
        String status = userService.getUserById(id).getStatus();
        return ResponseEntity.ok(new ApiResponse<>(true, "Status retrieved successfully", status));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
