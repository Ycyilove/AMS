package com.ams.service;

import com.ams.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    Student getByUserId(Long userId);
    
    Student getByStudentNo(String studentNo);
    
    void updateStudentInfo(Student student);
} 