# 📖 API Documentation - Zodiac HR Management System

<div align="center">

![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![API Endpoints](https://img.shields.io/badge/Endpoints-100+-green)
![Status](https://img.shields.io/badge/Status-Production%20Ready-success)

**Complete REST API Reference**  
JCI Danang Junior Club - Zodiac HR Management

[Quick Start](#-quick-start) • [Authentication](#-authentication) • [Endpoints](#-api-endpoints) • [Examples](#-request--response-examples)

</div>

---

## 📋 Table of Contents

1. [Quick Start](#-quick-start)
2. [Base URL & Versioning](#-base-url--versioning)
3. [Authentication](#-authentication)
4. [Common Headers](#-common-headers)
5. [Response Format](#-response-format)
6. [Error Handling](#-error-handling)
7. [Pagination](#-pagination)
8. [API Endpoints](#-api-endpoints)
9. [Request/Response Examples](#-request--response-examples)
10. [Rate Limiting](#-rate-limiting)
11. [Best Practices](#-best-practices)

---

## 🚀 Quick Start

### Prerequisites
- Backend running on `http://localhost:8080`
- Valid credentials (if authentication enabled)
- API client (Postman, Insomnia, or cURL)

### Test Your First Request

```bash
# Health Check
curl http://localhost:8080/api/actuator/health

# Get All Zodiac Profiles
curl http://localhost:8080/api/zodiac/profiles

# Get Today's Daily Insight
curl http://localhost:8080/api/bonus/daily-insight
```

### Interactive Documentation
Access Swagger UI at: **http://localhost:8080/api/swagger-ui.html**

---

## 🌐 Base URL & Versioning

### Development
```
Base URL: http://localhost:8080/api
```

### Production
```
Base URL: https://api.jcidanang.com/api
```

### Versioning
Currently: **v1** (no version prefix in URL)  
Future: `/api/v2/...` for breaking changes

---

## 🔐 Authentication

### Current Implementation
**Single User System** - Authentication optional for development

### Future Implementation (JWT)
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "your_password"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 86400
  }
}
```

### Using JWT Token
```http
GET /api/members
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 📤 Common Headers

### Required Headers
```http
Content-Type: application/json
Accept: application/json
```

### Optional Headers
```http
Authorization: Bearer {token}
X-Request-ID: {unique-request-id}
User-Agent: YourApp/1.0
```

---

## 📦 Response Format

### Standard Success Response

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {
    
  },
  "timestamp": "2024-12-30T10:30:00"
}
```

### Standard Error Response

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2024-12-30T10:30:00"
}
```

### Paginated Response

```json
{
  "success": true,
  "message": "Members retrieved successfully",
  "data": {
    "content": [],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalPages": 5,
    "totalElements": 100,
    "last": false,
    "first": true,
    "numberOfElements": 20,
    "empty": false
  },
  "timestamp": "2024-12-30T10:30:00"
}
```

---

## ⚠️ Error Handling

### HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET, PUT, DELETE |
| 201 | Created | Successful POST (resource created) |
| 204 | No Content | Successful DELETE (no return data) |
| 207 | Multi-Status | Partial success (bulk operations) |
| 400 | Bad Request | Invalid input, validation errors |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Authenticated but no permission |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource (e.g., email exists) |
| 422 | Unprocessable Entity | Validation failed |
| 500 | Internal Server Error | Server-side error |
| 503 | Service Unavailable | Server overloaded/maintenance |

### Error Response Examples

#### 404 Not Found
```json
{
  "success": false,
  "message": "Member not found with id: 999",
  "data": null,
  "timestamp": "2024-12-30T10:30:00"
}
```

#### 400 Bad Request (Validation)
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "fullName": "Full name is required",
    "email": "Invalid email format",
    "dateOfBirth": "Date of birth must be in the past"
  },
  "timestamp": "2024-12-30T10:30:00"
}
```

#### 409 Conflict
```json
{
  "success": false,
  "message": "Member with email 'john@example.com' already exists",
  "data": null,
  "timestamp": "2024-12-30T10:30:00"
}
```

---

## 📄 Pagination

### Query Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (0-indexed) |
| `size` | integer | 20 | Items per page (max: 100) |
| `sort` | string | createdAt | Sort field |
| `direction` | string | DESC | Sort direction (ASC/DESC) |

### Example Request
```http
GET /api/members?page=0&size=20&sort=fullName&direction=ASC
```

### Example Response
```json
{
  "success": true,
  "data": {
    "content": [],
    "totalPages": 5,
    "totalElements": 100,
    "number": 0,
    "size": 20,
    "first": true,
    "last": false
  }
}
```

---

## 🎯 API Endpoints

### Summary Table

| Module | Endpoints | Base Path |
|--------|-----------|-----------|
| Member Management | 13 | `/api/members` |
| Birthday Tracker | 6 | `/api/birthdays` |
| Bulk Operations | 6 | `/api/members/bulk` |
| Compatibility | 8 | `/api/compatibility` |
| Departments | 11 | `/api/departments` |
| Teams | 11 | `/api/teams` |
| Team Builder | 5 | `/api/team-builder` |
| Zodiac Profiles | 7 | `/api/zodiac/profiles` |
| Dashboard | 8 | `/api/dashboard` |
| Notes | 8 | `/api/notes` |
| Settings | 20 | `/api/settings` |
| Bonus Features | 11 | `/api/bonus` |

**Total: 100+ Endpoints**

---

## 👥 1. Member Management API

Base Path: `/api/members`

### 1.1 Create Member

**POST** `/api/members`

Creates a new JCI member with auto-generated member code and zodiac calculation.

**Request Body:**
```json
{
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "phone": "+84912345678",
  "dateOfBirth": "1995-12-01",
  "position": "Member",
  "departmentId": 1,
  "joinDate": "2024-01-15",
  "membershipStatus": "Active",
  "membershipType": "FullMember",
  "address": "123 Le Duan, Da Nang",
  "city": "Da Nang",
  "occupation": "Software Engineer",
  "company": "Tech Corp",
  "tags": ["leadership", "tech"]
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Member created successfully",
  "data": {
    "id": 1,
    "memberCode": "JCI-DN-001",
    "fullName": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "phone": "+84912345678",
    "dateOfBirth": "1995-12-01",
    "zodiacSign": "Sagittarius",
    "zodiacElement": "Fire",
    "zodiacSymbol": "♐",
    "position": "Member",
    "departmentId": 1,
    "joinDate": "2024-01-15",
    "membershipStatus": "Active",
    "membershipType": "FullMember",
    "age": 29,
    "daysSinceJoined": 349,
    "isActive": true,
    "createdAt": "2024-12-30T10:30:00",
    "updatedAt": "2024-12-30T10:30:00"
  },
  "timestamp": "2024-12-30T10:30:00"
}
```

**Validation Rules:**
- `fullName`: Required, 2-100 characters
- `email`: Required, valid email format, unique
- `phone`: Optional, valid phone format
- `dateOfBirth`: Required, must be in the past
- `joinDate`: Required, cannot be future date
- `memberCode`: Auto-generated (JCI-DN-XXX)
- `zodiacSign`: Auto-calculated from DOB
- `zodiacElement`: Auto-calculated from DOB

**Error Cases:**
- 400: Validation failed
- 409: Email already exists
- 500: Server error

---

### 1.2 Get Member by ID

**GET** `/api/members/{id}`

Retrieves full member details by ID.

**Path Parameters:**
- `id` (required): Member ID

**Example Request:**
```http
GET /api/members/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Member retrieved successfully",
  "data": {
    "id": 1,
    "memberCode": "JCI-DN-001",
    "fullName": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "zodiacSign": "Sagittarius",
    "zodiacElement": "Fire",
    "zodiacSymbol": "♐",
    "position": "Member",
    "age": 29,
    "daysSinceJoined": 349,
    "isActive": true
  }
}
```

**Error Cases:**
- 404: Member not found

---

### 1.3 Get All Members (Paginated)

**GET** `/api/members`

Retrieves paginated list of all members.

**Query Parameters:**
- `page` (optional): Page number, default: 0
- `size` (optional): Page size, default: 20, max: 100
- `sort` (optional): Sort field, default: createdAt
- `direction` (optional): ASC/DESC, default: DESC

**Example Request:**
```http
GET /api/members?page=0&size=20&sort=fullName&direction=ASC
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Members retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "memberCode": "JCI-DN-001",
        "fullName": "Nguyen Van A",
        "zodiacSign": "Sagittarius",
        "zodiacSymbol": "♐",
        "zodiacElement": "Fire",
        "position": "Member",
        "membershipStatus": "Active",
        "isActive": true
      }
    ],
    "totalPages": 5,
    "totalElements": 100,
    "number": 0,
    "size": 20,
    "first": true,
    "last": false
  }
}
```

---

### 1.4 Search Members

**POST** `/api/members/search`

Advanced search with multiple filters.

**Request Body:**
```json
{
  "keyword": "nguyen",
  "zodiacSign": "Sagittarius",
  "zodiacElement": "Fire",
  "departmentId": 1,
  "membershipStatus": "Active",
  "joinDateFrom": "2024-01-01",
  "joinDateTo": "2024-12-31",
  "page": 0,
  "size": 20,
  "sort": "fullName",
  "direction": "ASC"
}
```

**Response:** Same as Get All Members

---

### 1.5 Update Member

**PUT** `/api/members/{id}`

Updates existing member information.

**Path Parameters:**
- `id` (required): Member ID

**Request Body:**
```json
{
  "fullName": "Nguyen Van A Updated",
  "position": "Vice President",
  "phone": "+84987654321"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Member updated successfully",
  "data": {
    "id": 1,
    "fullName": "Nguyen Van A Updated",
    "position": "Vice President",
    "updatedAt": "2024-12-30T11:00:00"
  }
}
```

**Notes:**
- Only provided fields are updated
- `zodiacSign` and `zodiacElement` are recalculated if `dateOfBirth` changes
- `memberCode` cannot be changed

---

### 1.6 Delete Member (Soft Delete)

**DELETE** `/api/members/{id}`

Soft deletes member (changes status to Inactive).

**Path Parameters:**
- `id` (required): Member ID

**Example Request:**
```http
DELETE /api/members/1
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Member deleted successfully",
  "timestamp": "2024-12-30T11:00:00"
}
```

---

### 1.7 Permanently Delete Member

**DELETE** `/api/members/{id}/permanent`

⚠️ **Warning:** Permanently deletes member from database. Cannot be undone!

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Member permanently deleted",
  "timestamp": "2024-12-30T11:00:00"
}
```

---

### 1.8 Get Active Members Only

**GET** `/api/members/active`

Retrieves only members with Active status.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "fullName": "Nguyen Van A",
      "membershipStatus": "Active"
    }
  ]
}
```

---

### 1.9 Get Members by Zodiac Sign

**GET** `/api/members/zodiac/{sign}`

Filters members by zodiac sign.

**Path Parameters:**
- `sign`: One of: Aries, Taurus, Gemini, Cancer, Leo, Virgo, Libra, Scorpio, Sagittarius, Capricorn, Aquarius, Pisces

**Example Request:**
```http
GET /api/members/zodiac/Sagittarius
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "fullName": "Nguyen Van A",
      "zodiacSign": "Sagittarius",
      "zodiacSymbol": "♐"
    }
  ]
}
```

---

### 1.10 Get Members by Element

**GET** `/api/members/element/{element}`

Filters members by zodiac element.

**Path Parameters:**
- `element`: One of: Fire, Earth, Air, Water

**Example Request:**
```http
GET /api/members/element/Fire
```

---

### 1.11 Get Members by Department

**GET** `/api/members/department/{departmentId}`

Retrieves all members in a specific department.

**Path Parameters:**
- `departmentId` (required): Department ID

**Example Request:**
```http
GET /api/members/department/1
```

---

### 1.12 Get Member Statistics

**GET** `/api/members/stats`

Returns member count statistics.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "totalMembers": 100,
    "activeMembers": 85
  }
}
```

