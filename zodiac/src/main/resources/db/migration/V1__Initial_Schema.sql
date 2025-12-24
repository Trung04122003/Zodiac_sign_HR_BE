-- =====================================================
-- JCI DANANG JUNIOR CLUB - ZODIAC HR MANAGEMENT SYSTEM
-- Initial Database Schema
-- Vice President: Membership & Training ♐
-- =====================================================

-- Table 1: users (Single User - YOU!)
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE NOT NULL,
    zodiac_sign ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                     'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces') NOT NULL,
    avatar_url VARCHAR(500),
    organization VARCHAR(100) DEFAULT 'JCI Danang Junior Club',
    position VARCHAR(100) DEFAULT 'Vice President - Membership & Training',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,

    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table 2: members (JCI Members)
CREATE TABLE members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    member_code VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    date_of_birth DATE NOT NULL,
    zodiac_sign ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                     'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces') NOT NULL,
    zodiac_element ENUM('Fire', 'Earth', 'Air', 'Water') NOT NULL,

    -- JCI Specific
    position VARCHAR(100),
    department_id BIGINT,
    join_date DATE NOT NULL,
    membership_status ENUM('Active', 'Inactive', 'On Leave', 'Alumni') DEFAULT 'Active',
    membership_type ENUM('Full Member', 'Associate', 'Honorary') DEFAULT 'Full Member',

    -- Contact & Personal
    avatar_url VARCHAR(500),
    address TEXT,
    city VARCHAR(50) DEFAULT 'Da Nang',
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),

    -- Professional
    occupation VARCHAR(100),
    company VARCHAR(100),
    linkedin_url VARCHAR(200),

    -- Metadata
    notes TEXT,
    tags JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 1,

    INDEX idx_member_code (member_code),
    INDEX idx_email (email),
    INDEX idx_zodiac_sign (zodiac_sign),
    INDEX idx_zodiac_element (zodiac_element),
    INDEX idx_status (membership_status),
    INDEX idx_join_date (join_date),
    FULLTEXT INDEX idx_fulltext_search (full_name, email, position, occupation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table 3: departments (JCI Committees/Teams)
CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,

    zodiac_theme ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                      'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces'),
    color_primary VARCHAR(7),
    color_secondary VARCHAR(7),
    icon_url VARCHAR(500),

    lead_member_id BIGINT,
    member_count INT DEFAULT 0,
    active_projects_count INT DEFAULT 0,

    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_code (code),
    INDEX idx_is_active (is_active),
    INDEX idx_zodiac_theme (zodiac_theme)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table 4: zodiac_profiles (Master Zodiac Data)
CREATE TABLE zodiac_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    zodiac_sign ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                     'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces') UNIQUE NOT NULL,

    symbol VARCHAR(10),
    element ENUM('Fire', 'Earth', 'Air', 'Water') NOT NULL,
    modality ENUM('Cardinal', 'Fixed', 'Mutable') NOT NULL,
    ruling_planet VARCHAR(20),

    date_start VARCHAR(10),
    date_end VARCHAR(10),

    color_primary VARCHAR(7),
    color_secondary VARCHAR(7),
    color_gradient_start VARCHAR(7),
    color_gradient_end VARCHAR(7),

    personality_traits JSON NOT NULL,
    strengths JSON NOT NULL,
    weaknesses JSON NOT NULL,
    work_style JSON NOT NULL,
    best_roles JSON,
    communication_style TEXT,
    motivation_factors JSON,
    stress_triggers JSON,

    leadership_style TEXT,
    team_contribution TEXT,
    conflict_resolution_style TEXT,

    description_long TEXT,
    famous_people JSON,
    custom_attributes JSON,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_element (element),
    INDEX idx_modality (modality)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table 5: zodiac_compatibility (Compatibility Matrix)
CREATE TABLE zodiac_compatibility (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    zodiac_sign_1 ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                       'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces') NOT NULL,
    zodiac_sign_2 ENUM('Aries', 'Taurus', 'Gemini', 'Cancer', 'Leo', 'Virgo',
                       'Libra', 'Scorpio', 'Sagittarius', 'Capricorn', 'Aquarius', 'Pisces') NOT NULL,

    overall_score DECIMAL(5,2) NOT NULL,
    work_compatibility_score DECIMAL(5,2),
    communication_score DECIMAL(5,2),
    conflict_potential DECIMAL(5,2),
    synergy_score DECIMAL(5,2),

    compatibility_level ENUM('Excellent', 'Good', 'Moderate', 'Challenging', 'Difficult'),
    strengths_together TEXT,
    challenges_together TEXT,
    management_tips TEXT,
    best_collaboration_type VARCHAR(100),

    element_harmony ENUM('Harmonious', 'Neutral', 'Challenging'),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE KEY unique_pair (zodiac_sign_1, zodiac_sign_2),
    INDEX idx_sign1 (zodiac_sign_1),
    INDEX idx_sign2 (zodiac_sign_2),
    INDEX idx_compatibility (compatibility_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add Foreign Keys
ALTER TABLE members
    ADD CONSTRAINT fk_members_department
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_members_created_by
    FOREIGN KEY (created_by) REFERENCES users(id);

ALTER TABLE departments
    ADD CONSTRAINT fk_departments_lead
    FOREIGN KEY (lead_member_id) REFERENCES members(id) ON DELETE SET NULL;

-- Insert default user (YOU - Sagittarius VP!)
INSERT INTO users (username, password, full_name, email, date_of_birth, zodiac_sign, position)
VALUES (
    'admin',
    '$2a$10$dummyHashForNow', -- Will be updated with actual BCrypt hash
    'Vice President - Membership & Training',
    'vp.membership@jcidanang.com',
    '2003-12-04',
    'Sagittarius',
    'Vice President - Membership & Training'
);