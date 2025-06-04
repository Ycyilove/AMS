package com.ams.service.impl;

import com.ams.entity.Course;
import com.ams.entity.CourseSelection;
import com.ams.mapper.CourseMapper;
import com.ams.service.CourseService;
import com.ams.service.CourseSelectionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Lazy
    @Autowired
    private CourseSelectionService courseSelectionService;

    @Override
    public Map<String, Object> getCourseStatistics(Long teacherId) {
        List<Course> courses = list(new LambdaQueryWrapper<Course>()
                .eq(Course::getTeacherId, teacherId));

        Map<String, Object> result = new HashMap<>();
        for (Course course : courses) {
            Map<String, Object> courseStats = new HashMap<>();
            List<CourseSelection> selections = courseSelectionService.list(
                    new LambdaQueryWrapper<CourseSelection>()
                            .eq(CourseSelection::getCourseId, course.getId())
                            .isNotNull(CourseSelection::getScore)
            );

            if (!selections.isEmpty()) {
                BigDecimal avgScore = selections.stream()
                        .map(CourseSelection::getScore)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(selections.size()), 2, BigDecimal.ROUND_HALF_UP);

                BigDecimal maxScore = selections.stream()
                        .map(CourseSelection::getScore)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);

                BigDecimal minScore = selections.stream()
                        .map(CourseSelection::getScore)
                        .min(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);

                courseStats.put("avgScore", avgScore);
                courseStats.put("maxScore", maxScore);
                courseStats.put("minScore", minScore);
                courseStats.put("studentCount", selections.size());
            }

            result.put(course.getCourseName(), courseStats);
        }

        return result;
    }
} 