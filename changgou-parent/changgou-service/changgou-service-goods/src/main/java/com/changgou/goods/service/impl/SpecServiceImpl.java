package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.SpecMapper;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    private SpecMapper specMapper;

    @Override
    public List<Spec> findPage(int pageSize, int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return specMapper.selectAll();
    }

    @Override
    public List<Spec> findPage(Spec spec, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if(spec!=null){

            if(!StringUtils.isEmpty(spec.getName())){
                criteria.andLike("name","%"+spec.getName()+"%");
            }
            if(!StringUtils.isEmpty(spec.getOptions())){

                criteria.andLike("options","%" + spec.getOptions()+"%");
            }
            if(!StringUtils.isEmpty(spec.getTemplateId())){
                criteria.andEqualTo("templateId",spec.getTemplateId());
            }
        }
        return specMapper.selectByExample(example);
    }

    @Override
    public Spec findById(long id) {
        return specMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKey(spec);
    }

    @Override
    public void delete(long id) {
        specMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Spec spec) {
        specMapper.delete(spec);
    }

    @Override
    public void add(Spec spec) {
        specMapper.insertSelective(spec);
    }
}