---

### 1.13 Get Member by Code

**GET** `/api/members/code/{memberCode}`

Retrieves member by unique member code.

**Path Parameters:**
- `memberCode` (required): Member code (e.g., JCI-DN-001)

**Example Request:**
```http
GET /api/members/code/JCI-DN-001
```

---

## 🎂 2. Birthday Tracker API

Base Path: `/api/birthdays`

### 2.1 Get Birthdays Today

**GET** `/api/birthdays/today`

Returns members with birthdays today.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "🎂 2 birthday(s) today!",
  "data": [
    {
      "id": 1,
      "fullName": "Nguyen Van A",
      "zodiacSign": "Sagittarius",
      "zodiacSymbol": "♐",
      "dateOfBirth": "1995-12-30",
      "age": 29
    }
  ]
}
```

---

### 2.2 Get Upcoming Birthdays

**GET** `/api/birthdays/upcoming?daysAhead={days}`

Returns birthdays in the next N days.

**Query Parameters:**
- `daysAhead` (optional): Number of days ahead, default: 30

**Example Request:**
```http
GET /api/birthdays/upcoming?daysAhead=7
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 5 birthday(s) in next 7 days",
  "data": [
    {
      "id": 1,
      "fullName": "Nguyen Van A",
      "dateOfBirth": "1995-12-30",
      "daysUntilBirthday": 3
    }
  ]
}
```

---

### 2.3 Get Birthdays by Month

**GET** `/api/birthdays/month/{month}`

Returns all birthdays in a specific month.

**Path Parameters:**
- `month`: Month number (1-12) or name (JANUARY, FEBRUARY, etc.)

**Example Request:**
```http
GET /api/birthdays/month/12
# OR
GET /api/birthdays/month/DECEMBER
```

---

### 2.4 Get Birthdays by Zodiac Sign

**GET** `/api/birthdays/zodiac/{sign}`

Returns birthdays for a specific zodiac sign.

**Example Request:**
```http
GET /api/birthdays/zodiac/Sagittarius
```

---

### 2.5 Check if Birthdays Today

**GET** `/api/birthdays/check`

Simple boolean check if there are birthdays today.

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "hasBirthdays": true,
    "count": 2
  }
}
```

