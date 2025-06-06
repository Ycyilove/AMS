package com.ams.service;

import com.ams.dto.LoginDTO;
import com.ams.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    
    User getByLoginDTO(LoginDTO loginDTO);
    
    void resetPassword(Long userId, String newPassword);
    
    void updateUserInfo(User user);
} 