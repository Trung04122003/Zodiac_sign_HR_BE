-- =====================================================
-- V3: Add Teams and Team Members Tables
-- =====================================================

-- Table: teams (Project teams/working groups)
CREATE TABLE teams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL,
    description TEXT,
    department_id BIGINT,

    -- Team Type
    team_type ENUM('Project', 'Committee', 'WorkingGroup', 'TaskForce') DEFAULT 'Project',

    -- Timeline
    start_date DATE,
    end_date DATE,
    status ENUM('Planning', 'Active', 'OnHold', 'Completed', 'Cancelled') DEFAULT 'Planning',

    -- Team Composition
    member_count INT DEFAULT 0,
    target_member_count INT,

    -- Zodiac Analytics
    compatibility_score DECIMAL(5,2),
    element_balance JSON,
    has_zodiac_conflicts BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 1,

    INDEX idx_department (department_id),
    INDEX idx_status (status),
    INDEX idx_team_type (team_type),
    INDEX idx_dates (start_date, end_date),

    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Table: team_members (Junction table)
CREATE TABLE team_members (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    team_id BIGINT NOT NULL,
    member_id BIGINT NOT NULL,

    -- Role in Team
    role VARCHAR(50) DEFAULT 'Member',

    -- Timeline
    joined_date DATE NOT NULL,
    left_date DATE,
    is_active BOOLEAN DEFAULT TRUE,

    -- Notes
    notes TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    UNIQUE KEY unique_team_member (team_id, member_id),
    INDEX idx_team (team_id),
    INDEX idx_member (member_id),
    INDEX idx_active (is_active),

    FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;