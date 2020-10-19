package com.changgou.goods.service.impl;

import com.changgou.goods.mapper.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Authorization;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);

    }

    @Override
    public void delete(Brand brand) {
        brandMapper.delete(brand);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    /**
     * 自定义条件查询
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findBrandList(Brand brand) {

        List<Brand> brandList =  brandMapper.selectByExample(createExample(brand));

        return  brandList;
    }

    @Override
    public PageInfo<Brand> findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<Brand>(brandMapper.selectAll());
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Brand> brandList = brandMapper.selectByExample(createExample(brand));
        return new PageInfo<Brand>(brandList);
    }

    @Override
    public List<Brand> findByCategoryId(Integer categoryId) {
        return brandMapper.findByCatotoryId(categoryId);
    }

    private Example createExample(Brand brand){
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(brand!=null){
            if(brand.getId()!=null && brand.getId()>=0){
                criteria.andEqualTo("id",brand.getId());
            }
            if(!StringUtils.isEmpty(brand.getName())){
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            if(!StringUtils.isEmpty(brand.getImage())){
                criteria.andLike("image","%"+brand.getImage()+"%");
            }
            if(!StringUtils.isEmpty(brand.getLetter())){

                criteria.andLike("letter","%"+brand.getLetter()+"%");
            }
            if(!StringUtils.isEmpty(brand.getSeq()+"")){
                criteria.andEqualTo("seq",brand.getSeq());
            }
        }
        return example;
    }
}
