package com.ams.service.impl;

import com.ams.entity.Teacher;
import com.ams.mapper.TeacherMapper;
import com.ams.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public Map<String, Integer> getTitleCount() {
        List<Teacher> teachers = list();
        return teachers.stream()
                .collect(Collectors.groupingBy(
                        Teacher::getTitle,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    @Override
    public Map<String, Double> getTitleAvgSalary() {
        List<Teacher> teachers = list();
        Map<String, List<Teacher>> teachersByTitle = teachers.stream()
                .collect(Collectors.groupingBy(Teacher::getTitle));

        Map<String, Double> result = new HashMap<>();
        teachersByTitle.forEach((title, teacherList) -> {
            double avgSalary = teacherList.stream()
                    .mapToDouble(t -> t.getSalary().doubleValue())
                    .average()
                    .orElse(0.0);
            result.put(title, avgSalary);
        });

        return result;
    }
}