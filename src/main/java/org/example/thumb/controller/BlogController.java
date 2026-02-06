package org.example.thumb.controller;


import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.thumb.common.BaseResponse;
import org.example.thumb.common.ResultUtils;
import org.example.thumb.model.entity.Blog;
import org.example.thumb.model.vo.BlogVO;
import org.example.thumb.service.BlogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping("/list")
    public BaseResponse<List<BlogVO>> list(HttpServletRequest request) {
        List<Blog> blogList = blogService.list();
        List<BlogVO> blogVOList = blogService.getBlogVOList(blogList, request);
        return ResultUtils.success(blogVOList);
    }

}