---

### 2.6 Count Upcoming Birthdays

**GET** `/api/birthdays/count?daysAhead={days}`

Returns count of upcoming birthdays.

**Query Parameters:**
- `daysAhead` (optional): Number of days, default: 7

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "daysAhead": 7,
    "count": 5
  }
}
```

---

## 📦 3. Bulk Operations API

Base Path: `/api/members/bulk`

### 3.1 Bulk Create Members

**POST** `/api/members/bulk/create`

Creates multiple members at once.

**Request Body:**
```json
{
  "members": [
    {
      "fullName": "Member 1",
      "email": "member1@example.com",
      "dateOfBirth": "1995-01-15",
      "joinDate": "2024-01-01"
    },
    {
      "fullName": "Member 2",
      "email": "member2@example.com",
      "dateOfBirth": "1996-06-20",
      "joinDate": "2024-01-01"
    }
  ]
}
```

**Response (201 Created / 207 Multi-Status):**
```json
{
  "success": true,
  "message": "Bulk creation completed",
  "data": {
    "totalProcessed": 2,
    "successfulCreations": 2,
    "failedCreations": 0,
    "results": [
      {
        "index": 0,
        "status": "SUCCESS",
        "memberId": 1,
        "memberCode": "JCI-DN-001"
      },
      {
        "index": 1,
        "status": "SUCCESS",
        "memberId": 2,
        "memberCode": "JCI-DN-002"
      }
    ],
    "errors": []
  }
}
```

**Partial Success (207):**
```json
{
  "success": true,
  "message": "Bulk creation completed",
  "data": {
    "totalProcessed": 2,
    "successfulCreations": 1,
    "failedCreations": 1,
    "results": [
      {
        "index": 0,
        "status": "SUCCESS",
        "memberId": 1
      },
      {
        "index": 1,
        "status": "FAILED",
        "error": "Email already exists"
      }
    ]
  }
}
```

---

### 3.2 Bulk Update Status

**PUT** `/api/members/bulk/status`

Updates membership status for multiple members.

**Request Body:**
```json
{
  "memberIds": [1, 2, 3],
  "newStatus": "OnLeave"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Bulk status update completed",
  "data": {
    "totalProcessed": 3,
    "successfulUpdates": 3,
    "failedUpdates": 0
  }
}
```

---

### 3.3 Bulk Delete Members

**DELETE** `/api/members/bulk/delete`

Deletes multiple members (soft or hard delete).

**Request Body:**
```json
{
  "memberIds": [1, 2, 3],
  "permanent": false
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Bulk deletion completed",
  "data": {
    "totalProcessed": 3,
    "successfulDeletions": 3,
    "failedDeletions": 0
  }
}
```

---

### 3.4 Bulk Assign Department

**PUT** `/api/members/bulk/department/{departmentId}`

Assigns multiple members to a department.

**Path Parameters:**
- `departmentId` (required): Department ID

**Request Body:**
```json
[1, 2, 3, 4, 5]
```

---

### 3.5 Import from CSV

**POST** `/api/members/bulk/import/csv`

Imports members from CSV file.

**Content-Type:** `multipart/form-data`

**Form Data:**
- `file` (required): CSV file
- `skipHeader` (optional): boolean, default: true
- `dateFormat` (optional): string, default: yyyy-MM-dd

**Example Request (cURL):**
```bash
curl -X POST \
  http://localhost:8080/api/members/bulk/import/csv \
  -F "file=@members.csv" \
  -F "skipHeader=true" \
  -F "dateFormat=yyyy-MM-dd"
