package com.changgou.goods.service;

import com.changgou.goods.pojo.Category;

import java.util.List;

public interface CategoryService {

    public List<Category> findByParentId(Integer pid);

    public Category findById(Integer id);
}
