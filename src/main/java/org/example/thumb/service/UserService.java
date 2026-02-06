package org.example.thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.example.thumb.model.entity.User;

/**
 * @author pine
 */
public interface UserService extends IService<User> {

    User getLoginUser(HttpServletRequest request);
}
