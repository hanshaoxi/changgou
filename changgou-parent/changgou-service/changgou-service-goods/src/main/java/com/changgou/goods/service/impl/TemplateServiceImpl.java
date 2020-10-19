package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.TemplateMapper;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    @Override
    public void update(Template template) {
        templateMapper.updateByPrimaryKey(template);
    }

    @Override
    public Template findById(Integer id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Template> findByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Template>(templateMapper.selectAll());
    }

    @Override
    public List<Template> findList(Template template) {
        return templateMapper.select(template);
    }

    @Override
    public PageInfo<Template> findPage(Template template, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(templateMapper.select(template));
    }

    @Override
    public void delete(Template template) {

        templateMapper.delete(template);

    }

    @Override
    public void delete(Integer id) {
        templateMapper.deleteByPrimaryKey(id);
    }
}
