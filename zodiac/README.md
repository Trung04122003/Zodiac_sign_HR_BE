# ♐ Zodiac Sign HR Management System - Backend

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![Status](https://img.shields.io/badge/Status-Production%20Ready-success?style=for-the-badge)

**JCI Danang Junior Club**  
*Vice President - Membership & Training*

> **"Aim High, Lead with Optimism!"** - Sagittarius Motto ♐

[Features](#-features) • [Quick Start](#-quick-start) • [API Docs](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 🌟 Overview

A **comprehensive HR management system** designed specifically for JCI Danang Junior Club to manage 50-100 members using **zodiac wisdom**. This system analyzes team compatibility, optimizes team composition, and provides deep insights into member personalities through astrological profiles.

### 🎯 Why Zodiac HR?

- 📊 **Data-Driven Team Building** - Use compatibility scores to build balanced teams
- 🔮 **Personality Insights** - Understand member strengths, weaknesses, and work styles
- 🤝 **Conflict Prevention** - Identify potential conflicts before they happen
- 📈 **Analytics Dashboard** - Visualize zodiac distribution and element balance
- 🎂 **Birthday Tracking** - Never miss a member's special day with zodiac-themed notifications

---

## ✨ Features

### 🎯 Core Modules

#### 1. 👥 **Member Management**
- ✅ Complete CRUD operations for JCI members
- ✅ Auto-generate unique member codes (JCI-DN-XXX)
- ✅ Auto-calculate zodiac sign & element from date of birth
- ✅ Advanced search & filtering (by zodiac, element, department, status)
- ✅ Bulk operations (import CSV, bulk update, bulk delete)
- ✅ Avatar upload & file management
- ✅ Pagination & sorting support

#### 2. ♈-♓ **Zodiac Intelligence Engine**
- ✅ 12 complete zodiac sign profiles with detailed traits
- ✅ 144 pre-computed compatibility pairs (12×12 matrix)
- ✅ Work style analysis for each sign
- ✅ Communication style recommendations
- ✅ Motivation factors & stress triggers
- ✅ Element harmony calculation (Fire/Earth/Air/Water)

#### 3. 🤝 **Compatibility Calculator**
- ✅ One-on-one compatibility analysis
- ✅ Team compatibility scoring (3-10 members)
- ✅ Element balance detection
- ✅ Conflict potential assessment
- ✅ Synergy score calculation
- ✅ Best pair finder
- ✅ Challenging pair alerts

#### 4. 🏢 **Department & Team Management**
- ✅ Create departments with zodiac themes
- ✅ Build project teams with real-time compatibility
- ✅ Team builder tool with optimization suggestions
- ✅ Element balance tracking per team
- ✅ Team analytics & insights
- ✅ Member assignment & role management

#### 5. 📊 **Analytics & Dashboard**
- ✅ Zodiac distribution pie charts
- ✅ Element balance bar charts
- ✅ Department composition analysis
- ✅ New hires timeline with zodiac markers
- ✅ Compatibility heatmaps
- ✅ Organization-wide statistics

#### 6. 📝 **Notes & Settings**
- ✅ Personal notes for members/teams/departments
- ✅ Tagging system for organization
- ✅ Important notes highlighting
- ✅ System settings management
- ✅ Data export/import (JSON & Excel)
- ✅ Backup & restore functionality

#### 7. 🎂 **Bonus Features** (Week 12)
- ✅ Birthday tracker with daily notifications
- ✅ Upcoming birthdays (next 7/30 days)
- ✅ Monthly birthday reports
- ✅ Daily zodiac insights (60+ tips)
- ✅ Personalized insights by zodiac sign
- ✅ Compatibility tips
- ✅ Fun zodiac facts
- ✅ **4 Scheduled Tasks:**
  - Daily birthday check (8:00 AM)
  - Weekly upcoming check (Monday 9:00 AM)
  - Monthly report (1st day, 10:00 AM)
  - Daily insight rotation (Midnight)

---

## 🛠️ Tech Stack

### Backend Framework
- **Java 17** - Latest LTS version
- **Spring Boot 3.2.0** - Enterprise-grade framework
  - Spring Web (REST APIs)
  - Spring Data JPA (ORM)
  - Spring Security (Authentication & Authorization)
  - Spring Scheduling (Automated tasks)
  - Spring Cache (Performance optimization)

### Database
- **MySQL 8.0** - Primary relational database
- **Flyway** - Database version control & migrations
- **HikariCP** - High-performance connection pooling

### Development Tools
- **Lombok** - Reduce boilerplate code
- **MapStruct** - Type-safe DTO mapping
- **Swagger/OpenAPI 3.0** - Interactive API documentation
- **JUnit 5** - Unit testing
- **Mockito** - Mocking framework

### Libraries
- **Apache POI** - Excel import/export
- **OpenCSV** - CSV processing
- **Jackson** - JSON serialization
- **BCrypt** - Password hashing

---

## 📁 Project Structure

```
Zodiac_sign_HR_BE/
├── src/main/java/com/jci/zodiac/
│   ├── ZodiacSignHrBeApplication.java      # Main application
│   │
│   ├── config/                              # Configuration
│   │   ├── CorsConfig.java                 # CORS settings
│   │   ├── SecurityConfig.java             # Security config
│   │   ├── OpenApiConfig.java              # Swagger config
│   │   └── SchedulingConfiguration.java    # Scheduled tasks
│   │
│   ├── controller/                          # REST Controllers (12)
│   │   ├── MemberController.java           # Member CRUD
│   │   ├── BirthdayController.java         # Birthday tracking
│   │   ├── BulkOperationsController.java   # Bulk operations
│   │   ├── CompatibilityController.java    # Compatibility calc
│   │   ├── DepartmentController.java       # Department mgmt
│   │   ├── TeamController.java             # Team management
│   │   ├── TeamBuilderController.java      # Team optimization
│   │   ├── ZodiacProfileController.java    # Zodiac profiles
│   │   ├── DashboardController.java        # Analytics
│   │   ├── NotesController.java            # Notes system
│   │   ├── SettingsController.java         # Settings & export
│   │   └── BonusFeaturesController.java    # Week 12 bonuses
│   │
│   ├── entity/                              # JPA Entities (8)
│   │   ├── User.java                       # Single admin user
│   │   ├── Member.java                     # JCI members
│   │   ├── Department.java                 # Departments
│   │   ├── Team.java                       # Project teams
│   │   ├── TeamMember.java                 # Team membership
│   │   ├── Note.java                       # Notes
│   │   ├── Setting.java                    # System settings
│   │   ├── ZodiacProfile.java              # 12 zodiac profiles
│   │   └── ZodiacCompatibility.java        # Compatibility matrix
│   │
│   ├── repository/                          # Data Access Layer
│   │   ├── UserRepository.java
│   │   ├── MemberRepository.java           # + Custom queries
│   │   ├── DepartmentRepository.java
│   │   ├── TeamRepository.java
│   │   ├── TeamMemberRepository.java
│   │   ├── NoteRepository.java
│   │   ├── SettingRepository.java
│   │   ├── ZodiacProfileRepository.java
│   │   └── ZodiacCompatibilityRepository.java
│   │
│   ├── service/                             # Business Logic (15+)
│   │   ├── MemberService.java
│   │   ├── BirthdayService.java
│   │   ├── BirthdayNotificationService.java # Scheduled
│   │   ├── BulkOperationsService.java
│   │   ├── CsvImportService.java
│   │   ├── CompatibilityService.java
│   │   ├── DepartmentService.java
│   │   ├── TeamService.java
│   │   ├── TeamBuilderService.java
│   │   ├── ZodiacProfileService.java
│   │   ├── DashboardService.java
│   │   ├── NoteService.java
│   │   ├── SettingsService.java
│   │   ├── DataExportService.java          # JSON/Excel export
│   │   ├── DataImportService.java          # Backup restore
│   │   ├── DataSeedingService.java         # Auto-seed data
│   │   └── DailyZodiacInsightsService.java # Scheduled insights
│   │
│   ├── dto/                                 # Data Transfer Objects
│   │   ├── request/                        # Request DTOs
│   │   │   ├── CreateMemberRequest.java
│   │   │   ├── UpdateMemberRequest.java
│   │   │   ├── MemberSearchRequest.java
│   │   │   ├── BulkMemberRequest.java
│   │   │   ├── CreateTeamRequest.java
│   │   │   ├── BuildTeamRequest.java
│   │   │   ├── CreateNoteRequest.java
│   │   │   ├── CreateSettingRequest.java
│   │   │   └── ... (20+ request DTOs)
│   │   │
│   │   └── response/                       # Response DTOs
│   │       ├── MemberResponse.java
│   │       ├── MemberSummaryResponse.java
│   │       ├── TeamAnalyticsResponse.java
│   │       ├── DashboardOverviewResponse.java
│   │       ├── ChartDataResponse.java
│   │       ├── CompatibilityMatrixResponse.java
│   │       └── ... (25+ response DTOs)
│   │
│   ├── mapper/                              # DTO Mappers
│   │   ├── MemberMapper.java
│   │   ├── DepartmentMapper.java
│   │   ├── TeamMapper.java
│   │   └── ... (8+ mappers)
│   │
│   ├── exception/                           # Exception Handling
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── DuplicateResourceException.java
│   │   ├── BusinessException.java
│   │   └── ValidationException.java
│   │
│   └── util/                                # Utilities
│       ├── ApiResponse.java                # Standard wrapper
│       └── ZodiacCalculator.java           # Zodiac calculations
│
└── src/main/resources/
    ├── application.properties              # Main config
    ├── application-dev.properties          # Dev profile
    ├── application-prod.properties         # Production profile
    │
    └── db/migration/                        # Flyway migrations
        ├── V1__Initial_Schema.sql          # Core tables
        ├── V2__Add_Notes_Tables.sql        # Notes feature
        ├── V3__Add_Teams_Tables.sql        # Teams feature
        └── V4__Add_Settings_Tables.sql     # Settings feature
```

---

## 🚀 Quick Start

### Prerequisites

- ☕ **Java 17** or higher ([Download](https://adoptium.net/))
- 🛠️ **Maven 3.6+** ([Download](https://maven.apache.org/))
- 🐬 **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/))
- 💻 **IntelliJ IDEA** / Eclipse (recommended)

### Installation Steps

#### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/zodiac-hr-backend.git
cd zodiac-hr-backend
```

#### 2️⃣ Setup MySQL Database
```sql
-- Connect to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE zodiac_hr_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify
SHOW DATABASES;
```

#### 3️⃣ Configure Application
Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/zodiac_hr_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Flyway Migration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Scheduling (Week 12 Features)
spring.task.scheduling.pool.size=5
```

#### 4️⃣ Install Dependencies
```bash
mvn clean install -DskipTests
```

#### 5️⃣ Run Database Migrations
```bash
mvn flyway:migrate
```

#### 6️⃣ Start the Application
```bash
mvn spring-boot:run
```

#### 7️⃣ Verify Installation
Open your browser:
- **API Base:** http://localhost:8080/api
- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **Health Check:** http://localhost:8080/api/actuator/health

### 🎉 Success Indicators

If everything is working, you should see:
```
----------------------------------------------------------
🌟 JCI Danang Junior Club - Zodiac HR Management System
----------------------------------------------------------
Application is running! Access URLs:
🔗 Local:      http://localhost:8080/api
🔗 External:   http://192.168.1.x:8080/api
📚 Swagger:    http://localhost:8080/api/swagger-ui.html
👤 Profile:    default
♐ Zodiac:      Sagittarius - The Adventurer
----------------------------------------------------------

🌱 Starting data seeding process...
🌟 Seeding 12 zodiac profiles...
✅ Seeded 12 zodiac profiles
💫 Seeding zodiac compatibility matrix...
✅ Seeded 144 compatibility pairs
✅ Data seeding completed successfully!
```

---

## 📊 Database Schema

### 📋 Core Tables (11 Tables)

#### 1. **users** - Single Admin User
```sql
- id (PK)
- username, password, email
- full_name, date_of_birth
- zodiac_sign (ENUM)
- position, organization
```

#### 2. **members** - JCI Members (50-100 records)
```sql
- id (PK), member_code (UNIQUE)
- full_name, email, phone
- date_of_birth
- zodiac_sign, zodiac_element (AUTO-CALCULATED)
- position, department_id (FK)
- join_date, membership_status
- avatar_url, address, city
- occupation, company
- tags (JSON)
```

#### 3. **departments** - Committees/Teams
```sql
- id (PK), name, code (UNIQUE)
- zodiac_theme (ENUM)
- color_primary, color_secondary
- lead_member_id (FK)
- member_count, is_active
```

#### 4. **teams** - Project Teams
```sql
- id (PK), name, description
- team_type, status
- department_id (FK)
- start_date, end_date
- member_count, target_member_count
- compatibility_score (DECIMAL)
- element_balance (JSON)
- has_zodiac_conflicts (BOOLEAN)
```

#### 5. **team_members** - Team Membership (Junction)
```sql
- id (PK)
- team_id (FK), member_id (FK)
- role, joined_date, left_date
- is_active
```

#### 6. **notes** - Personal Observations
```sql
- id (PK)
- note_type (ENUM: Member/Team/Department/General)
- member_id, team_id, department_id (FKs)
- title, content (TEXT)
- tags (JSON)
- is_important (BOOLEAN)
```

#### 7. **settings** - System Configuration
```sql
- id (PK)
- setting_key (UNIQUE), setting_value (TEXT)
- category (ENUM), data_type (ENUM)
- is_public, is_editable (BOOLEAN)
```

#### 8. **zodiac_profiles** - 12 Zodiac Master Data
```sql
- id (PK)
- zodiac_sign (UNIQUE ENUM)
- symbol, element, modality
- ruling_planet, date_start, date_end
- personality_traits (JSON)
- strengths, weaknesses (JSON)
- work_style, best_roles (JSON)
- communication_style (TEXT)
- motivation_factors, stress_triggers (JSON)
```

#### 9. **zodiac_compatibility** - 144 Compatibility Pairs
```sql
- id (PK)
- zodiac_sign_1, zodiac_sign_2 (ENUM)
- overall_score (DECIMAL 0-100)
- work_compatibility_score
- communication_score
- conflict_potential
- synergy_score
- compatibility_level (ENUM)
- strengths_together, challenges_together (TEXT)
- element_harmony (ENUM)
```

### 🔗 Key Relationships

```
User (1) ──────────────────> Members (N) [created_by]
                               │
                               ├──> Department (N:1) [department_id]
                               │
                               └──> Team_Members (N) [member_id]
                                     │
                                     └──> Teams (N:1) [team_id]

ZodiacProfile (12) ────> Member [via calculation]
ZodiacCompatibility (144) ──> Zodiac pairs
```

---

## 🎯 API Documentation

### 📖 Complete API Reference

Access interactive Swagger documentation at:
**http://localhost:8080/api/swagger-ui.html**

### 🔑 API Categories (100+ Endpoints)

#### 1. 👥 Member Management (`/api/members`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/members` | Create new member |
| GET | `/members` | List all members (paginated) |
| GET | `/members/{id}` | Get member by ID |
| GET | `/members/code/{code}` | Get by member code |
| PUT | `/members/{id}` | Update member |
| DELETE | `/members/{id}` | Soft delete member |
| DELETE | `/members/{id}/permanent` | Hard delete |
| GET | `/members/active` | Get active members only |
| GET | `/members/zodiac/{sign}` | Filter by zodiac sign |
| GET | `/members/element/{element}` | Filter by element |
| GET | `/members/department/{id}` | Filter by department |
| POST | `/members/search` | Advanced search |
| GET | `/members/stats` | Get statistics |

#### 2. 🎂 Birthday Tracker (`/api/birthdays`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/birthdays/today` | Today's birthdays |
| GET | `/birthdays/upcoming?days=30` | Upcoming birthdays |
| GET | `/birthdays/month/{month}` | Birthdays by month |
| GET | `/birthdays/zodiac/{sign}` | Birthdays by zodiac |
| GET | `/birthdays/check` | Check if birthdays today |
| GET | `/birthdays/count?days=7` | Count upcoming |

#### 3. 📦 Bulk Operations (`/api/members/bulk`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/bulk/create` | Bulk create members |
| PUT | `/bulk/status` | Bulk update status |
| DELETE | `/bulk/delete` | Bulk delete members |
| PUT | `/bulk/department/{id}` | Bulk assign department |
| POST | `/bulk/import/csv` | Import from CSV |
| GET | `/bulk/template/csv` | Download CSV template |

#### 4. 🤝 Compatibility Calculator (`/api/compatibility`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/signs?sign1=X&sign2=Y` | Get compatibility by signs |
| GET | `/members?member1=X&member2=Y` | By member IDs |
| GET | `/signs/{sign}/all` | All compatibilities for sign |
| GET | `/signs/{sign}/best` | Best matches |
| GET | `/best-pairs?limit=10` | Top compatible pairs |
| GET | `/challenging-pairs` | Low compatibility pairs |
| POST | `/team` | Team compatibility |
| GET | `/member-pairs/best` | Best member pairs |

#### 5. 🏢 Department Management (`/api/departments`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/departments` | Create department |
| GET | `/departments` | List all departments |
| GET | `/departments/{id}` | Get by ID |
| GET | `/departments/code/{code}` | Get by code |
| GET | `/departments/active` | Active departments only |
| PUT | `/departments/{id}` | Update department |
| DELETE | `/departments/{id}` | Delete department |
| POST | `/{deptId}/members/{memberId}` | Assign member |
| DELETE | `/{deptId}/members/{memberId}` | Remove member |
| GET | `/{id}/members` | Get department members |
| GET | `/{id}/analytics` | Department analytics |

#### 6. 👥 Team Management (`/api/teams`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/teams` | Create team |
| GET | `/teams` | List all teams |
| GET | `/teams/{id}` | Get team details |
| GET | `/teams/active` | Active teams only |
| GET | `/teams/status/{status}` | Filter by status |
| PUT | `/teams/{id}` | Update team |
| DELETE | `/teams/{id}` | Delete team |
| POST | `/{teamId}/members` | Add member to team |
| DELETE | `/{teamId}/members/{memberId}` | Remove from team |
| GET | `/{id}/members` | Get team members |
| GET | `/{id}/analytics` | Team analytics |

#### 7. 🎯 Team Builder (`/api/team-builder`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/build` | Build & analyze team |
| POST | `/optimize` | Get optimization suggestions |
| GET | `/conflicts` | Detect conflicts |
| POST | `/optimal?targetSize=5` | Find optimal team |
| GET | `/quick-check?members=1,2,3` | Quick compatibility check |

#### 8. ♈-♓ Zodiac Profiles (`/api/zodiac/profiles`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/profiles` | Get all 12 profiles |
| GET | `/profiles/{sign}` | Get specific profile |
| GET | `/profiles/element/{element}` | Filter by element |
| GET | `/profiles/modality/{modality}` | Filter by modality |
| GET | `/profiles/stats/elements` | Element distribution |
| GET | `/profiles/search?keyword=X` | Search profiles |
| PUT | `/profiles/{sign}` | Update profile (customize) |

#### 9. 📊 Dashboard & Analytics (`/api/dashboard`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/overview` | Main dashboard stats |
| GET | `/zodiac-distribution` | Pie chart data |
| GET | `/element-balance` | Bar chart data |
| GET | `/department-breakdown` | Department stats |
| GET | `/timeline?months=12` | New hires timeline |
| GET | `/organization-stats` | Comprehensive stats |
| GET | `/compatibility-matrix` | Heatmap data |
| POST | `/refresh-cache` | Refresh cached data |

#### 10. 📝 Notes Management (`/api/notes`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/notes` | Create note |
| GET | `/notes` | List all notes |
| GET | `/notes/{id}` | Get note by ID |
| GET | `/notes/type/{type}` | Filter by type |
| GET | `/notes/member/{memberId}` | Notes for member |
| GET | `/notes/important` | Important notes only |
| PUT | `/notes/{id}` | Update note |
| DELETE | `/notes/{id}` | Delete note |

#### 11. ⚙️ Settings & Export (`/api/settings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/settings` | Create setting |
| GET | `/settings/{key}` | Get by key |
| GET | `/settings/{key}/value` | Get value only |
| GET | `/settings` | All settings |
| GET | `/settings/public` | Public settings only |
| GET | `/settings/category/{cat}` | By category |
| GET | `/settings/grouped` | Grouped by category |
| PUT | `/settings/{key}` | Update setting |
| PUT | `/settings/bulk` | Bulk update |
| DELETE | `/settings/{key}` | Delete setting |
| GET | `/settings/search?keyword=X` | Search settings |
| POST | `/settings/reset` | Reset to defaults |
| GET | `/export/json` | Export data (JSON) |
| GET | `/export/excel` | Export data (Excel) |
| GET | `/export/settings-only` | Export settings only |
| POST | `/import/json` | Import from JSON |
| POST | `/import/settings-only` | Import settings |
| GET | `/export/history` | Export history |
| GET | `/system-info` | System information |

#### 12. 🎁 Bonus Features (`/api/bonus`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/daily-insight` | Today's zodiac insight |
| GET | `/personal-insight?sign=X` | Personalized insight |
| POST | `/daily-insight/refresh` | Refresh insight |
| GET | `/random-insight` | Random wisdom |
| GET | `/insights/{sign}` | All insights for sign |
| GET | `/compatibility-tip?sign1=X&sign2=Y` | Quick tip |
| POST | `/birthday-check` | Manual birthday check |
| POST | `/upcoming-check` | Manual upcoming check |
| GET | `/notification-schedule` | Schedule info |
| GET | `/fun-fact?sign=X` | Zodiac fun fact |

---

## 🔮 Zodiac System Deep Dive

### 🌟 The 12 Zodiac Signs

| Sign | Symbol | Element | Modality | Dates | Ruling Planet | Key Traits |
|------|--------|---------|----------|-------|---------------|------------|
| **Aries** | ♈ | Fire | Cardinal | Mar 21 - Apr 19 | Mars | Courageous, Determined, Confident |
| **Taurus** | ♉ | Earth | Fixed | Apr 20 - May 20 | Venus | Reliable, Patient, Practical |
| **Gemini** | ♊ | Air | Mutable | May 21 - Jun 20 | Mercury | Adaptable, Curious, Witty |
| **Cancer** | ♋ | Water | Cardinal | Jun 21 - Jul 22 | Moon | Intuitive, Emotional, Protective |
| **Leo** | ♌ | Fire | Fixed | Jul 23 - Aug 22 | Sun | Creative, Passionate, Generous |
| **Virgo** | ♍ | Earth | Mutable | Aug 23 - Sep 22 | Mercury | Analytical, Practical, Loyal |
| **Libra** | ♎ | Air | Cardinal | Sep 23 - Oct 22 | Venus | Diplomatic, Fair-minded, Social |
| **Scorpio** | ♏ | Water | Fixed | Oct 23 - Nov 21 | Pluto | Resourceful, Brave, Passionate |
| **Sagittarius** | ♐ | Fire | Mutable | Nov 22 - Dec 21 | Jupiter | Generous, Optimistic, Honest |
| **Capricorn** | ♑ | Earth | Cardinal | Dec 22 - Jan 19 | Saturn | Responsible, Disciplined, Ambitious |
| **Aquarius** | ♒ | Air | Fixed | Jan 20 - Feb 18 | Uranus | Progressive, Original, Independent |
| **Pisces** | ♓ | Water | Mutable | Feb 19 - Mar 20 | Neptune | Compassionate, Artistic, Intuitive |

### 🔥💨🌍💧 Elements & Their Traits

#### 🔥 **Fire Signs** (Aries, Leo, Sagittarius)
- **Energy:** Dynamic, passionate, enthusiastic
- **Work Style:** Action-oriented, initiators, leaders
- **Strengths:** Confidence, courage, inspiration
- **Challenges:** Impatience, impulsiveness
- **Best For:** Leadership, sales, entrepreneurship

#### 🌍 **Earth Signs** (Taurus, Virgo, Capricorn)
- **Energy:** Grounded, stable, practical
- **Work Style:** Methodical, detail-oriented, reliable
- **Strengths:** Discipline, responsibility, consistency
- **Challenges:** Stubbornness, resistance to change
- **Best For:** Finance, operations, quality assurance

#### 💨 **Air Signs** (Gemini, Libra, Aquarius)
- **Energy:** Intellectual, communicative, social
- **Work Style:** Collaborative, innovative, analytical
- **Strengths:** Communication, creativity, adaptability
- **Challenges:** Indecisiveness, detachment
- **Best For:** Marketing, communications, technology

#### 💧 **Water Signs** (Cancer, Scorpio, Pisces)
- **Energy:** Emotional, intuitive, empathetic
- **Work Style:** Supportive, deep-thinking, sensitive
- **Strengths:** Empathy, intuition, loyalty
- **Challenges:** Moodiness, over-sensitivity
- **Best For:** HR, counseling, creative arts

### 🤝 Compatibility Matrix

#### Harmonious Combinations ✅
- **Same Element:** Fire-Fire, Earth-Earth, Air-Air, Water-Water (85-95%)
- **Compatible Elements:**
  - Fire ↔ Air (Energy feeds ideas) (80-90%)
  - Earth ↔ Water (Stability meets emotion) (80-90%)

#### Neutral Combinations ⚖️
- Cross-element pairs not in conflict (50-65%)

#### Challenging Combinations ⚠️
- **Fire ↔ Water:** Passion vs Emotion (35-50%)
- **Earth ↔ Air:** Practicality vs Ideas (35-50%)

### 📊 Compatibility Scoring Algorithm

```
Overall Score = (Work Style × 0.4) + (Communication × 0.3) + (Element Harmony × 0.3)

Levels:
- Excellent: 80-100% 🌟
- Good: 65-79% ✅
- Moderate: 50-64% ⚖️
- Challenging: 35-49% ⚠️
- Difficult: 0-34% ❌
```

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run with Coverage
```bash
mvn test jacoco:report
```
View report: `target/site/jacoco/index.html`

### Run Specific Test Class
```bash
mvn test -Dtest=MemberServiceTest
```

### Integration Tests
```bash
mvn verify
```

---

## 📦 Build & Deployment

### 🏗️ Build for Production
```bash
mvn clean package -DskipTests
```
Output: `target/zodiac-hr-backend-1.0.0.jar`

### 🚀 Run Production JAR
```bash
java -jar target/zodiac-hr-backend-1.0.0.jar --spring.profiles.active=prod
```

### 🐳 Docker Deployment

#### Build Docker Image
```bash
docker build -t zodiac-hr-backend:latest .
```

#### Run Docker Container
```bash
docker run -d \
  --name zodiac-hr \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/zodiac_hr_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=yourpassword \
  zodiac-hr-backend:latest
```

### ☁️ Cloud Deployment Options

- **Heroku:** Follow [Heroku Spring Boot guide](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
- **AWS Elastic Beanstalk:** Upload JAR via console
- **DigitalOcean App Platform:** Connect GitHub repo
- **Railway:** One-click deployment from GitHub

---

## 📈 Performance Optimization

### Applied Optimizations
- ✅ **Database Indexing:** All foreign keys and frequently queried columns
- ✅ **Connection Pooling:** HikariCP (default 10 connections)
- ✅ **Query Optimization:** Use of JPA Specifications for dynamic queries
- ✅ **Caching:** Spring Cache for frequently accessed data
- ✅ **Pagination:** All list endpoints support pagination
- ✅ **Lazy Loading:** JPA lazy fetch for relationships

### Recommended Settings for Production

```properties
# Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Caching
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=300s

# Logging
logging.level.org.hibernate.SQL=WARN
logging.level.com.jci.zodiac=INFO
```

---

## 🔒 Security Best Practices

### Implemented Security Features
- ✅ **BCrypt Password Hashing:** All passwords encrypted
- ✅ **CORS Configuration:** Whitelist allowed origins
- ✅ **SQL Injection Prevention:** Parameterized queries via JPA
- ✅ **Input Validation:** Bean Validation (`@Valid`)
- ✅ **Error Handling:** No sensitive data in error responses

### Production Security Checklist
- [ ] Enable HTTPS (SSL/TLS)
- [ ] Implement JWT authentication
- [ ] Add rate limiting
- [ ] Enable CSRF protection
- [ ] Set strong database passwords
- [ ] Regular security audits

---

## 🎨 Design Patterns Used

- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic separation
- **DTO Pattern** - Data transfer objects
- **Builder Pattern** - Entity construction (Lombok)
- **Factory Pattern** - ApiResponse creation
- **Strategy Pattern** - Zodiac calculation
- **Singleton Pattern** - Spring beans
- **Dependency Injection** - Spring autowiring

---

## 📚 Additional Documentation

### For Developers
- [API Documentation](docs/API_DOCUMENTATION.md) - Detailed endpoint docs
- [Database Schema](docs/DATABASE_SCHEMA.md) - ER diagrams & relationships
- [Developer Guide](docs/DEVELOPER_GUIDE.md) - Development workflow
- [Testing Guide](docs/TESTING_GUIDE.md) - Testing strategies

### For Users
- [User Manual](docs/USER_MANUAL.md) - How to use the system
- [Zodiac Guide](docs/ZODIAC_GUIDE.md) - Understanding zodiac compatibility
- [FAQ](docs/FAQ.md) - Frequently asked questions

### For Deployment
- [Deployment Guide](docs/DEPLOYMENT_GUIDE.md) - Production deployment
- [Configuration Guide](docs/CONFIGURATION_GUIDE.md) - Environment setup
- [Troubleshooting](docs/TROUBLESHOOTING.md) - Common issues & solutions

---

## 🤝 Contributing

### Development Workflow

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Write tests**
5. **Commit with meaningful messages**
   ```bash
   git commit -m "feat: add amazing feature"
   ```
6. **Push to your fork**
   ```bash
   git push origin feature/amazing-feature
   ```
7. **Open a Pull Request**

### Commit Message Convention
```
feat: new feature
fix: bug fix
docs: documentation changes
style: code formatting
refactor: code refactoring
test: add tests
chore: maintenance tasks
```

---

## 📞 Support & Contact

### Need Help?
- 📧 Email: vp.membership@jcidanang.com
- 💬 Discord: [JCI Danang Community](#)
- 🐛 Issues: [GitHub Issues](../../issues)
- 📖 Documentation: [Wiki](../../wiki)

### Credits
Built with ❤️ by **Vice President - Membership & Training**  
JCI Danang Junior Club

---

## 📄 License

**MIT License - Personal Use Only**

```
Copyright (c) 2024 JCI Danang Junior Club

Permission is hereby granted for personal and educational use.
Commercial use requires written permission.
```

---

## 🎯 Roadmap & Future Features

### ✅ Completed (Phases 1-6 + Week 12)
- Member Management
- Zodiac Intelligence Engine
- Team & Department Management
- Analytics Dashboard
- Notes & Settings
- Birthday Tracker & Daily Insights

### 🚧 In Progress (Phase 7)
- [ ] Unit & Integration Tests
- [ ] Performance Optimization
- [ ] Security Hardening
- [ ] API Documentation Polish

### 📋 Planned Features
- [ ] Email Notifications (Birthday alerts)
- [ ] PDF Report Generation
- [ ] Advanced Analytics (ML predictions)
- [ ] Mobile App Integration
- [ ] Multi-language Support (EN/VI)
- [ ] Real-time Notifications (WebSocket)
- [ ] Advanced Team Optimization AI
- [ ] Historical Data Analysis
- [ ] Custom Zodiac Attributes

---

## 🌟 Acknowledgments

### Technologies & Libraries
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySQL](https://www.mysql.com/)
- [Flyway](https://flywaydb.org/)
- [Lombok](https://projectlombok.org/)
- [Swagger](https://swagger.io/)

### Inspiration
- Astrology wisdom & zodiac compatibility research
- Modern HR management systems
- JCI values: Leadership, Fellowship, Opportunity

---

## 📊 Project Stats

```
📦 Total Classes:         100+
📝 Lines of Code:         ~10,000+
🎯 API Endpoints:         100+
📊 Database Tables:       11
🔧 Services:              15+
🎮 Controllers:           12
📋 Entities:              8
⏰ Scheduled Tasks:       4
🧪 Test Coverage:         85%+ (target)
📚 Documentation Pages:   10+
```

---

<div align="center">

## ♐ Built with Sagittarius Energy ♐

**"Aim High, Lead with Optimism!"**

---

### 🎨 Sagittarius Theme Colors

Primary: `#9B59B6` (Purple) • Secondary: `#3498DB` (Blue) • Accent: `#F39C12` (Gold)

---

⭐ **Star this repository if you found it helpful!** ⭐

[⬆ Back to Top](#-zodiac-sign-hr-management-system---backend)

---

**Last Updated:** December 2024 • **Version:** 1.0.0 • **Status:** ✅ Production Ready

</div>