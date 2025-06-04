package com.ams.service.impl;

import com.ams.entity.Course;
import com.ams.entity.CourseSelection;
import com.ams.mapper.CourseSelectionMapper;
import com.ams.service.CourseSelectionService;
import com.ams.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CourseSelectionServiceImpl extends ServiceImpl<CourseSelectionMapper, CourseSelection> implements CourseSelectionService {

    @Lazy
    @Autowired
    private CourseService courseService;

    @Override
    public BigDecimal getTotalCredits(Long studentId) {
        List<CourseSelection> selections = list(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, studentId));

        return selections.stream()
                .map(selection -> {
                    Course course = courseService.getById(selection.getCourseId());
                    return course != null ? course.getCredit() : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getCompletedCredits(Long studentId) {
        List<CourseSelection> selections = list(new LambdaQueryWrapper<CourseSelection>()
                .eq(CourseSelection::getStudentId, studentId)
                .isNotNull(CourseSelection::getScore)
                .ge(CourseSelection::getScore, new BigDecimal("60")));

        return selections.stream()
                .map(selection -> {
                    Course course = courseService.getById(selection.getCourseId());
                    return course != null ? course.getCredit() : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}