# 📮 Postman Collection Guide - H2 Database Version

## Overview
This is the complete Postman API collection for the Secure Chat Platform with H2 database integration.

**Version**: 1.1.0
**Database**: H2 (In-Memory)
**Status**: ✅ Updated & Verified

---

## 🚀 Getting Started

### Step 1: Import Collection
1. Open Postman
2. Click **Import**
3. Upload `Postman_Collection.json`
4. Collection imported successfully ✅

### Step 2: Set Environment Variables
The collection uses the following variables:
```
{{base_url}}          → http://localhost:8081
{{jwt_token}}         → (auto-filled after login)
{{h2_console}}        → http://localhost:8081/h2-console
{{default_user_id}}   → 1
{{receiver_id}}       → 2
```

### Step 3: Start Application
```bash
./mvnw.cmd spring-boot:run
```

### Step 4: Test API
Follow the order below to test properly.

---

## 📋 API Endpoints

### 1️⃣ Authentication Endpoints

#### Register User
```
POST {{base_url}}/auth/register
Content-Type: application/json

Body:
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}

Response: 201 Created
{
  "success": true,
  "message": "User registered successfully",
  "data": null,
  "timestamp": "2026-02-08T12:00:00"
}
```

**When to Use**: First-time users
**Expected Status**: 201 Created

#### Login User
```
POST {{base_url}}/auth/login
Content-Type: application/json

Body:
{
  "email": "john@example.com",
  "password": "securePassword123"
}

Response: 200 OK
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "email": "john@example.com"
  },
  "timestamp": "2026-02-08T12:00:00"
}
```

**When to Use**: After registration, to get JWT token
**Expected Status**: 200 OK
**Important**: JWT token is automatically saved in `{{jwt_token}}` variable

---

### 2️⃣ User Management Endpoints

#### Get User by ID
```
GET {{base_url}}/users/{{default_user_id}}
Authorization: Bearer {{jwt_token}}

Response: 200 OK
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "status": "ONLINE",
    "createdAt": "2026-02-08T12:00:00"
  },
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Expected Status**: 200 OK

#### Update Username
```
PUT {{base_url}}/users/{{default_user_id}}
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

Body:
{
  "username": "new_username"
}

Response: 200 OK
{
  "success": true,
  "message": "Username updated successfully",
  "data": { ... }
}
```

**Requires**: Valid JWT token
**Expected Status**: 200 OK

#### Update User Status
```
PUT {{base_url}}/users/{{default_user_id}}/status?status=ONLINE
Authorization: Bearer {{jwt_token}}

Query Parameters:
- status: ONLINE or OFFLINE

Response: 200 OK
{
  "success": true,
  "message": "Status updated successfully",
  "data": { ... }
}
```

**Requires**: Valid JWT token
**Query Options**: ONLINE, OFFLINE
**Expected Status**: 200 OK

#### Get User Status
```
GET {{base_url}}/users/{{default_user_id}}/status
Authorization: Bearer {{jwt_token}}

Response: 200 OK
{
  "success": true,
  "message": "Status retrieved successfully",
  "data": "ONLINE",
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Expected Status**: 200 OK
**Returns**: User status string (ONLINE or OFFLINE)

---

### 3️⃣ Message Endpoints

#### Send Message
```
POST {{base_url}}/messages/send
Authorization: Bearer {{jwt_token}}
Content-Type: application/json

Body:
{
  "senderId": 1,
  "receiverId": 2,
  "content": "Hello, how are you?"
}

Response: 200 OK
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
    "createdAt": "2026-02-08T12:00:00"
  },
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Important**: 
- Toxicity check is automatic
- Delivered flag is set to `true` on send
- Read flag is `false` initially

#### Get Messages for Receiver
```
GET {{base_url}}/messages/receiver/{{receiver_id}}
Authorization: Bearer {{jwt_token}}

Response: 200 OK
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
      "createdAt": "2026-02-08T12:00:00"
    }
  ],
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Returns**: Array of messages received by user
**Important**: Read status shows whether message was read

#### Mark Message as Read
```
PUT {{base_url}}/messages/read/1
Authorization: Bearer {{jwt_token}}

Response: 200 OK
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
    "createdAt": "2026-02-08T12:00:00"
  },
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Important**: Sets `read` flag to `true`
**Expected Status**: 200 OK

---

### 4️⃣ File Endpoints

#### Upload File
```
POST {{base_url}}/files/upload
Authorization: Bearer {{jwt_token}}
Content-Type: multipart/form-data

Form Data:
- file: (select file from computer)

