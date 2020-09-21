package com.changgou.goods.service;

import com.changgou.goods.pojo.Spec;

import java.util.List;

public interface SpecService {
    /**
     * 分页查询
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<Spec> findPage(int pageSize,int pageNum);

    /**
     * 根据自定义条件分页查询
     * @param spec
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<Spec> findPage(Spec spec,int pageSize,int pageNum);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Spec findById(long id);

    /**
     * 更新
     * @param spec
     */
    public void update(Spec spec);

    /**
     * 根据id删除
     * @param id
     */
    public void delete(long id);

    /**
     * 删除品牌
     * @param spec
     */
    public void delete(Spec spec);

    /**
     * 添加规格信息
     * @param spec
     */
    public void add(Spec spec);

}
