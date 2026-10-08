-- =====================================================
-- V1__create_assessment_tables.sql
-- Smart Assessment Platform - Initial Schema
-- =====================================================

-- Assessment Table
CREATE TABLE IF NOT EXISTS assessments (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    description    TEXT,
    duration_minutes INT NOT NULL,
    total_marks    INT,
    status         VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
    difficulty     VARCHAR(50),
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Assessment Instructions Table
CREATE TABLE IF NOT EXISTS assessment_instructions (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id  BIGINT NOT NULL,
    instruction    TEXT NOT NULL,
    CONSTRAINT fk_instruction_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id)
        ON DELETE CASCADE
);

-- Assessment Sections Table
CREATE TABLE IF NOT EXISTS assessment_sections (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id  BIGINT NOT NULL,
    section_name   VARCHAR(255) NOT NULL,
    section_type   VARCHAR(50),
    marks_per_question INT,
    total_questions    INT,
    CONSTRAINT fk_section_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id)
        ON DELETE CASCADE
);

-- Assessment Question Set Table (links section to question bank)
CREATE TABLE IF NOT EXISTS assessment_question_sets (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    section_id         BIGINT NOT NULL,
    topic_id           BIGINT,
    difficulty         VARCHAR(50),
    num_questions      INT NOT NULL,
    distribution_type  VARCHAR(50),
    CONSTRAINT fk_qset_section
        FOREIGN KEY (section_id) REFERENCES assessment_sections(id)
        ON DELETE CASCADE
);

-- Passing Criteria Table
CREATE TABLE IF NOT EXISTS passing_criteria (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id   BIGINT NOT NULL UNIQUE,
    passing_percentage DECIMAL(5,2) NOT NULL,
    passing_marks   INT,
    CONSTRAINT fk_criteria_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id)
        ON DELETE CASCADE
);

-- Proctoring Rules Table
CREATE TABLE IF NOT EXISTS proctoring_rules (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id       BIGINT NOT NULL UNIQUE,
    is_camera_required  BOOLEAN DEFAULT FALSE,
    is_screen_share     BOOLEAN DEFAULT FALSE,
    max_tab_switches    INT DEFAULT 0,
    CONSTRAINT fk_proctoring_assessment
        FOREIGN KEY (assessment_id) REFERENCES assessments(id)
        ON DELETE CASCADE
);