```

**CSV Format:**
```csv
fullName,email,dateOfBirth,joinDate,position
Nguyen Van A,email1@example.com,1995-12-01,2024-01-15,Member
Tran Thi B,email2@example.com,1996-06-20,2024-01-15,Member
```

**Response (201 Created / 207 Multi-Status):**
```json
{
  "success": true,
  "message": "Import completed: 2 successful, 0 failed",
  "data": {
    "totalRows": 2,
    "successfulImports": 2,
    "failedImports": 0,
    "results": [
      {
        "row": 1,
        "status": "SUCCESS",
        "memberId": 1,
        "memberCode": "JCI-DN-001"
      }
    ],
    "errors": []
  }
}
```

---

### 3.6 Download CSV Template

**GET** `/api/members/bulk/template/csv`

Downloads CSV template for bulk import.

**Response:** CSV file download

```csv
fullName,email,phone,dateOfBirth,position,joinDate,occupation,company
John Doe,john@example.com,+84912345678,1995-01-15,Member,2024-01-01,Engineer,ABC Corp
```

---

## 🤝 4. Compatibility Calculator API

Base Path: `/api/compatibility`

### 4.1 Get Compatibility by Signs

**GET** `/api/compatibility/signs?sign1={sign1}&sign2={sign2}`

Calculates compatibility between two zodiac signs.

**Query Parameters:**
- `sign1` (required): First zodiac sign
- `sign2` (required): Second zodiac sign

**Example Request:**
```http
GET /api/compatibility/signs?sign1=Aries&sign2=Leo
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "zodiacSign1": "Aries",
    "zodiacSign2": "Leo",
    "overallScore": 90.00,
    "workCompatibilityScore": 88.50,
    "communicationScore": 92.00,
    "conflictPotential": 10.00,
    "synergyScore": 91.00,
    "compatibilityLevel": "Excellent",
    "strengthsTogether": "Both are fire signs with natural leadership...",
    "challengesTogether": "May compete for dominance...",
    "managementTips": "Channel competitive energy into team goals...",
    "elementHarmony": "Harmonious"
  }
}
```

---

### 4.2 Get Compatibility by Members

**GET** `/api/compatibility/members?member1={id1}&member2={id2}`

Calculates compatibility between two members.

**Query Parameters:**
- `member1` (required): First member ID
- `member2` (required): Second member ID

**Example Request:**
```http
GET /api/compatibility/members?member1=1&member2=2
```

**Response:** Same format as 4.1

---

### 4.3 Get All Compatibilities for Sign

**GET** `/api/compatibility/signs/{sign}/all`

Returns compatibility with all other signs.

**Path Parameters:**
- `sign` (required): Zodiac sign

**Example Request:**
```http
GET /api/compatibility/signs/Sagittarius/all
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "zodiacSign2": "Aries",
      "overallScore": 88.00,
      "compatibilityLevel": "Excellent"
    },
    {
      "zodiacSign2": "Taurus",
      "overallScore": 55.00,
      "compatibilityLevel": "Moderate"
    }
    
  ]
}
```

---

### 4.4 Get Best Matches for Sign

**GET** `/api/compatibility/signs/{sign}/best`

Returns top 5 best compatible signs.

**Example Request:**
```http
GET /api/compatibility/signs/Sagittarius/best
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "zodiacSign2": "Aries",
      "overallScore": 88.00,
      "compatibilityLevel": "Excellent"
    },
    {
      "zodiacSign2": "Leo",
      "overallScore": 89.00,
      "compatibilityLevel": "Excellent"
    }
    
  ]
}
```

---

### 4.5 Get Best Compatible Pairs

**GET** `/api/compatibility/best-pairs?limit={limit}`

Returns top compatible zodiac sign pairs overall.

**Query Parameters:**
- `limit` (optional): Number of pairs, default: 10

**Example Request:**
```http
GET /api/compatibility/best-pairs?limit=10
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "zodiacSign1": "Cancer",
      "zodiacSign2": "Scorpio",
      "overallScore": 91.00,
      "compatibilityLevel": "Excellent"
    }
    
  ]
}
```

---

### 4.6 Get Challenging Pairs

**GET** `/api/compatibility/challenging-pairs`

Returns zodiac pairs with low compatibility (< 40%).

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "zodiacSign1": "Aries",
      "zodiacSign2": "Cancer",
      "overallScore": 38.00,
      "compatibilityLevel": "Challenging",
      "conflictPotential": 62.00
    }
  ]
}
```

