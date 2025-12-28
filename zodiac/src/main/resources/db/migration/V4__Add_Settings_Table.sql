-- =====================================================
-- V4: Add Settings Table for System Configuration (FIXED)
-- Supports key-value storage for flexible settings
-- =====================================================

CREATE TABLE settings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    -- Key-value structure
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,

    -- Metadata
    category ENUM('GENERAL', 'ORGANIZATION', 'ZODIAC', 'COMPATIBILITY', 'SYSTEM', 'FEATURE', 'NOTIFICATION', 'BACKUP') DEFAULT 'GENERAL',
    description TEXT,
    data_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') DEFAULT 'STRING',

    -- Access control
    is_public BOOLEAN DEFAULT FALSE,
    is_editable BOOLEAN DEFAULT TRUE,

    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by BIGINT DEFAULT 1,

    INDEX idx_key (setting_key),
    INDEX idx_category (category),
    INDEX idx_public (is_public),

    FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default settings (với ENUM values chính xác)
INSERT INTO settings (setting_key, setting_value, category, description, data_type, is_public, is_editable) VALUES
-- Organization Settings
('organization.name', 'JCI Danang Junior Club', 'ORGANIZATION', 'Organization name', 'STRING', true, true),
('organization.code', 'JCI-DN', 'ORGANIZATION', 'Organization code prefix', 'STRING', true, false),
('organization.motto', 'Aim High, Lead with Optimism!', 'ORGANIZATION', 'Organization motto', 'STRING', true, true),
('organization.email', 'vp.membership@jcidanang.com', 'ORGANIZATION', 'Contact email', 'STRING', true, true),

-- Zodiac Theme Settings
('zodiac.theme.primary_sign', 'Sagittarius', 'ZODIAC', 'Primary zodiac theme', 'STRING', true, true),
('zodiac.theme.color_primary', '#9B59B6', 'ZODIAC', 'Primary color (Sagittarius purple)', 'STRING', true, true),
('zodiac.theme.color_secondary', '#3498DB', 'ZODIAC', 'Secondary color (Sagittarius blue)', 'STRING', true, true),
('zodiac.theme.symbol', '♐', 'ZODIAC', 'Theme zodiac symbol', 'STRING', true, false),

-- Compatibility Algorithm Weights
('compatibility.weight.work_style', '40', 'COMPATIBILITY', 'Work style weight (%)', 'NUMBER', false, true),
('compatibility.weight.communication', '30', 'COMPATIBILITY', 'Communication weight (%)', 'NUMBER', false, true),
('compatibility.weight.element', '30', 'COMPATIBILITY', 'Element compatibility weight (%)', 'NUMBER', false, true),
('compatibility.threshold.excellent', '80', 'COMPATIBILITY', 'Excellent threshold (%)', 'NUMBER', false, true),
('compatibility.threshold.good', '65', 'COMPATIBILITY', 'Good threshold (%)', 'NUMBER', false, true),
('compatibility.threshold.moderate', '50', 'COMPATIBILITY', 'Moderate threshold (%)', 'NUMBER', false, true),

-- System Settings
('system.date_format', 'yyyy-MM-dd', 'SYSTEM', 'Default date format', 'STRING', true, true),
('system.language', 'en', 'SYSTEM', 'System language (en/vi)', 'STRING', true, true),
('system.timezone', 'Asia/Ho_Chi_Minh', 'SYSTEM', 'System timezone', 'STRING', false, true),
('system.pagination.default_size', '20', 'SYSTEM', 'Default page size', 'NUMBER', false, true),
('system.pagination.max_size', '100', 'SYSTEM', 'Maximum page size', 'NUMBER', false, false),

-- Feature Flags
('feature.birthday_tracker', 'true', 'FEATURE', 'Enable birthday tracking', 'BOOLEAN', false, true),
('feature.daily_insights', 'true', 'FEATURE', 'Enable daily zodiac insights', 'BOOLEAN', false, true),
('feature.team_builder', 'true', 'FEATURE', 'Enable team builder tool', 'BOOLEAN', false, true),
('feature.export_pdf', 'true', 'FEATURE', 'Enable PDF export', 'BOOLEAN', false, true),
('feature.export_excel', 'true', 'FEATURE', 'Enable Excel export', 'BOOLEAN', false, true),

-- Notification Settings
('notification.birthday_reminder_days', '7', 'NOTIFICATION', 'Days before birthday to remind', 'NUMBER', false, true),
('notification.email_enabled', 'false', 'NOTIFICATION', 'Enable email notifications', 'BOOLEAN', false, true),

-- Backup Settings
('backup.auto_enabled', 'false', 'BACKUP', 'Enable automatic backups', 'BOOLEAN', false, true),
('backup.frequency_days', '7', 'BACKUP', 'Backup frequency in days', 'NUMBER', false, true),
('backup.last_backup_date', NULL, 'BACKUP', 'Last backup timestamp', 'STRING', false, false);