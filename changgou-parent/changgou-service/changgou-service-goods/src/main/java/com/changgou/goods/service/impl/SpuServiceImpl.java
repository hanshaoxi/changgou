package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.mapper.BrandMapper;
import com.changgou.goods.mapper.CategoryMapper;
import com.changgou.goods.mapper.SkuMapper;
import com.changgou.goods.mapper.SpuMapper;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.PageHelper;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BrandMapper brandMapper;


    @Override
    public void add(Spu spu) {
        spuMapper.insertSelective(spu);
    }

    @Override
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public Spu findById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Spu> findPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return spuMapper.selectAll();
    }

    @Override
    public List<Spu> findPage(Spu spu, Integer pageNum, Integer pageSize) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu != null){

            if(!StringUtils.isEmpty(spu.getName())){
                criteria.andLike("name","%" + spu.getName()+"%");
            }
            if(!StringUtils.isEmpty(spu.getSaleService())){
                criteria.andLike("saleService","%" +spu.getSaleService()+"%");
            }
            if(spu.getCategory1Id()>=0){
                criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            if(spu.getCategory2Id()>=0){
                criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            if(spu.getCategory3Id()>=0){
                criteria.andEqualTo("category3Id",spu.getCategory3Id());


            }
        }
        return spuMapper.selectByExample(example);
    }

    @Override
    public void delete(Long id) {
        spuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(Spu spu) {
        spuMapper.delete(spu);
    }

    @Override
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        //判断spu的id是否为空，如果为空则为新增，不为空则为修改
        if(spu.getId() == null){
            spu.setId(idWorker.nextId());
            spuMapper.insertSelective(spu);
        }else {
            //更新spu
            spuMapper.updateByPrimaryKeySelective(spu);
            Sku sku = new Sku();
            sku.setSpuId(spu.getId());
            //根据spu_id删除原有的sku
            skuMapper.delete(sku);
        }

        Date date = new Date();
        for(Sku sku:goods.getSkuList()){
            sku.setId(idWorker.nextId());

            String name = spu.getName();

            Map<String,String> spec = JSON.parseObject(sku.getSpec(), Map.class);
            for(Map.Entry<String,String> sp:spec.entrySet()){
                name += " " +sp.getValue();
            }
            sku.setName(name);
            sku.setSpuId(spu.getId());
            sku.setCreateTime(date);
            sku.setUpdateTime(date);
            sku.setCategoryId(spu.getCategory3Id());
            sku.setCategoryName(categoryMapper.selectByPrimaryKey(spu.getCategory3Id()).getName());
            sku.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            skuMapper.insertSelective(sku);


        }

    }

    @Override
    public Goods findGoodsById(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

    @Override
    public void audit(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        //判断商品是否已经删除
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("商品已经删除");
        }
        //商品审核
        spu.setStatus("1");
        //商品上架
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void pull(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("已经删除的商品不能下架");
        }
        //下架
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void put(Long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("商品已删除不允许上架");
        }
        if(!spu.getStatus().equalsIgnoreCase("1")){
            throw new RuntimeException("商品未审核不允许上架");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 批量上架操作
     * @param spuIds
     */
    @Override
    public void putMany(Long[] spuIds) {
       Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(spuIds));
        //状态为已审核
        criteria.andEqualTo("status","1");
        criteria.andEqualTo("isDelete","0");
        Spu spu =new Spu();
        spu.setIsMarketable("1");
        spuMapper.updateByExample(spu,example);
    }
}
