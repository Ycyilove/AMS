package com.ams.service;

import com.ams.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface CourseService extends IService<Course> {
    Map<String, Object> getCourseStatistics(Long teacherId);
}