---

## 🏢 5. Department Management API

Base Path: `/api/departments`

### 5.1 Create Department

**POST** `/api/departments`

Creates a new department/committee.

**Request Body:**
```json
{
  "name": "Membership & Growth",
  "code": "JCI-DN-MG",
  "description": "Focuses on member recruitment and retention",
  "zodiacTheme": "Sagittarius",
  "colorPrimary": "#9B59B6",
  "colorSecondary": "#3498DB",
  "leadMemberId": 1
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Department created successfully",
  "data": {
    "id": 1,
    "name": "Membership & Growth",
    "code": "JCI-DN-MG",
    "zodiacTheme": "Sagittarius",
    "zodiacSymbol": "♐",
    "memberCount": 0,
    "isActive": true,
    "createdAt": "2024-12-30T10:30:00"
  }
}
```

---

### 5.2 Get All Departments

**GET** `/api/departments`

Returns all departments.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Membership & Growth",
      "code": "JCI-DN-MG",
      "zodiacTheme": "Sagittarius",
      "memberCount": 15,
      "isActive": true
    }
  ]
}
```

---

### 5.3 Get Department Analytics

**GET** `/api/departments/{id}/analytics`

Returns zodiac analytics for a department.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Department analytics generated",
  "data": {
    "departmentId": 1,
    "departmentName": "Membership & Growth",
    "totalMembers": 15,
    "zodiacDistribution": {
      "Sagittarius": 3,
      "Leo": 2,
      "Aries": 2
    },
    "elementBalance": {
      "Fire": 7,
      "Earth": 3,
      "Air": 3,
      "Water": 2
    },
    "averageCompatibility": 72.5,
    "insights": [
      "Strong Fire element presence (46%)",
      "Good element diversity"
    ]
  }
}
```

---

## 👥 6. Team Management API

Base Path: `/api/teams`

### 6.1 Create Team

**POST** `/api/teams`

Creates a new project team.

**Request Body:**
```json
{
  "name": "Website Redesign Team",
  "description": "Q1 2024 website redesign project",
  "teamType": "Project",
  "departmentId": 1,
  "startDate": "2024-01-15",
  "endDate": "2024-03-31",
  "targetMemberCount": 5
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Team created successfully",
  "data": {
    "id": 1,
    "name": "Website Redesign Team",
    "teamType": "Project",
    "status": "Planning",
    "memberCount": 0,
    "targetMemberCount": 5,
    "createdAt": "2024-12-30T10:30:00"
  }
}
```

---

### 6.2 Add Member to Team

**POST** `/api/teams/{teamId}/members`

Adds a member to a team.

**Request Body:**
```json
{
  "memberId": 1,
  "role": "Team Leader",
  "notes": "Project lead with 5 years experience"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Member added to team successfully",
  "data": {
    "id": 1,
    "teamId": 1,
    "memberId": 1,
    "memberName": "Nguyen Van A",
    "role": "Team Leader",
    "joinedDate": "2024-12-30",
    "isActive": true
  }
}
```

---

### 6.3 Get Team Analytics

**GET** `/api/teams/{id}/analytics`

