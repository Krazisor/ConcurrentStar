package org.example.thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.example.thumb.constant.UserConstant;
import org.example.thumb.mapper.UserMapper;
import org.example.thumb.model.entity.User;
import org.example.thumb.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author pine
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getLoginUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(UserConstant.LOGIN_USER);
    }

}




