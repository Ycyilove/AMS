-- 创建学生信息表
CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    student_no VARCHAR(20) NOT NULL UNIQUE,
    class VARCHAR(50),
    major VARCHAR(100),
    grade VARCHAR(20),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES `user`(id)
);

-- 创建成绩表
CREATE TABLE score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_selection_id BIGINT NOT NULL,
    score DECIMAL(5,2),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (course_selection_id) REFERENCES course_selection(id)
);

-- 创建统计表
CREATE TABLE statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- 'TEACHER_TITLE', 'COURSE_SCORE', 'STUDENT_CREDIT'
    target_id BIGINT NOT NULL,
    statistics_data JSON NOT NULL,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);

-- 添加索引
CREATE INDEX idx_student_user_id ON student(user_id);
CREATE INDEX idx_student_student_no ON student(student_no);
CREATE INDEX idx_score_course_selection_id ON score(course_selection_id);
CREATE INDEX idx_statistics_type_target ON statistics(type, target_id); 