package com.changgou.goods.mapper;

import com.changgou.goods.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CategoryMapper extends Mapper<Category> {

    @Select("select * from tb_category where parent_id=#{pid}")
    public List<Category> findByParentId(Integer pid);
}
