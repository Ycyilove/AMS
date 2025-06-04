package com.ams.service.impl;

import com.ams.entity.Student;
import com.ams.mapper.StudentMapper;
import com.ams.service.StudentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public Student getByUserId(Long userId) {
        return getOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getUserId, userId));
    }

    @Override
    public Student getByStudentNo(String studentNo) {
        return getOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getStudentNo, studentNo));
    }

    @Override
    public void updateStudentInfo(Student student) {
        Student existingStudent = getById(student.getId());
        if (existingStudent != null) {
            existingStudent.setClass_(student.getClass_());
            existingStudent.setMajor(student.getMajor());
            existingStudent.setGrade(student.getGrade());
            updateById(existingStudent);
        }
    }
} 