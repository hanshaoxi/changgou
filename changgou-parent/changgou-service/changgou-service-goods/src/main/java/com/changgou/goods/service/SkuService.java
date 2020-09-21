package com.changgou.goods.service;

import com.changgou.goods.pojo.Sku;

import java.util.List;

public interface SkuService {

    public void add(Sku sku);

    public void update(Sku sku);

    public Sku findById(Long id);

    public List<Sku> findPage(Integer pageNum,Integer pageSize);
    public List<Sku> findPage(Sku sku, Integer pageNum, Integer pageSize);
    public void delete(Long id);
    public void delete(Sku sku);
}
