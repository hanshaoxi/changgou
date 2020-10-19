package com.changgou.goods.mapper;

import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Select("select tb.* from tb_brand tb,tb_category_brand tcb where tb.id = tcb.brand_id and tcb.category_id=#{catogoryId}")
    public List<Brand> findByCatotoryId(Integer categoryId);
}
