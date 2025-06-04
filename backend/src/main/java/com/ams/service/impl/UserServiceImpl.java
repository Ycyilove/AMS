package com.ams.service.impl;

import com.ams.entity.User;
import com.ams.mapper.UserMapper;
import com.ams.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        User user = getById(userId);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            updateById(user);
        }
    }

    @Override
    public void updateUserInfo(User user) {
        User existingUser = getById(user.getId());
        if (existingUser != null) {
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            updateById(existingUser);
        }
    }
} 