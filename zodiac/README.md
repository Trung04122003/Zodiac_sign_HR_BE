# ♐ Zodiac Sign HR Management System - Backend

**JCI Danang Junior Club**  
*Vice President - Membership & Training*

> "Aim High, Lead with Optimism!" - Sagittarius Motto

---

## 🌟 Overview

A comprehensive HR management system designed for JCI Danang Junior Club to manage 50-100 members using zodiac wisdom. This system helps analyze team compatibility, optimize team composition, and understand member personalities through zodiac insights.

### Key Features

- 👥 **Member Management** - Complete CRUD operations for JCI members
- ♈-♓ **Zodiac Intelligence** - 12 zodiac sign profiles with detailed traits
- 🤝 **Compatibility Calculator** - Analyze one-on-one and team compatibility
- 🏢 **Department Management** - Organize members into committees/teams
- 📊 **Analytics Dashboard** - Zodiac distribution, element balance, insights
- 📝 **Notes System** - Personal observations and member notes
- 🎂 **Birthday Tracker** - Upcoming birthdays with zodiac themes

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.0**
    - Spring Web (REST APIs)
    - Spring Data JPA (Database ORM)
    - Spring Security (Authentication)
- **MySQL 8.0** - Primary database
- **Flyway** - Database migrations
- **Lombok** - Reduce boilerplate
- **MapStruct** - DTO mapping
- **Swagger/OpenAPI** - API documentation

---

## 📁 Project Structure

```
Zodiac_sign_HR_BE/
├── src/main/java/com/jci/zodiac/
│   ├── ZodiacSignHrBeApplication.java
│   ├── config/
│   │   ├── CorsConfig.java
│   │   ├── SecurityConfig.java
│   │   └── OpenApiConfig.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Member.java
│   │   ├── ZodiacProfile.java
│   │   └── ZodiacCompatibility.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── MemberRepository.java
│   │   ├── ZodiacProfileRepository.java
│   │   └── ZodiacCompatibilityRepository.java
│   ├── service/
│   │   └── ZodiacUtilityService.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── BadRequestException.java
│   └── util/
│       ├── ApiResponse.java
│       └── ZodiacCalculator.java
└── src/main/resources/
    ├── application.yml
    └── db/migration/
        └── V1__Initial_Schema.sql
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IntelliJ IDEA / Eclipse (recommended)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Zodiac_sign_HR_BE
   ```

2. **Configure MySQL Database**

   Create database in MySQL:
   ```sql
   CREATE DATABASE zodiac_hr_db 
   CHARACTER SET utf8mb4 
   COLLATE utf8mb4_unicode_ci;
   ```

3. **Update application.yml**

   Edit `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/zodiac_hr_db
       username: root
       password: @Trung123  # Your MySQL password
   ```

4. **Install dependencies**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**
    - Backend API: http://localhost:8080/api
    - Swagger UI: http://localhost:8080/api/swagger-ui.html

---

## 📊 Database Schema

### Core Tables

1. **users** - Single user (VP - Membership & Training)
2. **members** - JCI members (50-100 records)
3. **zodiac_profiles** - 12 zodiac signs master data
4. **zodiac_compatibility** - 144 compatibility pairs (12x12)
5. **departments** - JCI committees/teams
6. **notes** - Personal observations

### Key Relationships

- Member → Department (Many-to-One)
- Member → ZodiacSign (via calculation)
- ZodiacCompatibility → ZodiacSign pairs

---

## 🎯 API Endpoints (Coming Soon)

### Member Management
- `GET /api/members` - List all members
- `POST /api/members` - Create member
- `GET /api/members/{id}` - Get member details
- `PUT /api/members/{id}` - Update member
- `DELETE /api/members/{id}` - Delete member

### Zodiac Intelligence
- `GET /api/zodiac/profiles` - Get all 12 zodiac profiles
- `POST /api/compatibility/calculate` - Calculate compatibility
- `GET /api/compatibility/best-pairs` - Find best matches

### Analytics
- `GET /api/dashboard/overview` - Dashboard stats
- `GET /api/analytics/zodiac-distribution` - Sign distribution
- `GET /api/analytics/element-balance` - Element balance

---

## 🔮 Zodiac System

### Elements & Signs

| Element | Signs | Traits |
|---------|-------|--------|
| 🔥 Fire | Aries, Leo, Sagittarius | Energetic, Passionate, Enthusiastic |
| 🌍 Earth | Taurus, Virgo, Capricorn | Practical, Reliable, Grounded |
| 💨 Air | Gemini, Libra, Aquarius | Intellectual, Communicative, Social |
| 💧 Water | Cancer, Scorpio, Pisces | Emotional, Intuitive, Empathetic |

### Compatibility Rules

- **Harmonious**: Same element or Fire↔Air, Earth↔Water
- **Neutral**: Different non-conflicting elements
- **Challenging**: Fire↔Water, Earth↔Air

---

## 🧪 Testing

Run tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

---

## 📝 Development Roadmap

### ✅ Phase 1 - Week 1 (COMPLETED)
- [x] Project setup
- [x] Database schema
- [x] Flyway migrations

### ✅ Phase 1 - Week 2 (CURRENT)
- [x] Core entities
- [x] Repositories
- [x] Zodiac utility service
- [x] Exception handling
- [x] CORS & Security config

### 🚧 Phase 2 - Week 3-4 (NEXT)
- [ ] Member CRUD APIs
- [ ] Search & Filter
- [ ] Bulk operations
- [ ] File upload

---

## 👤 Author

**Vice President - Membership & Training**  
JCI Danang Junior Club  
Zodiac Sign: ♐ Sagittarius

---

## 📄 License

MIT License - Personal Use Only

---

## 🎨 Sagittarius Theme

**Colors:**
- Primary: `#9B59B6` (Purple)
- Secondary: `#3498DB` (Blue)
- Accent: `#F39C12` (Gold)

**Motto:** *"Aim High, Lead with Optimism!"*

---

**Built with ❤️ and Sagittarius Energy ♐**