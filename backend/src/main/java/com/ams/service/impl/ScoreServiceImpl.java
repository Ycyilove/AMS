package com.ams.service.impl;

import com.ams.entity.Score;
import com.ams.mapper.ScoreMapper;
import com.ams.service.ScoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements ScoreService {

    @Override
    public Score getByCourseSelectionId(Long courseSelectionId) {
        return getOne(new LambdaQueryWrapper<Score>()
                .eq(Score::getCourseSelectionId, courseSelectionId));
    }

    @Override
    public void updateScore(Long courseSelectionId, BigDecimal score) {
        Score existingScore = getByCourseSelectionId(courseSelectionId);
        if (existingScore != null) {
            existingScore.setScore(score);
            updateById(existingScore);
        } else {
            Score newScore = new Score();
            newScore.setCourseSelectionId(courseSelectionId);
            newScore.setScore(score);
            save(newScore);
        }
    }

    @Override
    public void deleteScore(Long courseSelectionId) {
        remove(new LambdaQueryWrapper<Score>()
                .eq(Score::getCourseSelectionId, courseSelectionId));
    }
} 