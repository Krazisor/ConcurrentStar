package org.example.thumb.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.example.thumb.model.entity.Blog;
import org.example.thumb.model.vo.BlogVO;

import java.util.List;

/**
 * @author pine
 */
public interface BlogService extends IService<Blog> {

    BlogVO getBlogVOById(long blogId, HttpServletRequest request);

    List<BlogVO> getBlogVOList(List<Blog> blogList, HttpServletRequest request);

}
