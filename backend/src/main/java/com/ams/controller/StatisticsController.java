package com.ams.controller;

import com.ams.entity.Statistics;
import com.ams.service.StatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Statistics>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long targetId) {
        
        Page<Statistics> page = new Page<>(current, size);
        LambdaQueryWrapper<Statistics> wrapper = new LambdaQueryWrapper<>();
        
        if (type != null) {
            wrapper.eq(Statistics::getType, type);
        }
        if (targetId != null) {
            wrapper.eq(Statistics::getTargetId, targetId);
        }
        
        return ResponseEntity.ok(statisticsService.page(page, wrapper));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Statistics> getById(@PathVariable Long id) {
        return ResponseEntity.ok(statisticsService.getById(id));
    }

    @GetMapping("/type/{type}/target/{targetId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Statistics> getByTypeAndTargetId(
            @PathVariable String type,
            @PathVariable Long targetId) {
        return ResponseEntity.ok(statisticsService.getByTypeAndTargetId(type, targetId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Statistics> create(@RequestBody Statistics statistics) {
        statisticsService.save(statistics);
        return ResponseEntity.ok(statistics);
    }

    @PutMapping("/type/{type}/target/{targetId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatistics(
            @PathVariable String type,
            @PathVariable Long targetId,
            @RequestBody String statisticsData) {
        statisticsService.updateStatistics(type, targetId, statisticsData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/type/{type}/target/{targetId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @PathVariable String type,
            @PathVariable Long targetId) {
        statisticsService.deleteStatistics(type, targetId);
        return ResponseEntity.ok().build();
    }
} 