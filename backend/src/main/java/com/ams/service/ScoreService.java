package com.ams.service;

import com.ams.entity.Score;
import com.baomidou.mybatisplus.extension.service.IService;
import java.math.BigDecimal;

public interface ScoreService extends IService<Score> {
    Score getByCourseSelectionId(Long courseSelectionId);
    
    void updateScore(Long courseSelectionId, BigDecimal score);
    
    void deleteScore(Long courseSelectionId);
} 