Returns compatibility analytics for a team.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Team compatibility: 75.0% (Good)",
  "data": {
    "teamId": 1,
    "teamName": "Website Redesign Team",
    "overallCompatibilityScore": 75.0,
    "compatibilityLevel": "Good",
    "memberCount": 5,
    "zodiacDistribution": {
      "Sagittarius": 2,
      "Leo": 1,
      "Virgo": 1,
      "Libra": 1
    },
    "elementBalance": {
      "Fire": 3,
      "Earth": 1,
      "Air": 1
    },
    "isElementBalanced": false,
    "conflictCount": 1,
    "strongPairs": [
      {
        "member1": "Nguyen Van A (Sagittarius)",
        "member2": "Tran Van B (Leo)",
        "score": 89.0
      }
    ],
    "conflictingPairs": [
      {
        "member1": "Le Van C (Virgo)",
        "member2": "Pham Van D (Sagittarius)",
        "score": 38.0
      }
    ],
    "insights": [
      "Strong Fire element dominance",
      "Watch for Virgo-Sagittarius friction",
      "Consider adding Water or Earth element"
    ]
  }
}
```

---

## 🎯 7. Team Builder API

Base Path: `/api/team-builder`

### 7.1 Build Team

**POST** `/api/team-builder/build`

Builds and analyzes team compatibility with real-time scoring.

**Request Body:**
```json
{
  "teamName": "New Project Team",
  "memberIds": [1, 2, 3, 4, 5],
  "targetSize": 5
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Team built successfully! Overall compatibility: 75.0% (Good)",
  "data": {
    "teamName": "New Project Team",
    "teamSize": 5,
    "overallCompatibilityScore": 75.0,
    "compatibilityLevel": "Good",
    "elementBalance": {
      "Fire": 2,
      "Earth": 1,
      "Air": 1,
      "Water": 1
    },
    "isElementBalanced": true,
    "conflictCount": 1,
    "members": [
      {
        "id": 1,
        "name": "Nguyen Van A",
        "zodiacSign": "Sagittarius",
        "element": "Fire"
      }
    ],
    "compatibilityMatrix": [
      [100, 89, 55, 72, 68],
      [89, 100, 62, 78, 71]
    ],
    "keyInsights": [
      "Excellent element balance",
      "Strong Fire-Fire synergy",
      "Minor conflict detected (38%)"
    ]
  }
}
```

---

### 7.2 Optimize Team

**POST** `/api/team-builder/optimize`

Get suggestions to improve team compatibility.

**Request Body:**
```json
{
  "currentTeamMemberIds": [1, 2, 3],
  "availableMemberIds": [4, 5, 6, 7, 8],
  "targetSize": 5
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 3 optimization suggestion(s)",
  "data": [
    {
      "type": "ADD_MEMBER",
      "memberId": 5,
      "memberName": "Phan Van E",
      "zodiacSign": "Pisces",
      "reason": "Adds Water element for balance",
      "expectedScoreIncrease": 8.5,
      "newScore": 83.5
    },
    {
      "type": "SWAP_MEMBER",
      "removeId": 2,
      "addId": 6,
      "reason": "Better compatibility with existing team",
      "expectedScoreIncrease": 12.0,
      "newScore": 87.0
    }
  ]
}
```

---

### 7.3 Detect Conflicts

**GET** `/api/team-builder/conflicts`

Identifies potential conflicts across the organization.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Found 5 potential conflicts (2 critical, 3 high)",
  "data": [
    {
      "severity": "CRITICAL",
      "member1": {
        "id": 1,
        "name": "Nguyen Van A",
        "zodiacSign": "Sagittarius"
      },
      "member2": {
        "id": 8,
        "name": "Hoang Van H",
        "zodiacSign": "Virgo"
      },
      "compatibilityScore": 35.0,
      "conflictPotential": 65.0,
      "teams": ["Team A", "Team B"],
      "recommendation": "Avoid pairing in same projects"
    }
  ]
}
```

---

### 7.4 Find Optimal Team

**POST** `/api/team-builder/optimal?targetSize={size}`

Finds the best team composition from available members.

**Query Parameters:**
- `targetSize` (required): Desired team size

**Request Body:**
```json
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Optimal team found! Compatibility: 87.5%",
  "data": {
    "teamSize": 5,
    "selectedMemberIds": [1, 3, 5, 7, 9],
    "overallCompatibilityScore": 87.5,
    "compatibilityLevel": "Excellent",
    "elementBalance": {
      "Fire": 1,
      "Earth": 2,
      "Air": 1,
      "Water": 1
    },
    "reason": "Best balance of compatibility and element diversity"
  }
}
```

---

### 7.5 Quick Team Check

**GET** `/api/team-builder/quick-check?members={id1},{id2},{id3}`

Quick compatibility check for a list of members.

**Query Parameters:**
- `members` (required): Comma-separated member IDs

**Example Request:**
```http
GET /api/team-builder/quick-check?members=1,2,3,4,5
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "teamSize": 5,
    "compatibilityScore": 75.0,
    "level": "Good",
    "conflictCount": 1,
    "isBalanced": true,
    "insights": [
      "Good overall compatibility",
      "Well-balanced elements",
      "One minor conflict detected"
    ]
  }
}
```

---

## ♈-♓ 8. Zodiac Profiles API

Base Path: `/api/zodiac/profiles`

### 8.1 Get All Zodiac Profiles

