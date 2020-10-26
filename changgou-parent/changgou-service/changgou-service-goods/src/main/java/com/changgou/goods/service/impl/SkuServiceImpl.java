package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.SkuMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public void add(Sku sku) {
        skuMapper.insertSelective(sku);
    }

    @Override
    public void update(Sku sku) {
        skuMapper.delete(sku);
    }

    @Override
    public Sku findById(Long id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Sku> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return skuMapper.selectAll();
    }

    @Override
    public List<Sku> findPage(Sku sku, Integer pageNum, Integer pageSize) {
        Example example = new Example(Sku.class);

        Example.Criteria criteria = example.createCriteria();
        if(sku!=null){
            if(!StringUtils.isEmpty(sku.getBrandName())){
                criteria.andLike("brandName","%" + sku.getBrandName()+"%");
            }
            if(!StringUtils.isEmpty(sku.getName())){
                criteria.andLike("name","%" + sku.getName()+"%");
            }
            if(!StringUtils.isEmpty(sku.getSaleNum())){
                criteria.andEqualTo("saleNum",sku.getSaleNum());
            }
            if(!StringUtils.isEmpty(sku.getPrice())){
                criteria.andEqualTo("price",sku.getPrice());
            }
        }

        return skuMapper.selectByExample(example);
    }

    @Override
    public void delete(Long id) {

        skuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Sku sku) {
        skuMapper.delete(sku);
    }

    @Override
    public List<Sku> findAll() {
        return skuMapper.selectAll();
    }
}
