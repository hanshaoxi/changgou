package com.changgou.goods.controller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.ResultUtil;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌信息
     * @return
     */
    @GetMapping
    public Result<Brand> findAll(){
        List<Brand> brandList = brandService.findAll();
        return  new Result(true, StatusCode.OK,"查询成功",brandList);
    }
    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",brand);
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id ,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    /**添加品牌*/
    @PostMapping
    public Result add(@RequestBody Brand brand){

        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加品牌信息成功");
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        Brand brand = new Brand();
        brand.setId(id);
        brandService.delete(brand);
        return  new Result(true,StatusCode.OK,"删除成功");
    }

    @PostMapping("/search")
    public Result<Brand> findBrandList(@RequestBody(required = false) Brand brand){
        List<Brand> brandList = brandService.findBrandList(brand);
        return new Result(true,StatusCode.OK,"查询成功",brandList);

    }

    @GetMapping("/{pageNum}/{pageSize}")
    public Result<Brand> findPage(@PathVariable Integer pageNum , @PathVariable Integer pageSize){
       PageInfo<Brand> pageInfo =  brandService.findPage(pageNum,pageSize);
       return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    @PostMapping("/search/{pageNum}/{pageSize}")
    public Result<Brand> findPage(@RequestBody(required = false) Brand brand,@PathVariable Integer pageNum,@PathVariable Integer pageSize){
        PageInfo<Brand> pageInfo = brandService.findPage(brand,pageNum,pageSize);
        return new Result<Brand>(true,StatusCode.OK,"查询成功",pageInfo);
    }
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findByCategoryId(@PathVariable("id") Integer categoryId){

        return ResultUtil.findSuccess(brandService.findByCategoryId(categoryId));
    }



}