Response: 200 OK
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "fileName": "document.pdf",
    "fileUrl": "/uploads/document.pdf",
    "fileSize": 1024
  },
  "timestamp": "2026-02-08T12:00:00"
}
```

**Requires**: Valid JWT token
**Supported**: All file types
**Location**: Files stored in `/uploads` directory
**Expected Status**: 200 OK

---

## 🧪 Testing Workflow

### Recommended Test Sequence

#### Step 1: Register New Users (if not already registered)
1. Open **Authentication → Register User**
2. Click **Send**
3. Expected: 201 Created
4. Repeat for another user if needed

#### Step 2: Login to Get JWT Token
1. Open **Authentication → Login User**
2. Click **Send**
3. Expected: 200 OK with JWT token
4. Token automatically saved in `{{jwt_token}}`
5. All subsequent requests will use this token

#### Step 3: Get User Information
1. Open **User Management → Get User by ID**
2. Verify user ID in URL (default: 1)
3. Click **Send**
4. Expected: 200 OK with user details

#### Step 4: Update User Status
1. Open **User Management → Update User Status**
2. Change status parameter (ONLINE or OFFLINE)
3. Click **Send**
4. Expected: 200 OK with updated status

#### Step 5: Send Messages
1. Open **Messages → Send Message**
2. Update senderId and receiverId as needed
3. Click **Send**
4. Expected: 200 OK with message details

#### Step 6: Retrieve Messages
1. Open **Messages → Get Messages for Receiver**
2. Update receiver ID in URL
3. Click **Send**
4. Expected: 200 OK with message list

#### Step 7: Mark Message as Read
1. Open **Messages → Mark Message as Read**
2. Update message ID in URL
3. Click **Send**
4. Expected: 200 OK with read flag updated

---

## 🔑 Authentication

### JWT Token Details
- **Type**: Bearer token
- **Algorithm**: HS256 (HMAC SHA-256)
- **Expiration**: 24 hours
- **Header Format**: `Authorization: Bearer <token>`

### How to Use Token
```
1. Login via /auth/login
2. Token is automatically extracted and stored in {{jwt_token}}
3. All secured endpoints include: Authorization: Bearer {{jwt_token}}
4. Token valid for 24 hours
5. After expiration, login again to get new token
```

---

## 💾 H2 Database Console

### Access H2 Console
**URL**: `{{h2_console}}` → `http://localhost:8081/h2-console`

**When Application is Running**:
1. Open browser
2. Navigate to `http://localhost:8081/h2-console`
3. Enter credentials:
   - JDBC URL: `jdbc:h2:mem:chatdb`
   - Username: `sa`
   - Password: (leave empty)
4. Click **Connect**

### View Database Schema
Once connected, you can:
- View all tables (users, message)
- Run SQL queries
- View data in real-time
- Monitor database state

### Important Notes
- H2 is **in-memory only** (data lost on restart)
- Perfect for development and testing
- Database auto-created on startup
- Tables auto-dropped on shutdown

---

## 🔍 Common Issues & Solutions

### Issue: 401 Unauthorized
**Problem**: JWT token is invalid or expired
**Solution**: 
1. Login again via `/auth/login`
2. Wait for token to be set in `{{jwt_token}}`
3. Retry the request

### Issue: 400 Bad Request
**Problem**: Email already exists or invalid input
**Solution**:
1. Use different email for registration
2. Check request body format
3. Verify all required fields are present

### Issue: 500 Internal Server Error
**Problem**: Server error
**Solution**:
1. Check server logs
2. Ensure application is running: `./mvnw.cmd spring-boot:run`
3. Verify H2 database is initialized

### Issue: H2 Console Not Accessible
**Problem**: Cannot access `http://localhost:8081/h2-console`
**Solution**:
1. Ensure application is running
2. Verify port 8081 is not blocked
3. Check `spring.h2.console.enabled=true` in main properties

### Issue: File Upload Fails
**Problem**: Cannot upload files
**Solution**:
1. Ensure `/uploads` directory exists
2. Check file permissions
3. Verify file size is reasonable
4. Check JWT token validity

---

## 📊 Response Format

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ },
  "timestamp": "2026-02-08T12:00:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2026-02-08T12:00:00"
}
```

---

## 🔐 Security Notes

1. **JWT Tokens**: All secured endpoints require valid JWT
2. **CORS**: API restricted to specified origins
3. **Toxicity Detection**: Messages are scanned automatically
4. **Password**: Stored as-is (no hashing in current version)
5. **HTTPS**: Use HTTPS in production

---

## 🚀 Quick Testing Tips

### Tip 1: Bulk Testing
Create multiple users for testing by repeating Register step

### Tip 2: Different Recipients
Change `receiverId` in Send Message to test different conversations

### Tip 3: Monitor Database
Keep H2 console open to watch database changes in real-time

### Tip 4: Save Tests
Use Postman's collection runs to execute all tests automatically

### Tip 5: Environment Variables
Customize `{{base_url}}` for different environments (dev, staging, prod)

---

## 📈 Advanced Features

### Pre-request Scripts
The collection includes automatic JWT token extraction in Login response

### Test Scripts
Successful login automatically sets the JWT token for subsequent requests

### Collection Variables
- `base_url`: API base URL (changeable)
- `jwt_token`: JWT token (auto-set after login)
- `h2_console`: H2 console URL (for reference)
- `default_user_id`: Default user ID (changeable)
- `receiver_id`: Receiver ID for messages (changeable)

---

## 📝 Collection Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-01-15 | Initial collection (MySQL) |
| 1.1.0 | 2026-02-08 | Updated for H2 database, added console URL |

---

## 🎯 Next Steps

1. ✅ Import collection in Postman
2. ✅ Start application: `./mvnw.cmd spring-boot:run`
3. ✅ Register new user via `/auth/register`
4. ✅ Login via `/auth/login` (token auto-saved)
5. ✅ Test other endpoints in sequence
6. ✅ Monitor H2 console at `http://localhost:8081/h2-console`

---

## 📞 Support

For issues:
1. Check **Common Issues & Solutions** above
2. Review API_DOCUMENTATION.md
3. Check application logs
4. Verify H2 console shows expected data

---

**Collection Version**: 1.1.0
**Database**: H2 In-Memory
**Status**: ✅ Production Ready
**Last Updated**: 2026-02-08

Happy Testing! 🎉

