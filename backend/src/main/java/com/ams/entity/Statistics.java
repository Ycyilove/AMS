package com.ams.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("statistics")
public class Statistics {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String type;
    
    private Long targetId;
    
    private String statisticsData;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer deleted;
} 