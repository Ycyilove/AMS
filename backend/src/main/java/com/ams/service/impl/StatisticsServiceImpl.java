package com.ams.service.impl;

import com.ams.entity.Statistics;
import com.ams.mapper.StatisticsMapper;
import com.ams.service.StatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Override
    public Statistics getByTypeAndTargetId(String type, Long targetId) {
        return getOne(new LambdaQueryWrapper<Statistics>()
                .eq(Statistics::getType, type)
                .eq(Statistics::getTargetId, targetId));
    }

    @Override
    public void updateStatistics(String type, Long targetId, String statisticsData) {
        Statistics existingStatistics = getByTypeAndTargetId(type, targetId);
        if (existingStatistics != null) {
            existingStatistics.setStatisticsData(statisticsData);
            updateById(existingStatistics);
        } else {
            Statistics newStatistics = new Statistics();
            newStatistics.setType(type);
            newStatistics.setTargetId(targetId);
            newStatistics.setStatisticsData(statisticsData);
            save(newStatistics);
        }
    }

    @Override
    public void deleteStatistics(String type, Long targetId) {
        remove(new LambdaQueryWrapper<Statistics>()
                .eq(Statistics::getType, type)
                .eq(Statistics::getTargetId, targetId));
    }
} 