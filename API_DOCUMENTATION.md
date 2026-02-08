# Secure Chat Platform - API Documentation

## Overview
This is a secure, real-time chat platform built with Spring Boot, WebSocket, and JWT authentication.

## Base URL
```
http://localhost:8081
```

## Authentication
All endpoints (except `/auth/**`) require JWT token in the `Authorization` header:
```
Authorization: Bearer <your_jwt_token>
```

---

## Authentication Endpoints

### 1. Register User
- **Endpoint:** `POST /auth/register`
- **Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```
- **Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null,
  "timestamp": "2026-02-07T10:30:00"
}
```

### 2. Login User
- **Endpoint:** `POST /auth/login`
- **Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePassword123"
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "john@example.com"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

---

## User Endpoints

### 1. Get User by ID
- **Endpoint:** `GET /users/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "status": "ONLINE",
    "createdAt": "2026-02-07T10:20:00"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

### 2. Update Username
- **Endpoint:** `PUT /users/{id}`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
```json
{
  "username": "new_username"
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Username updated successfully",
  "data": {
    "id": 1,
    "username": "new_username",
    "email": "john@example.com",
    "status": "ONLINE",
    "createdAt": "2026-02-07T10:20:00"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

### 3. Update User Status
- **Endpoint:** `PUT /users/{id}/status?status=OFFLINE`
- **Headers:** `Authorization: Bearer <token>`
- **Query Parameters:**
  - `status`: `ONLINE` or `OFFLINE`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Status updated successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "status": "OFFLINE",
    "createdAt": "2026-02-07T10:20:00"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

### 4. Get User Status
- **Endpoint:** `GET /users/{id}/status`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Status retrieved successfully",
  "data": "ONLINE",
  "timestamp": "2026-02-07T10:30:00"
}
```

---

## Message Endpoints

### 1. Send Message
- **Endpoint:** `POST /messages/send`
- **Headers:** `Authorization: Bearer <token>`
- **Request Body:**
```json
{
  "senderId": 1,
  "receiverId": 2,
  "content": "Hello, how are you?"
}
```
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Message sent successfully",
  "data": {
    "id": 1,
    "senderId": 1,
    "receiverId": 2,
    "content": "Hello, how are you?",
    "delivered": true,
    "read": false,
    "toxic": false,
    "createdAt": "2026-02-07T10:30:00"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

### 2. Get Messages for Receiver
- **Endpoint:** `GET /messages/receiver/{receiverId}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Messages retrieved successfully",
  "data": [
    {
      "id": 1,
      "senderId": 1,
      "receiverId": 2,
      "content": "Hello, how are you?",
      "delivered": true,
      "read": false,
      "toxic": false,
      "createdAt": "2026-02-07T10:30:00"
    }
  ],
  "timestamp": "2026-02-07T10:30:00"
}
```

### 3. Mark Message as Read
- **Endpoint:** `PUT /messages/read/{messageId}`
- **Headers:** `Authorization: Bearer <token>`
- **Response (200 OK):**
```json
{
  "success": true,
  "message": "Message marked as read",
  "data": {
    "id": 1,
    "senderId": 1,
    "receiverId": 2,
    "content": "Hello, how are you?",
    "delivered": true,
    "read": true,
    "toxic": false,
    "createdAt": "2026-02-07T10:30:00"
  },
  "timestamp": "2026-02-07T10:30:00"
}
```

---

## WebSocket Endpoints

### Connect to Chat
- **Endpoint:** `ws://localhost:8081/ws/chat`
- **Protocol:** STOMP over WebSocket

### Subscribe to Messages
```javascript
// Subscribe to receive messages
stompClient.subscribe('/topic/chat/' + recipientId, function(message) {
    console.log('Message received:', message.body);
});
```

### Send Message via WebSocket
```javascript
// Send a message
stompClient.send('/app/chat', {}, JSON.stringify({
    senderId: 1,
    receiverId: 2,
    content: 'Hello!'
}));
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2026-02-07T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2026-02-07T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2026-02-07T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## JWT Token Details

- **Algorithm:** HS256 (HMAC SHA-256)
- **Expiration:** 24 hours (86400000 milliseconds)
- **Secret Key:** Stored in `application.properties`

### Sample Token Claims
```json
{
  "sub": "john@example.com",
  "iat": 1707300600,
  "exp": 1707387000
}
```

---

## Database Schema

### users table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255),
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255),
  status VARCHAR(50),
  created_at DATETIME NOT NULL,
  updated_at DATETIME
);
```

### message table
```sql
CREATE TABLE message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id BIGINT,
  receiver_id BIGINT,
  content TEXT,
  delivered BOOLEAN DEFAULT FALSE,
  read BOOLEAN DEFAULT FALSE,
  toxic BOOLEAN DEFAULT FALSE,
  created_at DATETIME NOT NULL
);
```

---

## Setup Instructions

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chatdb?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=chatuser
spring.datasource.password=chat123
jwt.secret=YourSecretKeyHere
jwt.expiration=86400000
```

### Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

---

## Security Features

1. **JWT Authentication:** All secured endpoints require valid JWT tokens
2. **CORS Protection:** Restricted to specified origins
3. **Password Hashing:** (Future enhancement with BCrypt)
4. **Toxicity Detection:** Messages are scanned for toxic content
5. **CSRF Protection:** Disabled for API usage
6. **WebSocket Security:** Restricted to specified origins only

---

## Future Enhancements

- [ ] Password hashing with BCrypt
- [ ] Refresh tokens
- [ ] Email verification
- [ ] Rate limiting
- [ ] Message encryption
- [ ] File sharing with virus scanning
- [ ] Video calling integration
- [ ] User roles and permissions
- [ ] Message search functionality
- [ ] Read receipts improvements

---

## Support
For issues and questions, please refer to the project documentation or create an issue on GitHub.