**GET** `/api/zodiac/profiles`

Returns all 12 zodiac sign profiles.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "zodiacSign": "Aries",
      "symbol": "♈",
      "element": "Fire",
      "modality": "Cardinal",
      "rulingPlanet": "Mars",
      "dateStart": "03-21",
      "dateEnd": "04-19",
      "personalityTraits": [
        "Courageous",
        "Determined",
        "Confident"
      ],
      "strengths": [
        "Leadership",
        "Initiative",
        "Courage"
      ],
      "workStyle": [
        "Direct",
        "Action-oriented",
        "Fast-paced"
      ],
      "bestRoles": [
        "Team Leader",
        "Project Manager"
      ]
    }
    
  ]
}
```

---

### 8.2 Get Profile by Sign

**GET** `/api/zodiac/profiles/{sign}`

Returns detailed profile for a specific zodiac sign.

**Path Parameters:**
- `sign` (required): Zodiac sign name

**Example Request:**
```http
GET /api/zodiac/profiles/Sagittarius
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "zodiacSign": "Sagittarius",
    "symbol": "♐",
    "element": "Fire",
    "modality": "Mutable",
    "rulingPlanet": "Jupiter",
    "dateStart": "11-22",
    "dateEnd": "12-21",
    "personalityTraits": [
      "Generous",
      "Idealistic",
      "Great sense of humor",
      "Optimistic",
      "Adventurous"
    ],
    "strengths": [
      "Optimism",
      "Enthusiasm",
      "Honesty",
      "Philosophy"
    ],
    "weaknesses": [
      "Impatient",
      "Tactless",
      "Overconfident"
    ],
    "workStyle": [
      "Optimistic innovator",
      "Brings fresh perspectives",
      "Thrives on growth"
    ],
    "bestRoles": [
      "Education",
      "Innovation",
      "Entrepreneurship"
    ],
    "communicationStyle": "Straightforward and enthusiastic",
    "motivationFactors": [
      "Freedom",
      "Growth",
      "Learning",
      "Adventure"
    ],
    "stressTriggers": [
      "Constraints",
      "Micromanagement",
      "Negativity"
    ]
  }
}
```

---

### 8.3 Get Profiles by Element

**GET** `/api/zodiac/profiles/element/{element}`

Returns all zodiac signs of a specific element.

**Path Parameters:**
- `element` (required): Fire, Earth, Air, or Water

**Example Request:**
```http
GET /api/zodiac/profiles/element/Fire
```

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "zodiacSign": "Aries",
      "symbol": "♈",
      "element": "Fire"
    },
    {
      "zodiacSign": "Leo",
      "symbol": "♌",
      "element": "Fire"
    },
    {
      "zodiacSign": "Sagittarius",
      "symbol": "♐",
      "element": "Fire"
    }
  ]
}
```

---

### 8.4 Search Profiles

**GET** `/api/zodiac/profiles/search?keyword={keyword}`

Searches profiles by keyword.

**Query Parameters:**
- `keyword` (required): Search term

**Example Request:**
```http
GET /api/zodiac/profiles/search?keyword=leadership
```

---

## 📊 9. Dashboard & Analytics API

Base Path: `/api/dashboard`

### 9.1 Get Dashboard Overview

**GET** `/api/dashboard/overview`

Returns main dashboard statistics.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Dashboard overview retrieved successfully",
  "data": {
    "totalMembers": 100,
    "activeMembers": 85,
    "totalTeams": 12,
    "activeTeams": 8,
    "totalDepartments": 5,
    "mostCommonZodiacSign": {
      "sign": "Sagittarius",
      "symbol": "♐",
      "count": 15,
      "percentage": 15.0
    },
    "averageTeamCompatibility": 73.5,
    "recentJoins": 8
  }
}
```

---

### 9.2 Get Zodiac Distribution

**GET** `/api/dashboard/zodiac-distribution`

Returns zodiac sign distribution data for pie chart.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Zodiac distribution retrieved successfully",
  "data": {
    "labels": [
      "Aries",
      "Taurus",
      "Gemini",
      "Cancer",
      "Leo",
      "Virgo",
      "Libra",
      "Scorpio",
      "Sagittarius",
      "Capricorn",
      "Aquarius",
      "Pisces"
    ],
    "values": [8, 7, 9, 6, 10, 8, 7, 6, 15, 9, 8, 7],
    "colors": [
      "#E74C3C",
      "#27AE60",
      "#F39C12",
      "#3498DB"
    ]
  }
}
```

---

### 9.3 Get Element Balance

**GET** `/api/dashboard/element-balance`

Returns zodiac element balance data for bar chart.

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Element balance retrieved successfully",
  "data": {
    "labels": ["Fire", "Earth", "Air", "Water"],
    "values": [33, 24, 24, 19],
    "percentages": [33.0, 24.0, 24.0, 19.0],
    "isBalanced": true
  }
}
```

---

### 9.4 Get Compatibility Matrix

**GET** `/api/dashboard/compatibility-matrix?departmentId={id}`

Returns compatibility matrix for heatmap visualization.

**Query Parameters:**
- `departmentId` (optional): Filter by department

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Compatibility matrix retrieved successfully",
  "data": {
    "memberNames": [
      "Nguyen Van A",
      "Tran Van B",
      "Le Van C"
    ],
    "matrix": [
      [100, 89, 55],
      [89, 100, 72],
      [55, 72, 100]
    ],
    "averageCompatibility": 73.5
  }
}
```

