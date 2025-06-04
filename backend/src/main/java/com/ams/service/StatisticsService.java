package com.ams.service;

import com.ams.entity.Statistics;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StatisticsService extends IService<Statistics> {
    Statistics getByTypeAndTargetId(String type, Long targetId);
    
    void updateStatistics(String type, Long targetId, String statisticsData);
    
    void deleteStatistics(String type, Long targetId);
} 