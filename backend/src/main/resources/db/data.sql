USE ams;

-- 插入管理员用户（密码：admin123）
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `email`, `phone`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'ADMIN', 'admin@example.com', '13800138000');

-- 插入测试学生用户（密码：student123）
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `email`, `phone`)
VALUES ('student1', '$2a$10$8K1p/a0dR1LXMIgoEDFrwOfgqw7B.qpQz5J9QxGzqK9YwGzQxQzXy', '张三', 'STUDENT', 'student1@example.com', '13800138001');

-- 插入测试教师用户（密码：teacher123）
INSERT INTO `user` (`username`, `password`, `real_name`, `role`, `email`, `phone`)
VALUES ('teacher1', '$2a$10$8K1p/a0dR1LXMIgoEDFrwOfgqw7B.qpQz5J9QxGzqK9YwGzQxQzXy', '李四', 'TEACHER', 'teacher1@example.com', '13800138002');

-- 插入教师信息
INSERT INTO `teacher` (`user_id`, `teacher_no`, `title`, `salary`, `department`)
VALUES (3, 'T2023001', '教授', 15000.00, '计算机科学与技术学院');

-- 插入课程信息
INSERT INTO `course` (`course_no`, `course_name`, `teacher_id`, `credit`, `capacity`)
VALUES ('C2023001', 'Java程序设计', 1, 4.0, 50); 