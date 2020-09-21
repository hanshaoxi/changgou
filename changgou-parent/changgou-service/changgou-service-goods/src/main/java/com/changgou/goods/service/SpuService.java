package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;

import java.util.List;

public interface SpuService  {

    public void add(Spu spu);
    public void update(Spu spu);
    public Spu findById(Long id);
    public List<Spu> findPage(Integer pageNum,Integer pageSize);
    public List<Spu> findPage(Spu spu,Integer pageNum,Integer pageSize);
    public void delete(Long id);
    public void delete(Spu spu);
    public void saveGoods(Goods goods);
    public Goods findGoodsById(Long spuId);
    public void audit(Long spuId);
    public void pull(Long spuId);
    public void put(Long spuId);
    public void putMany(Long[] spuIds);

}
