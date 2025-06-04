package com.ams.service;

import com.ams.entity.CourseSelection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface CourseSelectionService extends IService<CourseSelection> {
    BigDecimal getTotalCredits(Long studentId);
    
    BigDecimal getCompletedCredits(Long studentId);
}