package com.changgou.goods.controller;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import entity.IdWorker;
import entity.Result;
import entity.ResultUtil;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spu")
public class SpuController {

    @Autowired
    private SpuService spuService;


    @PostMapping
    public Result add(@RequestBody Spu spu){
        spuService.add(spu);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id,Spu spu){
        spu.setId(id);
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable("id") Long id){
        return new Result(true,StatusCode.OK,"查询成功",spuService.findById(id));
    }
    @GetMapping("/{pageNum}/{pageSize}")
    public Result<List<Spu>> findPage(@PathVariable("pageNum") Integer pageNum, @PathVariable("pageSize") Integer pageSize){

        return new Result<>(true,StatusCode.OK,"查询成功",spuService.findPage(pageNum,pageSize));
    }
    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<List<Spu>> findPage(@RequestBody Spu spu,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize){

        return  new Result<>(true,StatusCode.OK,"查询成功",spuService.findPage(spu,pageNum,pageSize));
    }

    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods){
        spuService.saveGoods(goods);
        return ResultUtil.addSuccess();
    }
    @GetMapping("/goods/{id}")
    public Result<Goods> findBySpuId(@PathVariable("id") Long spuId){
        Goods goods = spuService.findGoodsById(spuId);
        return ResultUtil.findSuccess(goods);
    }

    @PutMapping("/audit/{id}")
    public Result audit(@PathVariable("id") Long spuId){
        spuService.audit(spuId);
        return ResultUtil.updateSuccess("审核成功");
    }

    @PutMapping("/pull/{id}")
    public Result pull(@PathVariable("id") Long spuId){
        spuService.pull(spuId);
        return ResultUtil.updateSuccess("下架成功");
    }
    @PutMapping("/put/{id}")
    public Result put(@PathVariable("id") Long spuId){
        spuService.put(spuId);
        return ResultUtil.updateSuccess("上架成功");
    }
    @PutMapping("/put/many")
    public Result put(@RequestBody Long[] ids){
        spuService.putMany(ids);
        return ResultUtil.updateSuccess("批量上架成功");
    }

}
