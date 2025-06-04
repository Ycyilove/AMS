package com.ams.service;

import com.ams.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface TeacherService extends IService<Teacher> {
    Map<String, Integer> getTitleCount();
    
    Map<String, Double> getTitleAvgSalary();
} 