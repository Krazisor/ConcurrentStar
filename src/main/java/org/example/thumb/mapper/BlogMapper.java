package org.example.thumb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.thumb.model.entity.Blog;

import java.util.Map;

/**
 * @author pine
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    void batchUpdateThumbCount(@Param("countMap") Map<Long, Long> countMap);
}




