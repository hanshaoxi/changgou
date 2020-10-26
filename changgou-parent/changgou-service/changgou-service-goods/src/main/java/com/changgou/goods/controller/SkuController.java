package com.changgou.goods.controller;

import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import entity.Result;
import entity.ResultUtil;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;


    @PostMapping
    public Result add(@RequestBody Sku sku){
        skuService.add(sku);
        return ResultUtil.addSuccess();
    }
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable("id") Long id){
        return ResultUtil.findSuccess(skuService.findById(id));
    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result<List<Sku>> findPage(@PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize){
        return ResultUtil.findSuccess(skuService.findPage(pageNum,pageSize));
    }
    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<List<Sku>> findPage(@RequestBody Sku sku,
                                      @PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize){
        return ResultUtil.findSuccess(skuService.findPage(sku,pageNum,pageSize));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id){
        skuService.delete(id);
        return ResultUtil.deleteSuccess();
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id") Long id ,
                         @RequestBody Sku sku){
        skuService.update(sku);
        return ResultUtil.updateSuccess();
    }

    @GetMapping
    public Result<List<Sku>> findSkuList(){
        List<Sku> skuList = skuService.findAll();
        return  new Result(true,StatusCode.OK,"查询成功",skuList);
    }
}
