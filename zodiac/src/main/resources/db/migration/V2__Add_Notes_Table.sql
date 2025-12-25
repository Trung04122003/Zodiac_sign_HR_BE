-- =====================================================
-- V2: Add Notes Table for Personal Observations
-- =====================================================

CREATE TABLE notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    note_type ENUM('Member', 'Team', 'Department', 'General') NOT NULL,

    member_id BIGINT,
    team_id BIGINT,
    department_id BIGINT,

    title VARCHAR(200),
    content TEXT NOT NULL,
    tags JSON,

    is_important BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT DEFAULT 1,

    INDEX idx_note_type (note_type),
    INDEX idx_member (member_id),
    INDEX idx_important (is_important),
    FULLTEXT INDEX idx_search (title, content),

    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;