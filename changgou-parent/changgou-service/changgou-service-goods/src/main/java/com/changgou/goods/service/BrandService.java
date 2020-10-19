package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;


public interface BrandService {
    /**
     * 查询所有品牌
     * @return
     */
    public List<Brand> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Brand findById(Integer id);
    /**
     * 更新品牌信息
     */

    public void update(Brand brand);

    /**
     * 删除品牌信息
     * @param brand
     */
    public void delete(Brand brand);

    /**
     * 添加品牌信息
     * @param brand
     */
    public void add(Brand brand);

    /**
     * 根据自定义条件查询
     * @param brand
     * @return
     */
    public List<Brand> findBrandList(Brand brand);

    public PageInfo<Brand> findPage(int pageNum , int pageSize);

    /**
     * 根据自定义条件分页查询
     * @param brand
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Brand> findPage(Brand brand ,Integer pageNum ,Integer pageSize);

    public List<Brand> findByCategoryId(Integer categoryId);
}
