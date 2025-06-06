package com.ams.controller;

import com.ams.common.Result;
import com.ams.dto.LoginDTO;
import com.ams.entity.User;
import com.ams.service.UserService;
import com.ams.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            // 先验证用户是否存在且角色是否正确
            User user = userService.getByLoginDTO(loginDTO);
            if (user == null) {
                return Result.error("不存在对应用户");
            }
            
            // 验证用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(loginDTO.getUsername(), user.getRole());
            
            // 调试信息
            System.out.println("Generated JWT token: " + jwt);
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", jwt);
            result.put("user", user);
            
            return Result.success(result);
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials: " + e.getMessage());
            return Result.error("用户名或密码错误");
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return Result.error("登录失败：" + e.getMessage());
        }
    }
} 