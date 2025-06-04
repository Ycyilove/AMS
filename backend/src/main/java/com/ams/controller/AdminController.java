package com.ams.controller;

import com.ams.common.Result;
import com.ams.entity.Course;
import com.ams.entity.CourseSelection;
import com.ams.entity.Teacher;
import com.ams.entity.User;
import com.ams.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSelectionService courseSelectionService;

    // 用户管理
    @PostMapping("/users")
    public Result<User> createUser(@RequestBody User user) {
        userService.save(user);
        return Result.success(user);
    }

    @PutMapping("/users/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.success(user);
    }

    @DeleteMapping("/users/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    @PostMapping("/users/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> params) {
        userService.resetPassword(id, params.get("newPassword"));
        return Result.success();
    }

    // 教师管理
    @PostMapping("/teachers")
    public Result<Teacher> createTeacher(@RequestBody Teacher teacher) {
        teacherService.save(teacher);
        return Result.success(teacher);
    }

    @PutMapping("/teachers/{id}")
    public Result<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        teacher.setId(id);
        teacherService.updateById(teacher);
        return Result.success(teacher);
    }

    @DeleteMapping("/teachers/{id}")
    public Result<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.removeById(id);
        return Result.success();
    }

    // 课程管理
    @PostMapping("/courses")
    public Result<Course> createCourse(@RequestBody Course course) {
        courseService.save(course);
        return Result.success(course);
    }

    @PutMapping("/courses/{id}")
    public Result<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        courseService.updateById(course);
        return Result.success(course);
    }

    @DeleteMapping("/courses/{id}")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.removeById(id);
        return Result.success();
    }

    // 成绩管理
    @PutMapping("/course-selections/{id}/score")
    public Result<CourseSelection> updateScore(@PathVariable Long id, @RequestBody Map<String, BigDecimal> params) {
        CourseSelection selection = courseSelectionService.getById(id);
        selection.setScore(params.get("score"));
        courseSelectionService.updateById(selection);
        return Result.success(selection);
    }

    // 统计信息
    @GetMapping("/statistics/teachers")
    public Result<Map<String, Object>> getTeacherStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("titleCount", teacherService.getTitleCount());
        statistics.put("titleAvgSalary", teacherService.getTitleAvgSalary());
        return Result.success(statistics);
    }

    @GetMapping("/statistics/courses/{teacherId}")
    public Result<Map<String, Object>> getCourseStatistics(@PathVariable Long teacherId) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("courseStats", courseService.getCourseStatistics(teacherId));
        return Result.success(statistics);
    }

    @GetMapping("/statistics/students/{studentId}")
    public Result<Map<String, Object>> getStudentStatistics(@PathVariable Long studentId) {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCredits", courseSelectionService.getTotalCredits(studentId));
        statistics.put("completedCredits", courseSelectionService.getCompletedCredits(studentId));
        return Result.success(statistics);
    }
} 