-- ===================================================================
-- Flyway Migration V2: Seed Data for Assessments, Sections, Proctoring & Enrollments
-- ===================================================================

INSERT INTO assessments (
    id, title, code, description, instructions, category, status, access_type, passcode,
    total_duration_minutes, total_marks, passing_marks, passing_percentage,
    valid_from, valid_to, shuffle_questions, shuffle_options, allow_navigation_back,
    allow_review, allow_retake, max_retake_attempts, auto_submit_on_expiry,
    connection_grace_period_minutes, dynamic_questionnaire_enabled, show_results_immediately,
    created_by, updated_by, created_at, updated_at, is_deleted
) VALUES (
    1,
    'Java Senior Software Engineer Assessment',
    'JAVA-SR-2026',
    'Comprehensive technical evaluation covering Java 21, Spring Boot microservices, multithreading, and live algorithmic problem solving.',
    '1. Ensure you have a functioning webcam and microphone before beginning.\n2. Do not leave fullscreen mode or switch browser tabs.\n3. The assessment consists of 3 sections: MCQ, Coding, and Debugging.\n4. You have 90 minutes in total. Your code and answers are autosaved continuously.',
    'Software Engineering',
    'PUBLISHED',
    'INVITE_ONLY',
    'JAVA2026PASS',
    90,
    100.00,
    60.00,
    60.00,
    '2026-01-01 00:00:00',
    '2026-12-31 23:59:59',
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    FALSE,
    1,
    TRUE,
    10,
    TRUE,
    FALSE,
    'admin-101',
    'admin-101',
    NOW(),
    NOW(),
    FALSE
);

-- Section 1: Multiple Choice Questions (Core Java, Spring, Architecture)
INSERT INTO assessment_sections (
    id, assessment_id, title, description, section_order, section_duration_minutes,
    cutoff_marks, weightage_percentage, question_type, total_questions_to_ask,
    easy_question_count, medium_question_count, hard_question_count, question_set_id,
    created_at, updated_at
) VALUES (
    1, 1, 'Section 1 - Core Java & Spring Microservices (MCQ)',
    'Assessment on JVM memory model, garbage collection, virtual threads, and Spring Boot architecture.',
    1, 30, 15.00, 30.00, 'MCQ', 15, 5, 7, 3, 'QSET-JAVA-MCQ-01', NOW(), NOW()
);

-- Section 2: Live Coding (Monaco Editor & Docker Sandbox)
INSERT INTO assessment_sections (
    id, assessment_id, title, description, section_order, section_duration_minutes,
    cutoff_marks, weightage_percentage, question_type, total_questions_to_ask,
    easy_question_count, medium_question_count, hard_question_count, question_set_id,
    created_at, updated_at
) VALUES (
    2, 1, 'Section 2 - Algorithmic Coding Challenge',
    'Live coding task evaluated against automated test cases, computational complexity, and code quality.',
    2, 40, 25.00, 50.00, 'CODING', 2, 0, 1, 1, 'QSET-JAVA-CODE-01', NOW(), NOW()
);

-- Section 3: Debugging & Code Comprehension
INSERT INTO assessment_sections (
    id, assessment_id, title, description, section_order, section_duration_minutes,
    cutoff_marks, weightage_percentage, question_type, total_questions_to_ask,
    easy_question_count, medium_question_count, hard_question_count, question_set_id,
    created_at, updated_at
) VALUES (
    3, 1, 'Section 3 - Code Comprehension & Debugging',
    'Identify race conditions and fix memory leaks in an existing distributed cache service code snippet.',
    3, 20, 10.00, 20.00, 'DEBUGGING', 1, 0, 1, 0, 'QSET-JAVA-DEBUG-01', NOW(), NOW()
);

-- Proctoring Rules for Java Assessment
INSERT INTO assessment_proctoring_rules (
    id, assessment_id, camera_required, mic_required, fullscreen_enforced,
    tab_switch_limit, face_detection_required, multiple_faces_forbidden,
    phone_detection_forbidden, copy_paste_forbidden, code_similarity_check,
    screen_share_required, auto_terminate_on_violations, warning_threshold,
    created_at, updated_at
) VALUES (
    1, 1, TRUE, TRUE, TRUE, 3, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, 3, NOW(), NOW()
);

-- Sample Enrollment for Testing
INSERT INTO assessment_enrollments (
    id, assessment_id, candidate_id, candidate_email, candidate_name,
    enrollment_status, invitation_token, scheduled_start_time, scheduled_end_time,
    attempt_count, enrolled_by, enrolled_at, expires_at
) VALUES (
    1, 1, 'cand-1001', 'candidate1@example.com', 'Jane Doe',
    'ENROLLED', 'token-jane-doe-java-2026', '2026-10-01 10:00:00', '2026-10-31 23:59:59',
    0, 'admin-101', NOW(), '2026-10-31 23:59:59'
);

-- Sample Audit Log
INSERT INTO assessment_audit_logs (
    id, assessment_id, user_id, user_role, action, description,
    old_value_json, new_value_json, ip_address, timestamp
) VALUES (
    1, 1, 'admin-101', 'ADMIN', 'PUBLISHED',
    'Assessment published by admin-101',
    '{"status": "DRAFT"}', '{"status": "PUBLISHED"}', '127.0.0.1', NOW()
);
