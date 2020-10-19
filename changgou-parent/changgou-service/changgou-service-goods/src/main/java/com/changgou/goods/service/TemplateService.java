package com.changgou.goods.service;

import com.changgou.goods.pojo.Template;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TemplateService {
    /**
     * 添加模板
     * @param template
     */
    public void add(Template template);
    /**修改*/
    public void update(Template template);
    /**根据id查询*/
    public Template findById(Integer id);
    /**分页查询*/
    public PageInfo<Template> findByPage(Integer pageNum , Integer pageSize);
    /**根据自定义条件查询*/
    public List<Template> findList(Template template);
    /**根据条件自定义分页查询*/
    public PageInfo<Template> findPage(Template template,Integer pageNum,Integer pageSize);
    /**删除*/
    public void delete(Template template);
    /**根据id删除*/
    public void delete(Integer id);

}
