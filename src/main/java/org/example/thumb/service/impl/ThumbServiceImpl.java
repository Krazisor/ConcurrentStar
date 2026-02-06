package org.example.thumb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.thumb.constant.ThumbConstant;
import org.example.thumb.mapper.ThumbMapper;
import org.example.thumb.model.dto.thumb.DoThumbRequest;
import org.example.thumb.model.entity.Blog;
import org.example.thumb.model.entity.Thumb;
import org.example.thumb.model.entity.User;
import org.example.thumb.service.BlogService;
import org.example.thumb.service.ThumbService;
import org.example.thumb.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author tuhaoran
 */
@Service("thumbServiceLocalCache")
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final UserService userService;

    private final BlogService blogService;

    private final TransactionTemplate transactionTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean hasThumb(Long blogId, Long userId) {
        return redisTemplate.opsForHash().hasKey(ThumbConstant.USER_THUMB_KEY_PREFIX + userId, blogId.toString());
    }


    @Override
    public Boolean doThumb(DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new RuntimeException("参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        // 修改，细化锁粒度，使用userID+blogID的形式加锁
        synchronized ((loginUser.getId().toString() + doThumbRequest.getBlogId().toString()).intern()) {
            return transactionTemplate.execute(status -> {
                Long blogId = doThumbRequest.getBlogId();
                Boolean exists = this.hasThumb(blogId, loginUser.getId());
                if (exists) {
                    throw new RuntimeException("用户已经点赞");
                }
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount + 1")
                        .update();
                Thumb thumb = new Thumb();
                thumb.setBlogId(blogId);
                thumb.setUserId(loginUser.getId());
                boolean success = update && this.save(thumb);
                if (success) {
                    redisTemplate.opsForHash().put(
                            ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId().toString(),
                            blogId.toString(),
                            thumb.getId());
                }
                return success;
            });
        }
    }

    @Override
    public Boolean undoThumb(DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new RuntimeException("参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        synchronized ((loginUser.getId().toString() + doThumbRequest.getBlogId().toString()).intern()) {
            return transactionTemplate.execute(status -> {
                Long blogId = doThumbRequest.getBlogId();
                Object thumbObj = redisTemplate.opsForHash()
                        .get(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId().toString(), blogId.toString());
                if (thumbObj == null) {
                    throw new RuntimeException("用户未点赞");
                }
                Long thumbId = Long.valueOf(thumbObj.toString());
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount - 1")
                        .update();
                boolean success = update && this.removeById(thumbId);
                if (success) {
                    redisTemplate.opsForHash()
                            .delete(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId().toString(), blogId.toString());
                }
                return success;
            });
        }
    }
}




