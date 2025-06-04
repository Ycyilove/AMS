package com.ams.controller;

import com.ams.entity.Score;
import com.ams.service.ScoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Score>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long courseSelectionId) {
        
        Page<Score> page = new Page<>(current, size);
        LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
        
        if (courseSelectionId != null) {
            wrapper.eq(Score::getCourseSelectionId, courseSelectionId);
        }
        
        return ResponseEntity.ok(scoreService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Score> getById(@PathVariable Long id) {
        return ResponseEntity.ok(scoreService.getById(id));
    }

    @GetMapping("/course-selection/{courseSelectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Score> getByCourseSelectionId(@PathVariable Long courseSelectionId) {
        return ResponseEntity.ok(scoreService.getByCourseSelectionId(courseSelectionId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Score> create(@RequestBody Score score) {
        scoreService.save(score);
        return ResponseEntity.ok(score);
    }

    @PutMapping("/course-selection/{courseSelectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateScore(
            @PathVariable Long courseSelectionId,
            @RequestParam BigDecimal score) {
        scoreService.updateScore(courseSelectionId, score);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/course-selection/{courseSelectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long courseSelectionId) {
        scoreService.deleteScore(courseSelectionId);
        return ResponseEntity.ok().build();
    }
} 