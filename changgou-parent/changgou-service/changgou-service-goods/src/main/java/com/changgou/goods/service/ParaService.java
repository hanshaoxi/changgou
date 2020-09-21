package com.changgou.goods.service;


import com.changgou.goods.pojo.Para;

import java.util.List;

public interface ParaService {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Para findById(Integer id);

    /**
     * 添加商品参数
     * @param para
     */
    public void add(Para para);

    public void update(Para para);

    public void delete(Integer id);

    public void delete(Para para);
    public List<Para> findPage(Integer pageSize, Integer pageNum);
    public List<Para> findPage(Integer PageSize,Integer pageNum,Para para);
}
