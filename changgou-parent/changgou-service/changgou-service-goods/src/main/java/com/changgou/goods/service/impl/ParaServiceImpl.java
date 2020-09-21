package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.CategoryMapper;
import com.changgou.goods.mapper.ParaMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    private ParaMapper paraMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Para findById(Integer id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Para para) {

        paraMapper.insertSelective(para);
    }

    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKeySelective(para);
    }

    @Override
    public void delete(Integer id) {
        paraMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Para para) {
        paraMapper.delete(para);
    }

    @Override
    public List<Para> findPage(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return paraMapper.selectAll();
    }

    @Override
    public List<Para> findPage(Integer PageSize, Integer pageNum, Para para) {
        Example example = new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();
        if(para!=null){
             if(para.getTemplateId()>0){
                 criteria.andEqualTo("templateId",para.getTemplateId());
             }
             if(!StringUtils.isEmpty(para.getName())){
                 criteria.andLike("name","%" + para.getName()+"%" );
             }
             if(!StringUtils.isEmpty(para.getOptions())){
                 criteria.andLike("options","%" + para.getOptions() + "%");
             }

        }


        return paraMapper.selectByExample(example);
    }

    @Override
    public List<Para> findByCategoryId(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Para para = new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);
    }
}
