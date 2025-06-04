package com.ams.controller;

import com.ams.common.Result;
import com.ams.entity.Course;
import com.ams.entity.CourseSelection;
import com.ams.entity.User;
import com.ams.service.CourseSelectionService;
import com.ams.service.CourseService;
import com.ams.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSelectionService courseSelectionService;

    // 个人信息管理
    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getByUsername(username);
        
        user.setId(currentUser.getId());
        userService.updateUserInfo(user);
        return Result.success(user);
    }

    // 选课管理
    @PostMapping("/courses/{courseId}/select")
    public Result<Void> selectCourse(@PathVariable Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User student = userService.getByUsername(username);

        Course course = courseService.getById(courseId);
        if (course == null) {
            return Result.error("课程不存在");
        }

        if (course.getSelected() >= course.getCapacity()) {
            return Result.error("课程已满");
        }

        // 检查是否已经选过这门课
        CourseSelection existingSelection = courseSelectionService.getOne(
                new LambdaQueryWrapper<CourseSelection>()
                        .eq(CourseSelection::getStudentId, student.getId())
                        .eq(CourseSelection::getCourseId, courseId)
        );

        if (existingSelection != null) {
            return Result.error("已经选过这门课");
        }

        // 创建选课记录
        CourseSelection selection = new CourseSelection();
        selection.setStudentId(student.getId());
        selection.setCourseId(courseId);
        courseSelectionService.save(selection);

        // 更新课程已选人数
        course.setSelected(course.getSelected() + 1);
        courseService.updateById(course);

        return Result.success();
    }

    @DeleteMapping("/courses/{courseId}/drop")
    public Result<Void> dropCourse(@PathVariable Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User student = userService.getByUsername(username);

        CourseSelection selection = courseSelectionService.getOne(
                new LambdaQueryWrapper<CourseSelection>()
                        .eq(CourseSelection::getStudentId, student.getId())
                        .eq(CourseSelection::getCourseId, courseId)
        );

        if (selection == null) {
            return Result.error("未选择该课程");
        }

        // 删除选课记录
        courseSelectionService.removeById(selection.getId());

        // 更新课程已选人数
        Course course = courseService.getById(courseId);
        course.setSelected(course.getSelected() - 1);
        courseService.updateById(course);

        return Result.success();
    }

    // 成绩查询
    @GetMapping("/courses/scores")
    public Result<List<CourseSelection>> getScores() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User student = userService.getByUsername(username);

        List<CourseSelection> selections = courseSelectionService.list(
                new LambdaQueryWrapper<CourseSelection>()
                        .eq(CourseSelection::getStudentId, student.getId())
        );

        return Result.success(selections);
    }

    // 学分统计
    @GetMapping("/statistics/credits")
    public Result<Map<String, BigDecimal>> getCreditStatistics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User student = userService.getByUsername(username);

        Map<String, BigDecimal> statistics = new HashMap<>();
        statistics.put("totalCredits", courseSelectionService.getTotalCredits(student.getId()));
        statistics.put("completedCredits", courseSelectionService.getCompletedCredits(student.getId()));

        return Result.success(statistics);
    }
} 