package org.example.thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.example.thumb.model.dto.thumb.DoThumbRequest;
import org.example.thumb.model.entity.Thumb;

/**
 * @author pine
 */
public interface ThumbService extends IService<Thumb> {

    Boolean hasThumb(Long blogId, Long userId);

    /**
     * 点赞
     * @param doThumbRequest 点赞请求
     * @param request Servlet 请求
     * @return {@link Boolean }
     */
    Boolean doThumb(DoThumbRequest doThumbRequest, HttpServletRequest request);

    /**
     * 取消点赞
     * @param doThumbRequest 点赞请求
     * @param request Servlet 请求
     * @return {@link Boolean }
     */
    Boolean undoThumb(DoThumbRequest doThumbRequest, HttpServletRequest request);


}
