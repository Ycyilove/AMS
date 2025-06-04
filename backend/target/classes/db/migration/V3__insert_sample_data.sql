-- 插入学生信息
INSERT INTO student (user_id, student_no, class, major, grade) VALUES
(8, 'S2023001', '计算机2301', '计算机科学与技术', '2023');

-- 插入选课记录
INSERT INTO course_selection (student_id, course_id) VALUES
(1, 1);

-- 插入成绩
INSERT INTO score (course_selection_id, score) VALUES
(1, 85.5);

-- 插入统计示例数据
INSERT INTO statistics (type, target_id, statistics_data) VALUES
('TEACHER_TITLE', 1, '{"教授": 1, "副教授": 0, "讲师": 0}'),
('COURSE_SCORE', 1, '{"平均分": 85.5, "最高分": 85.5, "最低分": 85.5}'),
('STUDENT_CREDIT', 1, '{"总学分": 4, "已修学分": 4}'); 