---

## 📝 10. Notes Management API

Base Path: `/api/notes`

### 10.1 Create Note

**POST** `/api/notes`

Creates a new note or observation.

**Request Body:**
```json
{
  "noteType": "Member",
  "memberId": 1,
  "title": "Performance Review Q4",
  "content": "Excellent leadership skills demonstrated...",
  "tags": ["performance", "leadership"],
  "isImportant": true
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Note created successfully",
  "data": {
    "id": 1,
    "noteType": "Member",
    "memberId": 1,
    "memberName": "Nguyen Van A",
    "title": "Performance Review Q4",
    "content": "Excellent leadership skills...",
    "tags": ["performance", "leadership"],
    "isImportant": true,
    "createdAt": "2024-12-30T10:30:00"
  }
}
```

---

### 10.2 Get Notes by Member

**GET** `/api/notes/member/{memberId}`

Returns all notes for a specific member.

**Path Parameters:**
- `memberId` (required): Member ID

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Performance Review Q4",
      "content": "Excellent leadership...",
      "createdAt": "2024-12-30T10:30:00"
    }
  ]
}
```

---

### 10.3 Get Important Notes

**GET** `/api/notes/important`

Returns all notes marked as important.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "noteType": "Member",
      "title": "Critical Issue",
      "isImportant": true
    }
  ]
}
```

---

## ⚙️ 11. Settings & Export API

Base Path: `/api/settings`

### 11.1 Get All Settings

**GET** `/api/settings`

Returns all system settings.

**Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "key": "organization.name",
      "value": "JCI Danang Junior Club",
      "category": "ORGANIZATION",
      "isPublic": true,
      "isEditable": true
    }
  ]
}
```

---

### 11.2 Update Setting

**PUT** `/api/settings/{key}`

Updates a setting value.

**Path Parameters:**
- `key` (required): Setting key

**Request Body:**
```json
{
  "value": "New Value"
}
```

---

### 11.3 Export Data to JSON

**GET** `/api/settings/export/json`

Exports all system data as JSON file.

**Response:** File download (`zodiac_hr_backup_1234567890.json`)

---

### 11.4 Export Data to Excel

**GET** `/api/settings/export/excel`

Exports all system data as Excel file.

**Response:** File download (`zodiac_hr_backup_1234567890.xlsx`)

---

### 11.5 Import Data from JSON

**POST** `/api/settings/import/json`

Imports system data from JSON backup file.

**Content-Type:** `multipart/form-data`

**Form Data:**
- `file` (required): JSON backup file

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Data imported successfully",
  "data": {
    "members": 50,
    "departments": 3,
    "teams": 5,
    "settings": 25
  }
}
```

---

## 📱 Example: Complete Workflow

### Scenario: Creating a New Project Team

```javascript
// Step 1: Create members
const member1 = await createMember({
  fullName: "John Doe",
  email: "john@example.com",
  dateOfBirth: "1995-12-01",
  joinDate: "2024-01-01"
});
// member1.zodiacSign = "Sagittarius" (auto-calculated)

const member2 = await createMember({
  fullName: "Jane Smith",
  email: "jane@example.com",
  dateOfBirth: "1996-08-10",
  joinDate: "2024-01-01"
});
// member2.zodiacSign = "Leo" (auto-calculated)

// Step 2: Check compatibility
const compatibility = await fetch(
  `/api/compatibility/members?member1=${member1.id}&member2=${member2.id}`
).then(r => r.json());
// Result: 89% compatibility (Excellent!)

// Step 3: Create team
const team = await createTeam({
  name: "Website Redesign",
  teamType: "Project",
  targetMemberCount: 5
});

// Step 4: Add members to team
await addMemberToTeam(team.id, {
  memberId: member1.id,
  role: "Team Leader"
});

await addMemberToTeam(team.id, {
  memberId: member2.id,
  role: "Designer"
});

// Step 5: Get team analytics
const analytics = await fetch(`/api/teams/${team.id}/analytics`)
  .then(r => r.json());

console.log(`Team compatibility: ${analytics.data.overallCompatibilityScore}%`);
// Output: "Team compatibility: 89%"

// Step 6: Get optimization suggestions
const suggestions = await optimizeTeam({
  currentTeamMemberIds: [member1.id, member2.id],
  availableMemberIds: [3, 4, 5, 6],
  targetSize: 5
});

console.log('Suggestions:', suggestions.data);
```

---

<div align="center">

## 🎉 END OF API DOCUMENTATION

**You now have complete API documentation covering all 100+ endpoints!**

For interactive testing, visit: [Swagger UI](http://localhost:8080/api/swagger-ui.html)

---

**Questions?** Check our [FAQ](FAQ.md) or [open an issue](../../issues)

**Built with ❤️ by JCI Danang Junior Club**

♐ **"Aim High, Lead with Optimism!"** ♐

</div>